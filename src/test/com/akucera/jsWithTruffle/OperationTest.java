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
public class OperationTest {

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