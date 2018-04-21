package com.delvebyte.etisalatapp.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class NetworkResponseRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private Map<String, String> headers;
    private Map<String, String> parameters;

    protected NetworkResponseRequest(int method, String url, Response.Listener<NetworkResponse> listener,
                                     Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public NetworkResponseRequest(String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        this(Request.Method.GET, url, listener, errorListener);
    }

    public NetworkResponseRequest(int method, String url, Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener, Map<String, String> headers,
                                  Map<String, String> parameters) {
        this(method, url, listener, errorListener);
        this.headers = headers;
        this.parameters = parameters;
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    public static String parseToString(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return parsed;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return (parameters != null) ? parameters : super.getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (headers != null) ? headers : super.getHeaders();
    }

    /*
    * All Instances of errors if you require to compare against
    *
    * if( error instanceof NetworkError) {
            } else if( error instanceof ClientError) {
            } else if( error instanceof ServerError) {
            } else if( error instanceof AuthFailureError) {
            } else if( error instanceof ParseError) {
            } else if( error instanceof NoConnectionError) {
            } else if( error instanceof TimeoutError) {
            }
    * */
}
