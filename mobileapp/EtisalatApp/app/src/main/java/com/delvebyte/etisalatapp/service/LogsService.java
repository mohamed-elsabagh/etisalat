package com.delvebyte.etisalatapp.service;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.delvebyte.etisalatapp.model.LogModel;
import com.delvebyte.etisalatapp.network.NetworkResponseRequest;
import com.delvebyte.etisalatapp.network.VolleySingleton;
import com.delvebyte.etisalatapp.utils.ConstantsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogsService {
    private RequestQueue requestQueue;
    private ArrayList<INetworkService> listeners = new ArrayList<>();
    private final static String ALL_REQUESTS_TAG = "LOGS";

    public LogsService() {
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    public void getAllLogs(final int requestNumber) {
        NetworkResponseRequest mBasicRequestTest = new NetworkResponseRequest(Request.Method.GET, ConstantsUtils.API_NODE_URL + "/api/log",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // This is status code: response.statusCode
                        // This is string response: NetworkResponseRequest.parseToString(response)
                        try {
                            String response_string = NetworkResponseRequest.parseToString(response);
                            JSONArray reader = new JSONArray(response_string);
                            responseSuccess(reader, requestNumber);
                        } catch (JSONException e) {
                            responseFailure(requestNumber);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error instanceof NetworkError) {
                    responseNetworkError(requestNumber);
                } else if( error instanceof ServerError) {
                    responseServerError(requestNumber);
                } else if( error instanceof AuthFailureError) {
                    responseAuthFailureError(requestNumber);
                } else if( error instanceof ParseError) {
                    responseParseError(requestNumber);
                } else if( error instanceof TimeoutError) {
                    responseTimeoutError(requestNumber);
                } else {
                    responseFailure(requestNumber);
                }
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Cache-Control", "no-cache");
                return headers;
            }
        };
        // Maybe use this in future upgrade to trace requests with request code
        mBasicRequestTest.setTag(ALL_REQUESTS_TAG);
        requestQueue.add(mBasicRequestTest);
    }

    public void pushLog(final int requestNumber, final LogModel log) {
        NetworkResponseRequest mBasicRequestTest = new NetworkResponseRequest(Request.Method.POST, ConstantsUtils.API_NODE_URL + "/api/log",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // This is status code: response.statusCode
                        // This is string response: NetworkResponseRequest.parseToString(response)
                        try {
                            String response_string = NetworkResponseRequest.parseToString(response);
                            JSONObject reader = new JSONObject(response_string);
                            responseSuccess(reader, requestNumber);
                        } catch (JSONException e) {
                            responseFailure(requestNumber);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error instanceof NetworkError) {
                    responseNetworkError(requestNumber);
                } else if( error instanceof ServerError) {
                    responseServerError(requestNumber);
                } else if( error instanceof AuthFailureError) {
                    responseAuthFailureError(requestNumber);
                } else if( error instanceof ParseError) {
                    responseParseError(requestNumber);
                } else if( error instanceof TimeoutError) {
                    responseTimeoutError(requestNumber);
                } else {
                    responseFailure(requestNumber);
                }
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Cache-Control", "no-cache");
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", log.getName());
                params.put("status", "" + log.getStatus());
                return params;
            }
        };
        // Maybe use this in future upgrade to trace requests with request code
        mBasicRequestTest.setTag(ALL_REQUESTS_TAG);
        requestQueue.add(mBasicRequestTest);
    }

    public void addListener(INetworkService listener) {
        listeners.add(listener);
    }

    public void removeListener(INetworkService listener) {
        listeners.remove(listener);
    }

    private void responseSuccess(JSONObject response, int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseSuccess(response, requestNumber);
        }
    }

    private void responseSuccess(JSONArray response, int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseSuccess(response, requestNumber);
        }
    }

    private void responseFailure(JSONObject failure, int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseFailure(failure, requestNumber);
        }
    }

    private void responseFailure(int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseFailure(requestNumber);
        }
    }

    private void responseTimeoutError(int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseTimeoutError(requestNumber);
        }
    }

    private void responseNetworkError(int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseNetworkError(requestNumber);
        }
    }

    private void responseServerError(int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseServerError(requestNumber);
        }
    }

    private void responseParseError(int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.responseParseError(requestNumber);
        }
    }

    private void responseAuthFailureError(int requestNumber) {
        for (INetworkService listener : listeners) {
            listener.AuthFailureError(requestNumber);
        }
    }

    public void cancelAllRequests() {
        requestQueue.cancelAll(ALL_REQUESTS_TAG);
    }
}
