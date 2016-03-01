package com.yalin.uafrpclient.activity;

import android.view.View;
import android.view.ViewStub;

import com.yalin.uafrpclient.R;


/**
 * Created by YaLin on 2015/7/30.
 */
public abstract class BaseLoadActivity extends BaseActivity {

    ViewStub vsLoading;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        vsLoading = (ViewStub) findViewById(R.id.vs_loading);
    }

    protected void showLoading() {
        if (vsLoading != null) {
            vsLoading.setVisibility(View.VISIBLE);
        }
    }

    protected void dismissLoading() {
        if (vsLoading != null) {
            vsLoading.setVisibility(View.GONE);
        }
    }
}
