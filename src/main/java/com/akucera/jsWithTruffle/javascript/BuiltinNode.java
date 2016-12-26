package com.akucera.jsWithTruffle.javascript;

import com.oracle.truffle.api.dsl.NodeChild;

/**
 * Created by akucera on 25.12.16.
 */
@NodeChild(value = "arguments", type = JSNode[].class)
public abstract class BuiltinNode extends JSNode {
}
