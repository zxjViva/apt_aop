package com.zxj.needle_runtime;

import android.app.Activity;

public class ViewBinderInjector {
    public static void bind(Activity activity){
        try {
            //com.zxj.needle.MainActivity_Binder
            Class<?> aClass = Class.forName(activity.getClass().getCanonicalName() + "_Binder");
            AbsViewBinder viewBinder = (AbsViewBinder) aClass.newInstance();
            viewBinder.findViewById(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
