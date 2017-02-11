/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akucera.jsWithTruffle;

import jdk.nashorn.internal.ir.Statement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class JSBenchmark {

    public static void benchmark(JS[] impls) throws IOException {
        benchmark("simple.js", impls, "");
    }

    private static final void benchmark(String name, JS[] implementations, String input) throws IOException {
        List<Statement> operations = parse(name);
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes("ASCII"));
        for (JS impl : implementations) {
            impl.prepare(operations, in, System.out);
        }
        System.out.println("Benchmarking " + name);
        for (JS impl : implementations) {
            System.err.printf("%10s : ", impl.getClass().getSimpleName());
            int iterations = warmup(impl, 4000, in);
            double score = run(impl, iterations, 2000, in);
            System.err.printf("%15.2f ops/sec%n", score);
        }
    }

    private static double run(JS bf, int warmupIterations, long timeToRun, ByteArrayInputStream in) throws IOException {
        long startTime = System.currentTimeMillis();
        int iterations = warmupIterations;
        long timeElapsed = 0;
        int totalIterations = 0;
        while (true) {
            for (int i = 0; i < iterations; i++) {
                bf.run();
                in.reset();
            }
            totalIterations += iterations;
            long newTime = System.currentTimeMillis();
            timeElapsed = newTime - startTime;
            if (timeElapsed <= 100) {
                iterations = iterations << 1;
            }
            if (timeElapsed > timeToRun) {
                break;
            }
        }
        double overTimeFactor = (double) (timeElapsed - timeToRun) / (double) timeElapsed;
        double score = totalIterations - overTimeFactor * totalIterations;
        return score;
    }

    private static int warmup(JS bf, long timeToRun, ByteArrayInputStream in) throws IOException {
        long startTime = System.currentTimeMillis();
        int iterations = 1;
        while (true) {
            for (int i = 0; i < iterations; i++) {
                bf.run();
                in.reset();
            }
            long newTime = System.currentTimeMillis();
            long timeEleapsed = newTime - startTime;
            if (timeEleapsed <= 100) {
                iterations = iterations << 1;
            }
            if (timeEleapsed > timeToRun) {
                break;
            }
        }
        return iterations;
    }

    private static List<Statement> parse(String file) throws IOException {
        return JSWithTruffleMain.parseToStatements(file);
    }

}
