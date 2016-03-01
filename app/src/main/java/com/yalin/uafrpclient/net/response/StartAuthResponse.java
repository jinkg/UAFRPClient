package com.yalin.uafrpclient.net.response;

import com.google.gson.Gson;
import com.yalin.uafmessage.msg.AuthenticationRequest;

import java.util.List;

/**
 * Created by YaLin on 2016/1/12.
 */
public class StartAuthResponse extends BaseResponse {
    public List<AuthenticationRequest> data;

    public String toJson() {
        return new Gson().toJson(data);
    }
}
