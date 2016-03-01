package com.yalin.uafrpclient.net.response;

import com.google.gson.Gson;
import com.yalin.uafrpclient.data.AuthenticatorRecord;

import java.util.List;

/**
 * Created by YaLin on 2016/1/12.
 */
public class FinishAuthResponse extends BaseResponse {
    public List<AuthenticatorRecord> data;

    public String toJson() {
        return new Gson().toJson(data);
    }
}
