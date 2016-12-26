package com.akucera.jsWithTruffle;

import com.akucera.jsWithTruffle.exceptions.UnknownSyntaxException;
import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.nodes.*;
import com.akucera.jsWithTruffle.javascript.nodes.NumberNodeGen;
import com.akucera.jsWithTruffle.javascript.nodes.JSVarNodeGen;
import com.akucera.jsWithTruffle.javascript.nodes.StringNodeGen;
import com.akucera.jsWithTruffle.javascript.nodes.BooleanNodeGen;
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
        return list;
    }

    private static JSNode handleWhileNode(Statement s) {
        return null;
    }

    private static JSNode handleIfNode(Statement s) {
        return null;
    }

    private static JSNode handleVarNode(Statement s) {
        VarNode var = (VarNode) s;
        IdentNode assignmentDest = var.getAssignmentDest();
        Expression assignmentSource = var.getAssignmentSource();

        //podle toho jaky typ ma promenna vytvorime JSnodes
        switch (assignmentSource.getType().toString()) {
            case "int":
                NumberNode numberNode = NumberNodeGen.create(frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                JSVarNode numberVar = JSVarNodeGen.create(numberNode, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return numberVar;
            case "boolean":
                BooleanNode booleanNode = BooleanNodeGen.create(frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                JSVarNode booleanVar = JSVarNodeGen.create(booleanNode, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return booleanVar;
            case "object<type=String>":
                StringNode stringNode = StringNodeGen.create(frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                JSVarNode stringVar = JSVarNodeGen.create(stringNode, frameDescriptors.peek().findOrAddFrameSlot(assignmentDest.getName()));
                return stringVar;
            case "object":
                System.out.println("got null");
                break;
            case "object<type=Undefined>":
                System.out.println("got undefined");
                break;
            default:

                break;
        }

        System.out.println(assignmentDest.getName());
        System.out.println(assignmentSource.getType().getInternalName());

        return null;
    }

    private static JSNode handleExpressionStatement(Statement s) {
        return null;
    }
}
