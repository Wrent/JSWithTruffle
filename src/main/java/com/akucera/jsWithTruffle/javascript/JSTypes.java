package com.akucera.jsWithTruffle.javascript;

import com.akucera.jsWithTruffle.javascript.types.*;
import com.oracle.truffle.api.dsl.TypeSystem;

/**
 * This file specifies types in our implementation.
 */
@TypeSystem({JSBoolean.class, JSNumber.class, JSString.class, JSNull.class, JSUndefined.class, JSFunction.class, JSArray.class})
public class JSTypes {
}
