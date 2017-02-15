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
 * Most of the magic happens here, using NASHORN parser to create our JSNode statements.
 */
public class StatementsHandler {
    //descriptor for variables
    public static final Stack<FrameDescriptor> frameDescriptors = new Stack<>();
    public static PrintStream out;
    public static InputStream in;


    static {
        frameDescriptors.add(new FrameDescriptor());
    }

    /**
     * Main running method performing the handling of parsed NASHORN statements.
     * @param statements
     * @param in
     * @param out
     * @return
     * @throws UnknownSyntaxException
     */
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

    /**
     * Basic switch between basic nashorn statements.
     * @param list
     * @param s
     * @throws UnknownSyntaxException
     */
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

    /**
     * Function to process a while loop.
     * @param s
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode handleWhileNode(Statement s) throws UnknownSyntaxException {
        WhileNode whileNode = (WhileNode) s;
        JSNode testCondition = handleExpression(whileNode.getTest());
        List<JSNode> loopStatements = new ArrayList<>();
        //handle statements inside the loop
        for (Statement statement : whileNode.getBody().getStatements()) {
            handleStatement(loopStatements, statement);
        }
        BlockNode loopBlock = new BlockNode(loopStatements);
        return new JSWhileNode(testCondition, loopBlock);
    }

    /**
     * Expression statement is usually a type of an assignment or call to a function (we dont have much functions though)
     * @param s
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode handleExpressionStatement(Statement s) throws UnknownSyntaxException {
        Expression exp = ((ExpressionStatement) s).getExpression();
        //it is an assignment
        if (exp.isAssignment()) {
            BinaryNode binaryExp = (BinaryNode) exp;
            //expression on the right
            JSNode builtExpression = handleExpression(binaryExp.rhs());
            if (binaryExp.lhs().getClass().getName().equals("jdk.nashorn.internal.ir.IdentNode")) {
                //normal assignment
                return assignVarNode((IdentNode) binaryExp.lhs(), binaryExp.rhs(), builtExpression);
            } else {
                //array assignment
                IndexNode indexNode = (IndexNode) binaryExp.lhs();
                Expression indexExpression = indexNode.getIndex();
                IdentNode identNode = (IdentNode) indexNode.getBase();
                return assignToArray(identNode, indexExpression, builtExpression);
            }
        }
        //function call
        else if (exp.getClass().getName().equals("jdk.nashorn.internal.ir.CallNode")){
            try {
                CallNode callNode = (CallNode) exp;
                AccessNode accessNode = (AccessNode) callNode.getFunction();
                IdentNode base = (IdentNode) accessNode.getBase();
                //our only known function call is console.log
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

    /**
     * Function for if branches
     * @param s
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode handleIfNode(Statement s) throws UnknownSyntaxException {
        IfNode ifNode = (IfNode) s;
        JSNode testCondition = handleExpression(ifNode.getTest());
        List<JSNode> passStatements = new ArrayList<>();
        List<JSNode> failStatements = new ArrayList<>();
        //if branch
        for (Statement statement : ifNode.getPass().getStatements()) {
            handleStatement(passStatements, statement);
        }
        //else branch (if exists)
        if (ifNode.getFail() != null) {
            for (Statement statement : ifNode.getFail().getStatements()) {
                handleStatement(failStatements, statement);
            }
        }
        BlockNode passBlock = new BlockNode(passStatements);
        BlockNode failBlock = new BlockNode(failStatements);
        return new JSIfNode(testCondition, passBlock, failBlock);
    }

    /**
     * Var node creates a new variable.
     * @param s
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode handleVarNode(Statement s) throws UnknownSyntaxException {
        VarNode var = (VarNode) s;
        IdentNode assignmentDest = var.getAssignmentDest();
        Expression assignmentSource = var.getAssignmentSource();

        //musime sestavit expression vpravo
        JSNode builtExpression = handleExpression(assignmentSource);
        JSNode varNode = assignVarNode(assignmentDest, assignmentSource, builtExpression);
        return varNode;
    }

    /**
     * Helper function to create the new JSNode of new variable.
     * @param assignmentDest
     * @param assignmentSource
     * @param builtExpression
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode assignVarNode(IdentNode assignmentDest, Expression assignmentSource, JSNode builtExpression) throws UnknownSyntaxException {
        return JSVarNodeGen.create(builtExpression, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
    }

    /**
     * Creates new AssignToArrayNode.
     * @param arrayName
     * @param index
     * @param builtExpression
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode assignToArray(IdentNode arrayName, Expression index, JSNode builtExpression) throws UnknownSyntaxException {
        Expression exp = arrayName;
        JSNode symbol = handleExpression(exp);
        JSNode indexNode = handleExpression(index);
        return new AssignToArrayNode(symbol, indexNode, builtExpression);
    }

    /**
     * Creates new ReadFromArrayNode.
     * @param arrayName
     * @param index
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode readFromArray(IdentNode arrayName, Expression index) throws UnknownSyntaxException {
        Expression exp = arrayName;
        JSNode symbol = handleExpression(exp);
        JSNode indexNode = handleExpression(index);
        return new ReadFromArrayNode(symbol, indexNode);
    }

    /**
     * Another switch function for expressions, there is quite a lot of different types of them.
     * @param exp
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSNode handleExpression(Expression exp) throws UnknownSyntaxException {
        switch (exp.getClass().getName()) {
            case "jdk.nashorn.internal.ir.LiteralNode$NumberLiteralNode":
                //just number
                LiteralNumberNode literalNumberNode = getLiteralNumberNode(exp);
                return literalNumberNode;
            case "jdk.nashorn.internal.ir.LiteralNode$StringLiteralNode":
                //just string
                LiteralStringNode literalStringNode = getLiteralStringNode(exp);
                return literalStringNode;
            case "jdk.nashorn.internal.ir.LiteralNode$NullLiteralNode":
                //just null
                JSNull jsNull = new JSNull();
                return jsNull;
            case "jdk.nashorn.internal.ir.LiteralNode$BooleanLiteralNode":
                //just boolean
                LiteralBooleanNode literalBooleanNode = getLiteralBooleanNode(exp);
                return literalBooleanNode;
            case "jdk.nashorn.internal.ir.IdentNode":
                //this is a symbol (reference to a variable)
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
                //binary node (e.g. a + b)
                JSBinaryNode binaryNode = getBinaryNode(exp);
                return binaryNode;
            case "jdk.nashorn.internal.ir.JoinPredecessorExpression":
                //assignment to the same variable (e.g. i = i + 1)
                JoinPredecessorExpression jpe = (JoinPredecessorExpression) exp;
                if (jpe.getExpression().getClass().getName().equals("jdk.nashorn.internal.ir.IdentNode")) {
                    return handleExpression(jpe.getExpression());
                } else {
                    return getBinaryNode(jpe.getExpression());
                }
            case "jdk.nashorn.internal.ir.LiteralNode$ArrayLiteralNode":
                //create new empty array
                return new NewArrayNode();
            case "jdk.nashorn.internal.ir.IndexNode":
                //reading from an array
                IndexNode indexNode = (IndexNode) exp;
                Expression indexExpression = indexNode.getIndex();
                IdentNode indexBase = (IdentNode) indexNode.getBase();
                return readFromArray(indexBase, indexExpression);
            default:
                System.out.println(exp.getClass().getName());
                throw new UnknownSyntaxException();
        }
    }

    /**
     * Handles a binary node (performs handling on both sides of the expression)
     * @param exp
     * @return
     * @throws UnknownSyntaxException
     */
    private static JSBinaryNode getBinaryNode(Expression exp) throws UnknownSyntaxException {
        BinaryNode node = (BinaryNode) exp;
        JSNode lhs = handleExpression(node.lhs());
        JSNode rhs = handleExpression(node.rhs());
        OpCode token;

        //what kind of binary node it is?
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

    /**
     * Handles simple string.
     * @param assignmentSource
     * @return
     */
    private static LiteralStringNode getLiteralStringNode(Expression assignmentSource) {
        return new LiteralStringNode(new JSString(assignmentSource.toString()));
    }

    /**
     * Handles simple bool.
     * @param assignmentSource
     * @return
     */
    private static LiteralBooleanNode getLiteralBooleanNode(Expression assignmentSource) {
        return new LiteralBooleanNode(new JSBoolean(Boolean.parseBoolean(assignmentSource.toString())));
    }

    /**
     * Handles simple number.
     * @param assignmentSource
     * @return
     */
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
