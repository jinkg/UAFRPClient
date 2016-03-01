package com.yalin.uafrpclient.net.response;

import com.google.gson.Gson;
import com.yalin.uafmessage.msg.DeregistrationRequest;

import java.util.List;

/**
 * Created by YaLin on 2016/1/14.
 */
public class DeregResponse extends BaseResponse {
    public List<DeregistrationRequest> data;

    public String toJson() {
        return new Gson().toJson(data);
    }
}
