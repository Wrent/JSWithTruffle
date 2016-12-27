package com.akucera.jsWithTruffle;

import com.akucera.jsWithTruffle.exceptions.UnknownSyntaxException;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralBooleanNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralNumberNode;
import com.akucera.jsWithTruffle.javascript.literals.LiteralStringNode;
import com.akucera.jsWithTruffle.javascript.nodes.*;
import com.akucera.jsWithTruffle.javascript.nodes.NumberNodeGen;
import com.akucera.jsWithTruffle.javascript.nodes.JSVarNodeGen;
import com.akucera.jsWithTruffle.javascript.nodes.StringNodeGen;
import com.akucera.jsWithTruffle.javascript.nodes.BooleanNodeGen;
import com.akucera.jsWithTruffle.javascript.types.*;
import com.oracle.truffle.api.frame.FrameDescriptor;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.VarNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by akucera on 26.12.16.
 */
public class StatementsHandler {
    public static final Stack<FrameDescriptor> frameDescriptors = new Stack<>();
    static {
        frameDescriptors.add(new FrameDescriptor());
    }

    public static List<JSNode> handle(List<Statement> statements) throws UnknownSyntaxException {
        ArrayList<JSNode> list = new ArrayList<>();

        for (Statement s : statements) {
            switch (s.getClass().toString()) {
                case "class jdk.nashorn.internal.ir.VarNode":
                    list.add(handleVarNode(s));
                    break;
                case "class jdk.nashorn.internal.ir.IfNode":
                    list.add(handleIfNode(s));
                    break;
                case "class jdk.nashorn.internal.ir.WhileNode":
                    list.add(handleWhileNode(s));
                    break;
                case "class jdk.nashorn.internal.ir.ExpressionStatement":
                    list.add(handleExpressionStatement(s));
                    break;
                default:
                    System.out.println(s.getClass());
                    throw new UnknownSyntaxException();
            }
        }
        System.out.println(list);
        return list;
    }

    private static JSNode handleWhileNode(Statement s) {
        return null;
    }

    private static JSNode handleIfNode(Statement s) {
        return null;
    }

    private static JSNode handleVarNode(Statement s) throws UnknownSyntaxException {
        VarNode var = (VarNode) s;
        IdentNode assignmentDest = var.getAssignmentDest();
        Expression assignmentSource = var.getAssignmentSource();

        //podle toho jaky typ ma promenna vytvorime JSnodes
        switch (assignmentSource.getType().toString()) {
            case "int":
                LiteralNumberNode literalNumberNode = getLiteralNumberNode(assignmentSource);
                JSVarNode numberVar = JSVarNodeGen.create(literalNumberNode, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return numberVar;
            case "boolean":
                LiteralBooleanNode literalBooleanNode = getLiteralBooleanNode(assignmentSource);
                JSVarNode booleanVar = JSVarNodeGen.create(literalBooleanNode, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return booleanVar;
            case "object<type=String>":
                LiteralStringNode literalStringNode = getLiteralStringNode(assignmentSource);
                JSVarNode stringVar = JSVarNodeGen.create(literalStringNode, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return stringVar;
            case "object":
                JSNull jsNull = new JSNull();
                JSVarNode nullVar = JSVarNodeGen.create(jsNull, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return nullVar;
            case "object<type=Undefined>":
                JSUndefined jsUndefined = new JSUndefined();
                JSVarNode undefinedVar = JSVarNodeGen.create(jsUndefined, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return undefinedVar;
            default:
                throw new UnknownSyntaxException();
        }
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

    private static JSNode handleExpressionStatement(Statement s) {
        return null;
    }
}
