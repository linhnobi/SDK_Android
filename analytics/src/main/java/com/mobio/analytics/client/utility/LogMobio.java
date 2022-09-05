package com.mobio.analytics.client.utility;

import android.util.Log;

public class LogMobio {
    private final static boolean SHOULD_LOG = true;

    private LogMobio() {
        throw new IllegalStateException("LogMobio class");
    }

    public static void logV(String tag, String content){
        if(SHOULD_LOG) {
            Log.v(tag, content);
        }
    }

    public static void logD(String tag, String content){
        if(SHOULD_LOG) {
            Log.d(tag, content);
        }
    }

    public static void logE(String tag, String content){
        if(SHOULD_LOG) {
            Log.e(tag, content);
        }
    }

    public static void logI(String tag, String content){
        if(SHOULD_LOG) {
            Log.i(tag, content);
        }
    }

    public static void logW(String tag, String content){
        if(SHOULD_LOG) {
            Log.w(tag, content);
        }
    }

    public static void logWTF(String tag, String content){
        if(SHOULD_LOG) {
            Log.wtf(tag, content);
        }
    }

}
