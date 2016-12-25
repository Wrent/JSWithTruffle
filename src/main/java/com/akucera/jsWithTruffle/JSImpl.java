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
import java.util.List;

public abstract class JSImpl {

    public abstract void prepare(List<Statement> statements, InputStream in, OutputStream out);

    public abstract void run() throws IOException;
    
}
