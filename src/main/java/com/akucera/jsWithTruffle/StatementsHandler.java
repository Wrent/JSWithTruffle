package com.akucera.jsWithTruffle;

import com.akucera.jsWithTruffle.exceptions.UnknownSyntaxException;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralBooleanNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralNumberNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralStringNode;
import com.akucera.jsWithTruffle.javascript.nodes.*;
import com.akucera.jsWithTruffle.javascript.nodes.JSBinaryNode;
import com.akucera.jsWithTruffle.javascript.nodes.JSVarNodeGen;
import com.akucera.jsWithTruffle.javascript.operations.OpCode;
import com.akucera.jsWithTruffle.javascript.types.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import jdk.nashorn.internal.ir.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by akucera on 26.12.16.
 */
public class StatementsHandler {
    public static final Stack<FrameDescriptor> frameDescriptors = new Stack<>();
    public static PrintStream out;
    public static InputStream in;


    static {
        frameDescriptors.add(new FrameDescriptor());
    }

    public static List<JSNode> handle(List<Statement> statements, InputStream in, PrintStream out) throws UnknownSyntaxException {
        ArrayList<JSNode> list = new ArrayList<>();

        StatementsHandler.out = out;
        StatementsHandler.in = in;

        for (Statement s : statements) {
            handleStatement(list, s);
        }
        //System.out.println(list);
        return list;
    }

    private static void handleStatement(List<JSNode> list, Statement s) throws UnknownSyntaxException {
        switch (s.getClass().getName()) {
            case "jdk.nashorn.internal.ir.VarNode":
                list.add(handleVarNode(s));
                break;
            case "jdk.nashorn.internal.ir.IfNode":
                list.add(handleIfNode(s));
                break;
            case "jdk.nashorn.internal.ir.WhileNode":
                list.add(handleWhileNode(s));
                break;
            case "jdk.nashorn.internal.ir.ExpressionStatement":
                list.add(handleExpressionStatement(s));
                break;
            default:
                System.out.println(s.getClass());
                throw new UnknownSyntaxException();
        }
    }

    private static JSNode handleWhileNode(Statement s) throws UnknownSyntaxException {
        WhileNode whileNode = (WhileNode) s;
        JSNode testCondition = handleExpression(whileNode.getTest());
        List<JSNode> loopStatements = new ArrayList<>();
        for (Statement statement : whileNode.getBody().getStatements()) {
            handleStatement(loopStatements, statement);
        }
        BlockNode loopBlock = new BlockNode(loopStatements);
        return new JSWhileNode(testCondition, loopBlock);
    }

    private static JSNode handleExpressionStatement(Statement s) throws UnknownSyntaxException {
        Expression exp = ((ExpressionStatement) s).getExpression();
        if (exp.isAssignment()) {
            BinaryNode binaryExp = (BinaryNode) exp;
            //expression vprvo
            JSNode builtExpression = handleExpression(binaryExp.rhs());
            if (binaryExp.lhs().getClass().getName().equals("jdk.nashorn.internal.ir.IdentNode")) {
                //normal assignement
                return assignVarNode((IdentNode) binaryExp.lhs(), binaryExp.rhs(), builtExpression);
            } else {
                //array assignement
                IndexNode indexNode = (IndexNode) binaryExp.lhs();
                Expression indexExpression = indexNode.getIndex();
                IdentNode identNode = (IdentNode) indexNode.getBase();
                return assignToArray(identNode, indexExpression, builtExpression);
            }
        } else if (exp.getClass().getName().equals("jdk.nashorn.internal.ir.CallNode")){
            try {
                CallNode callNode = (CallNode) exp;
                AccessNode accessNode = (AccessNode) callNode.getFunction();
                IdentNode base = (IdentNode) accessNode.getBase();
                if (base.getName().equals("console") && accessNode.getProperty().equals("log")) {
                    List<Expression> args = callNode.getArgs();
                    JSNode loggedNode = handleExpression(args.get(0));
                    return new ConsoleLogNode(loggedNode, out);
                }
            } catch (Exception e) {
                System.out.println(exp.getClass());
                throw new UnknownSyntaxException();
            }
        }
        System.out.println(s.getClass());
        throw new UnknownSyntaxException();

    }

    private static JSNode handleIfNode(Statement s) throws UnknownSyntaxException {
        IfNode ifNode = (IfNode) s;
        JSNode testCondition = handleExpression(ifNode.getTest());
        List<JSNode> passStatements = new ArrayList<>();
        List<JSNode> failStatements = new ArrayList<>();
        for (Statement statement : ifNode.getPass().getStatements()) {
            handleStatement(passStatements, statement);
        }
        for (Statement statement : ifNode.getFail().getStatements()) {
            handleStatement(failStatements, statement);
        }
        BlockNode passBlock = new BlockNode(passStatements);
        BlockNode failBlock = new BlockNode(failStatements);
        return new JSIfNode(testCondition, passBlock, failBlock);
    }

    private static JSNode handleVarNode(Statement s) throws UnknownSyntaxException {
        VarNode var = (VarNode) s;
        IdentNode assignmentDest = var.getAssignmentDest();
        Expression assignmentSource = var.getAssignmentSource();

        //musime sestavit expression vpravo
        JSNode builtExpression = handleExpression(assignmentSource);
        JSNode varNode = assignVarNode(assignmentDest, assignmentSource, builtExpression);
        return varNode;
    }

    private static JSNode assignVarNode(IdentNode assignmentDest, Expression assignmentSource, JSNode builtExpression) throws UnknownSyntaxException {
        return JSVarNodeGen.create(builtExpression, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
    }

    private static JSNode assignToArray(IdentNode arrayName, Expression index, JSNode builtExpression) throws UnknownSyntaxException {
        Expression exp = arrayName;
        JSNode symbol = handleExpression(exp);
        JSNode indexNode = handleExpression(index);
        return new AssignToArrayNode(symbol, indexNode, builtExpression);
    }

    private static JSNode readFromArray(IdentNode arrayName, Expression index) throws UnknownSyntaxException {
        Expression exp = arrayName;
        JSNode symbol = handleExpression(exp);
        JSNode indexNode = handleExpression(index);
        return new ReadFromArrayNode(symbol, indexNode);
    }

    private static JSNode handleExpression(Expression exp) throws UnknownSyntaxException {
        switch (exp.getClass().getName()) {
            case "jdk.nashorn.internal.ir.LiteralNode$NumberLiteralNode":
                LiteralNumberNode literalNumberNode = getLiteralNumberNode(exp);
                return literalNumberNode;
            case "jdk.nashorn.internal.ir.LiteralNode$StringLiteralNode":
                LiteralStringNode literalStringNode = getLiteralStringNode(exp);
                return literalStringNode;
            case "jdk.nashorn.internal.ir.LiteralNode$NullLiteralNode":
                JSNull jsNull = new JSNull();
                return jsNull;
            case "jdk.nashorn.internal.ir.LiteralNode$BooleanLiteralNode":
                LiteralBooleanNode literalBooleanNode = getLiteralBooleanNode(exp);
                return literalBooleanNode;
            case "jdk.nashorn.internal.ir.IdentNode":
                IdentNode identNode = (IdentNode) exp;
                FrameSlot frameSlot = frameDescriptors.peek().findFrameSlot(identNode.getName());
                if (frameSlot != null) {
                    SymbolNode symbol = new SymbolNode(frameSlot);
                    return symbol;
                } else {
                    //zadny frameSlot nenalezen = promenna neexistuje v danem scope
                    JSUndefined jsUndefined = new JSUndefined();
                    return jsUndefined;
                }

            case "jdk.nashorn.internal.ir.BinaryNode":
                JSBinaryNode binaryNode = getBinaryNode(exp);
                return binaryNode;
            case "jdk.nashorn.internal.ir.JoinPredecessorExpression":
                JoinPredecessorExpression jpe = (JoinPredecessorExpression) exp;
                return getBinaryNode(jpe.getExpression());
            case "jdk.nashorn.internal.ir.LiteralNode$ArrayLiteralNode":
                //create new empty array
                return new NewArrayNode();
            case "jdk.nashorn.internal.ir.IndexNode":
                IndexNode indexNode = (IndexNode) exp;
                Expression indexExpression = indexNode.getIndex();
                IdentNode indexBase = (IdentNode) indexNode.getBase();
                return readFromArray(indexBase, indexExpression);
            default:
                System.out.println(exp.getClass().getName());
                throw new UnknownSyntaxException();
        }
    }

    private static JSBinaryNode getBinaryNode(Expression exp) throws UnknownSyntaxException {
        BinaryNode node = (BinaryNode) exp;
        JSNode lhs = handleExpression(node.lhs());
        JSNode rhs = handleExpression(node.rhs());
        OpCode token;

        switch (node.tokenType().getName()) {
            case "+":
                token = OpCode.PLUS;
                break;
            case ">":
                token = OpCode.GT;
                break;
            case "-":
                token = OpCode.MINUS;
                break;
            case "*":
                token = OpCode.TIMES;
                break;
            case "/":
                token = OpCode.DIV;
                break;
            case "<":
                token = OpCode.LT;
                break;
            case "||":
                token = OpCode.OR;
                break;
            case "&&":
                token = OpCode.AND;
                break;
            case "==":
                token = OpCode.EQ;
                break;
            default:
                System.out.println(node.tokenType());
                throw new UnknownSyntaxException();
        }

        JSBinaryNode binaryNode = new JSBinaryNode(lhs, rhs, token);
        return binaryNode;
    }

    private static LiteralStringNode getLiteralStringNode(Expression assignmentSource) {
        return new LiteralStringNode(new JSString(assignmentSource.toString()));
    }

    private static LiteralBooleanNode getLiteralBooleanNode(Expression assignmentSource) {
        return new LiteralBooleanNode(new JSBoolean(Boolean.parseBoolean(assignmentSource.toString())));
    }

    private static LiteralNumberNode getLiteralNumberNode(final Expression assignmentSource) {
        return new LiteralNumberNode(new JSNumber(new Number() {
            @Override
            public int intValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public long longValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public float floatValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public double doubleValue() {
                return Integer.parseInt(assignmentSource.toString());
            }

            @Override
            public String toString() {
                return "" + this.intValue();
            }
        }));
    }
}
