package com.delvebyte.etisalatapp.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.delvebyte.etisalatapp.activity.MyApplication;

public class VolleySingleton {
    private static  VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getsAppContext());
    }

    public static VolleySingleton getInstance() {
        if (sInstance == null){
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
