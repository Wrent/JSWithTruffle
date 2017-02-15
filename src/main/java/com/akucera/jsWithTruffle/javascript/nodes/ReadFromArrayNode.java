package com.akucera.jsWithTruffle.javascript.nodes;

import com.akucera.jsWithTruffle.javascript.JSNode;
import com.akucera.jsWithTruffle.javascript.types.JSArray;
import com.akucera.jsWithTruffle.javascript.types.JSNumber;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * Node which performs reading from an array.
 */
public class ReadFromArrayNode extends JSNode {
    private JSNode symbol;
    private JSNode index;

    public ReadFromArrayNode(JSNode symbolNode, JSNode index) {
        this.symbol = symbolNode;
        this.index = index;
    }

    @Override
    public Object execute(VirtualFrame virtualFrame) {
        JSArray<Object> jsArray = (JSArray<Object>) symbol.execute(virtualFrame);
        int i = ((JSNumber) index.execute(virtualFrame)).getInt();
        return jsArray.get(i);
    }

    @Override
    public String toString() {
        return "ReadFromArrayNode{" +
                "symbol=" + symbol +
                ", index=" + index +
                '}';
    }
}

