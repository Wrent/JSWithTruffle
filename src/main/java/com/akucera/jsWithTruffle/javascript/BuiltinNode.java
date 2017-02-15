package com.akucera.jsWithTruffle.javascript;

import com.oracle.truffle.api.dsl.NodeChild;

/**
 * Used for built in function console.log.
 */
@NodeChild(value = "arguments", type = JSNode[].class)
public abstract class BuiltinNode extends JSNode {
}
