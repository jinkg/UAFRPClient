package com.yalin.uafrpclient.net.response;

import com.google.gson.Gson;
import com.yalin.uafmessage.msg.RegistrationRequest;

import java.util.List;

/**
 * Created by YaLin on 2016/1/11.
 */
public class StartRegResponse extends BaseResponse {
    public List<RegistrationRequest> data;

    public String toJson() {
        return new Gson().toJson(data);
    }
}
