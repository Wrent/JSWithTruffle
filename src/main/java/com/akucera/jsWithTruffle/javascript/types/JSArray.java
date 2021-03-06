package com.akucera.jsWithTruffle.javascript.types;


import java.util.ArrayList;

/**
 * Array type.
 */
public class JSArray<T extends Object>  {
    private ArrayList<T> list;

    public JSArray() {
        this.list = new ArrayList<T>();
    }

    public boolean add(T e) {
        return this.list.add(e);
    }

    /**
     * We have to be careful, if there is already something on index i.
     * @param i
     * @param e
     */
    public void add(int i, T e) {
        try {
            this.list.set(i, e);
        } catch (IndexOutOfBoundsException exp) {
            this.list.add(i, e);
        }
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
