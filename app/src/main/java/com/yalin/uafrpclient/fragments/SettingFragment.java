package com.yalin.uafrpclient.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.yalin.uafmessage.api.UAFClientApi;
import com.yalin.uafmessage.api.UAFIntent;
import com.yalin.uafmessage.msg.AsmInfo;
import com.yalin.uafmessage.msg.RegistrationResponse;
import com.yalin.uafmessage.msg.client.UAFMessage;
import com.yalin.uafrpclient.R;
import com.yalin.uafrpclient.data.User;
import com.yalin.uafrpclient.net.GetRequest;
import com.yalin.uafrpclient.net.NetService;
import com.yalin.uafrpclient.net.PostRequest;
import com.yalin.uafrpclient.net.RequestQueueHelper;
import com.yalin.uafrpclient.net.response.FinishRegResponse;
import com.yalin.uafrpclient.net.response.StartRegResponse;
import com.yalin.uafrpclient.utils.StatLog;


/**
 * Created by 雅麟 on 2015/3/22.
 */
public class SettingFragment extends BaseLoadingFragment implements View.OnClickListener {
    public interface SettingItemClickCallback {
        void onPersonalInfoClicked();
    }

    public static final String TAG = SettingFragment.class.getSimpleName();
    private static final int REQUEST_REG = 2;

    private LinearLayout llHasLogin;
    private LinearLayout llNotLogin;
    private TextView tvPhone;

    private View rootCoordinator;

    private SettingItemClickCallback settingItemClickCallback;


    public static SettingFragment getInstance(SettingItemClickCallback settingItemClickCallback, View rootCoordinator) {
        SettingFragment fragment = new SettingFragment();
        fragment.settingItemClickCallback = settingItemClickCallback;
        fragment.rootCoordinator = rootCoordinator;
        return fragment;
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(view);
        initData();

        return view;
    }

    @Override
    protected String getRequestTag() {
        return SettingFragment.class.getName();
    }

    private void initView(View view) {
        llHasLogin = (LinearLayout) view.findViewById(R.id.setting_ll_has_login);
        llNotLogin = (LinearLayout) view.findViewById(R.id.setting_ll_not_login);
        tvPhone = (TextView) view.findViewById(R.id.setting_tv_phone);
        view.findViewById(R.id.setting_rl_personal_info).setOnClickListener(this);
        view.findViewById(R.id.setting_tv_register_device).setOnClickListener(this);
    }

    private void initData() {
        if (User.isLogin(getActivity().getApplicationContext())) {
            tvPhone.setText(User.getUsername(getActivity().getApplicationContext()));
            llHasLogin.setVisibility(View.VISIBLE);
            llNotLogin.setVisibility(View.GONE);
        } else {
            llNotLogin.setVisibility(View.VISIBLE);
            llHasLogin.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_rl_personal_info:
                if (settingItemClickCallback != null) {
                    settingItemClickCallback.onPersonalInfoClicked();
                }
                break;
            case R.id.setting_tv_register_device:
                if (User.isLogin(getActivity().getApplicationContext())) {
                    String username = User.getUsername(getActivity().getApplicationContext());
                    startReg(username);
                } else {
                    Snackbar.make(rootCoordinator, R.string.login_before_option, Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onLoginStateChanged() {
        initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_REG) {
                Bundle bundle = data.getExtras();
                String message = bundle.getString(UAFIntent.MESSAGE_KEY);
                String componentName = bundle.getString(UAFIntent.COMPONENT_NAME_KEY);

                Gson gson = new Gson();
                UAFMessage uafMessage = gson.fromJson(message, UAFMessage.class);
                RegistrationResponse[] registrationResponses = gson.fromJson(uafMessage.uafProtocolMessage, RegistrationResponse[].class);
                Log.d(TAG, "reg ok message : " + message + " componentName : " + componentName);
                finishReg(registrationResponses);
            }
        }
    }

    private void startReg(String username) {
        showLoading();
        RequestQueue requestQueue = RequestQueueHelper.getInstance(getActivity().getApplicationContext());
        GetRequest<StartRegResponse> request = new GetRequest<>(NetService.getStartRegUrl(username, null), StartRegResponse.class,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        startRegSuccess((StartRegResponse) response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestError(error.getMessage());
                    }
                }
        );
        request.setTag(TAG);
        requestQueue.add(request);
    }

    private void finishReg(RegistrationResponse[] registrationResponses) {
        showLoading();
        RequestQueue requestQueue = RequestQueueHelper.getInstance(getActivity().getApplicationContext());
        PostRequest<FinishRegResponse> request = new PostRequest<>(NetService.FINISH_REG_URL, FinishRegResponse.class, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        finishRegSuccess((FinishRegResponse) response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestError(error.getMessage());
                    }
                }
        );
        StatLog.printLog(TAG, "finish reg:" + new Gson().toJson(registrationResponses));
        request.setBody(new Gson().toJson(registrationResponses));
        request.setTag(TAG);
        requestQueue.add(request);
    }

    private void startRegSuccess(StartRegResponse response) {
        dismissLoading();
        UAFClientApi.doOperation(this, REQUEST_REG, response.toJson(), "channel");
    }

    private void finishRegSuccess(FinishRegResponse response) {
        dismissLoading();
        Snackbar.make(rootCoordinator, R.string.reg_success, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void requestError(String msg) {
        dismissLoading();
        Snackbar.make(rootCoordinator, msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}
