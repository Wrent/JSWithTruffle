package com.akucera.jsWithTruffle.javascript;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;

/**
 * Javascript language specification for Truffle.
 */
@TruffleLanguage.Registration(name = "Javascript", version = "0.1",
        mimeType = JSLanguage.MIME_TYPE)
public class JSLanguage extends TruffleLanguage<Object> {
    public static final String MIME_TYPE = "application/javascript";

    public static final JSLanguage INSTANCE = new JSLanguage();

    private static final boolean TAIL_CALL_OPTIMIZATION_ENABLED = true;

    private JSLanguage() {
    }

    @Override
    protected Object createContext(Env env) {
        return null;
    }

    @Override
    protected CallTarget parse(Source source, Node node, String... strings) throws Exception {
        return null;
    }

    @Override
    protected Object findExportedSymbol(Object o, String s, boolean b) {
        return null;
    }

    @Override
    protected Object getLanguageGlobal(Object o) {
        return null;
    }

    @Override
    protected boolean isObjectOfLanguage(Object o) {
        return false;
    }

    @Override
    protected Object evalInContext(Source source, Node node, MaterializedFrame materializedFrame) throws Exception {
        return null;
    }

}