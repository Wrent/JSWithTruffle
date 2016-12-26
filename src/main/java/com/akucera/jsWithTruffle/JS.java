package com.akucera.jsWithTruffle;

import com.akucera.jsWithTruffle.exceptions.UnknownSyntaxException;
import jdk.nashorn.internal.ir.Statement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by akucera on 25.12.16.
 */
public class JS extends JSImpl {
    @Override
    public void prepare(List<Statement> statements, InputStream in, OutputStream out) {
        try {
            StatementsHandler.handle(statements);
        } catch (UnknownSyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() throws IOException {

    }
}
