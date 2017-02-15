/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akucera.jsWithTruffle;

import jdk.nashorn.internal.ir.Statement;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Abstract class for any JS implementation.
 */
public abstract class JS {

    public abstract void prepare(List<Statement> statements, InputStream in, PrintStream out);

    public abstract void run() throws IOException;
    
}
