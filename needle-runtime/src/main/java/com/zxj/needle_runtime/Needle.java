package com.zxj.needle_runtime;

public class Needle {
    private static GaTracker mGaTracker;

    public static void initGaTracker(GaTracker gaTracker){
        mGaTracker = gaTracker;
    }

    public static GaTracker getGaTracker(){
        return mGaTracker;
    }
}
