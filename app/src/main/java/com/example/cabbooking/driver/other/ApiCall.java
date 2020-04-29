package com.example.cabbooking.driver.other;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cabbooking.driver.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MG on 04-03-2018.
 */

public class ApiCall {
    private static ApiCall mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public ApiCall(Context ctx) {
        mCtx = ctx;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApiCall getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiCall(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String url, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
    public static void makePost(Context ctx, String url, JSONObject parameters, Response.Listener<JSONObject>
            listener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        ApiCall.getInstance(ctx).addToRequestQueue(request);
    }


    public static void makeGET(Context ctx, String url, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        System.out.println("api_get_call-----------"+url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.replaceAll(" ", "%20"),
                listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
    }
    public static void makePostData(final Context ctx, String url, final JSONObject parameters, Response.Listener<JSONObject>
            listener, Response.ErrorListener errorListener) {
        System.out.println("api_get_call-----------" + url);
        System.out.println("api_get_call-----------" + parameters);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,parameters, listener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", "key="+ctx.getString(R.string.noti_key));
                return headers;
            }
        };
        ApiCall.getInstance(ctx).addToRequestQueue(request);
    }

}
