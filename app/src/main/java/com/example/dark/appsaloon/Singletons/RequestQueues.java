package com.example.dark.appsaloon.Singletons;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;



public class RequestQueues {
    int socketTimeout = 30000;
    private static RequestQueues requestQueues;
    private RequestQueue requestQueue;
    private static Context mctx;

    private RequestQueues(Context ctx) {
        this.mctx = ctx;
        this.requestQueue=getRequestQueue();
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized RequestQueues getInstance(Context context){

        if(requestQueues==null)
        {
            requestQueues = new RequestQueues(context);
        }
        return requestQueues;

    }
    public<T> void addToRequestQue(Request<T> request){
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);
    }

}
