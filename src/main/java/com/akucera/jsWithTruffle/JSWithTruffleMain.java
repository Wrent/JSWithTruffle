package com.akucera.jsWithTruffle;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

public class JSWithTruffleMain {


    public static void main(String[] args) throws IOException {

        JS[] impls = new JS[]{new JSImpl()};
        String arg = args[0];
        if (arg.equals("-benchmark")) {
            JSBenchmark.benchmark(impls);
            return;
        }

        int impl = 0;

        int iterations = 1;
        if (args.length > 1) {
            iterations = Integer.parseInt(args[1]);
        }
        JS js = impls[impl];

        System.out.println("Running with " + js.getClass().getSimpleName() + " for " + iterations+ " iterations.");

        js.prepare(parseToStatements(args[0]), System.in, System.out);
        long time = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            js.run();
        }

        System.out.println("Elapsed " + ( System.currentTimeMillis() - time) + "ms");
    }

    public static List<Statement> parseToStatements(String file) throws IOException {
        ClassLoader classLoader = JSWithTruffleMain.class.getClassLoader();
        File f = new File(classLoader.getResource("test/" + file).getFile());
        return parseToStatements(f);
    }

    public static List<Statement> parseToStatements(File f) throws IOException {
        Options options = new Options("nashorn");
        options.set("anon.functions", true);
        options.set("parse.only", true);
        options.set("scripting", true);

        ErrorManager errors = new ErrorManager();
        Context context = new Context(options, errors, Thread.currentThread().getContextClassLoader());

        ClassLoader classLoader = JSWithTruffleMain.class.getClassLoader();
        Source source = Source.sourceFor(f.getName(), f);
        Parser parser = new Parser(context.getEnv(), source, errors);
        FunctionNode functionNode = parser.parse();
        Block block = functionNode.getBody();
        List<Statement> statements = block.getStatements();

        return statements;
    }
}
