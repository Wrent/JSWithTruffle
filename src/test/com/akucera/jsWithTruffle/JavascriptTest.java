package com.akucera.jsWithTruffle;

import jdk.nashorn.internal.ir.Statement;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by akucera on 8.2.17.
 */
public class JavascriptTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() throws IOException {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        outContent.flush();
        outContent.reset();
        errContent.flush();
    }

    @After
    public void cleanUpStreams() throws IOException {
        outContent.flush();
        errContent.flush();
        outContent.toString();
        errContent.toString();
        System.setOut(null);
        System.setErr(null);
    }

    @org.junit.Test
    public void testAddOperation() throws Exception {
        initTest("console.log(1 + 2)");
        assertEquals("3", outContent.toString().trim());
    }

    @org.junit.Test
    public void testMinusOperation() throws Exception {
        initTest("console.log(1 - 2)");
        assertEquals("-1", outContent.toString().trim());
    }

    @org.junit.Test
    public void testTimesOperation() throws Exception {
        initTest("console.log(5 * 2)");
        assertEquals("10", outContent.toString().trim());
    }

    @org.junit.Test
    public void testDivOperation() throws Exception {
        initTest("console.log(12 / 2)");
        assertEquals("6", outContent.toString().trim());
    }

    @org.junit.Test
    public void testEqualsOperation() throws Exception {
        initTest("console.log(12 == 2);console.log(12 == 12)");
        assertEquals("false\ntrue", outContent.toString().trim());
    }

    @org.junit.Test
    public void testGTOperation() throws Exception {
        initTest("console.log(12 > 2);console.log(2 > 12)");
        assertEquals("true\nfalse", outContent.toString().trim());
    }

    @org.junit.Test
    public void testLTOperation() throws Exception {
        initTest("console.log(12 < 2);console.log(2 < 12)");
        assertEquals("false\ntrue", outContent.toString().trim());
    }

    @org.junit.Test
    public void testAndOperation() throws Exception {
        initTest("console.log((12 > 1) && (13 > 1));console.log((13 > 1) && (1 > 13))");
        assertEquals("true\nfalse", outContent.toString().trim());
    }

    @org.junit.Test
    public void testOrOperation() throws Exception {
        initTest("console.log((12 > 1) || (13 > 1));console.log((13 > 1) || (1 > 13));console.log((13 < 1) || (1 > 13))");
        assertEquals("true\ntrue\nfalse", outContent.toString().trim());
    }

    @org.junit.Test
    public void testIntVar() throws Exception {
        initTest("var a = 10;\nconsole.log(a + 2);");
        assertEquals("12", outContent.toString().trim());
    }

    @org.junit.Test
    public void testStringVar() throws Exception {
        initTest("var a = \"hello world\";\nconsole.log(a);");
        assertEquals("\"hello world\"", outContent.toString().trim());
    }

    @org.junit.Test
    public void testBooleanVar() throws Exception {
        initTest("var a = false;\nconsole.log(a);");
        assertEquals("false", outContent.toString().trim());
    }

    @org.junit.Test
    public void testIfBranch() throws Exception {
        initTest("if (2 > 1) {console.log(\"if branch\");} else {console.log(\"else branch\");}");
        assertEquals("\"if branch\"", outContent.toString().trim());
    }

    @org.junit.Test
    public void testElseBranch() throws Exception {
        initTest("if (2 < 1) {console.log(\"if branch\");} else {console.log(\"else branch\");}");
        assertEquals("\"else branch\"", outContent.toString().trim());
    }

    @org.junit.Test
    public void testLoop() throws Exception {
        initTest("var a = 0; while (a < 5) {console.log(a);a = a + 1;}");
        assertEquals("0\n1\n2\n3\n4", outContent.toString().trim());
    }

    @org.junit.Test
    public void testFailLoop() throws Exception {
        initTest("var a = 6; while (a < 5) {console.log(a);a = a + 1;}");
        assertEquals("", outContent.toString().trim());
    }


    @org.junit.Test
    public void testArray() throws Exception {
        initTest("var a = 0; var array = []; while (a < 5) {array[a] = a; a = a + 1;} a = 0;" +
                "while (a < 5) {console.log(array[a]);a = a + 1;}");
        assertEquals("0\n1\n2\n3\n4", outContent.toString().trim());
    }

    @org.junit.Test
    public void testNull() throws Exception {
        initTest("var a = null; console.log(a);");
        assertEquals("null", outContent.toString().trim());
    }

    @org.junit.Test
    public void testUndefined() throws Exception {
        initTest("var a = undefined; console.log(a);");
        assertEquals("undefined", outContent.toString().trim());
    }

    @org.junit.Test
    public void testNotDefined() throws Exception {
        initTest("console.log(a);");
        assertEquals("undefined", outContent.toString().trim());
    }

    @org.junit.Test
    public void testFibonaci() throws Exception {
        JS impl = new JSImpl();
        List<Statement> statements = JSWithTruffleMain.parseToStatements("fibonaci.js");
        impl.prepare(statements, System.in, System.out);
        impl.run();
        assertEquals("1\n" +
                "2\n" +
                "3\n" +
                "5\n" +
                "8\n" +
                "13\n" +
                "21\n" +
                "34\n" +
                "55\n" +
                "89\n" +
                "144\n" +
                "233\n" +
                "377\n" +
                "610\n" +
                "987\n" +
                "1597\n" +
                "2584\n" +
                "4181\n" +
                "6765\n" +
                "10946\n" +
                "17711\n" +
                "28657\n" +
                "46368\n" +
                "75025\n" +
                "121393\n" +
                "196418\n" +
                "317811\n" +
                "514229", outContent.toString().trim());
    }

    @org.junit.Test
    public void testBubbleSort() throws Exception {
        JS impl = new JSImpl();
        List<Statement> statements = JSWithTruffleMain.parseToStatements("bubbleSort.js");
        impl.prepare(statements, System.in, System.out);
        impl.run();
        assertEquals("JSArray{list=[3, 9, 34, 198, 200, 203, 746, 764, 984]}", outContent.toString().trim());
    }

    private void initTest(String content) throws IOException {
        File file = new File(Math.random()*100000 + ".js");
        String testJSString = content;
        writeToFile(file, testJSString);
        JS impl = new JSImpl();
        List<Statement> statements = JSWithTruffleMain.parseToStatements(file);
        impl.prepare(statements, System.in, System.out);
        impl.run();
    }

    private void writeToFile(File file, String testJSString) throws FileNotFoundException {
        PrintWriter wri = new PrintWriter(file);
        wri.print("");
        wri.close();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(testJSString);

        } catch (IOException e) {
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
            }
        }
    }

}