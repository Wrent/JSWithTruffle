package com.akucera.jsWithTruffle.javascript.types;


import java.util.ArrayList;

/**
 * Created by akucera on 25.12.16.
 */
public class JSArray<T extends Object>  {
    private ArrayList<T> list;

    public JSArray() {
        this.list = new ArrayList<T>();
    }

    public boolean add(T e) {
        return this.list.add(e);
    }

    public void add(int i, T e) {
        this.list.add(i, e);
    }

    public T get(int i) {
        return this.list.get(i);
    }

    public int length() {
        return this.list.size();
    }

    @Override
    public String toString() {
        return "JSArray{" +
                "list=" + list +
                '}';
    }
}
