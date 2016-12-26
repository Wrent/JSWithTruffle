package com.akucera.jsWithTruffle.javascript;

import com.akucera.jsWithTruffle.javascript.types.*;
import com.oracle.truffle.api.dsl.TypeSystem;

/**
 * Created by akucera on 25.12.16.
 */
@TypeSystem({JSBoolean.class, JSNumber.class, JSString.class, JSNull.class, JSUndefined.class})
public class JSTypes {
}
