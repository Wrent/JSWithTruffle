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
public class JSWithTruffleMainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @org.junit.Test
    public void testAddOperation() throws Exception {
        File file = new File("testFile.js");
        String testJSString = "console.log(1 + 2)";
        writeToFile(file, testJSString);
        JS impl = new JSImpl();
        List<Statement> statements = JSWithTruffleMain.parseToStatements(file.getAbsolutePath());
        impl.prepare(statements, System.in, System.out);
        impl.run();
        assertEquals("3", outContent.toString());
    }

    private void writeToFile(File file, String testJSString) {
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