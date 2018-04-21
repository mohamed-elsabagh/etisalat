package com.delvebyte.etisalatapp.service;

import org.json.JSONArray;
import org.json.JSONObject;

public interface INetworkService {
    void responseSuccess(JSONObject response, int requestNumber);

    void responseSuccess(JSONArray response, int requestNumber);

    void responseFailure(JSONObject failure, int requestNumber);

    void responseFailure(int requestNumber);

    void responseTimeoutError(int requestNumber);

    void responseNetworkError(int requestNumber);

    void responseServerError(int requestNumber);

    void responseParseError(int requestNumber);

    void AuthFailureError(int requestNumber);
}
