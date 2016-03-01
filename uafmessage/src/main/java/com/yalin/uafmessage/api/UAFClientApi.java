package com.yalin.uafmessage.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.yalin.uafmessage.msg.AsmInfo;
import com.yalin.uafmessage.msg.DeregisterAuthenticator;
import com.yalin.uafmessage.msg.DeregistrationRequest;
import com.yalin.uafmessage.msg.RegRecord;
import com.yalin.uafmessage.msg.client.UAFMessage;
import com.yalin.uafmessage.utils.Utils;

import java.util.List;

/**
 * Created by YaLin on 2016/1/11.
 */
public class UAFClientApi {

    public static void doDiscover(Activity activity, int requestCode) {
        if (activity == null) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getDiscoverIntent();
        activity.startActivityForResult(intent, requestCode);
    }

    public static void doCheckPolicy(Activity activity, int requestCode, String responseMessage) {
        if (activity == null || TextUtils.isEmpty(responseMessage)) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getCheckPolicyIntent(new UAFMessage(responseMessage).toJson(), activity.getApplication().getPackageName());
        activity.startActivityForResult(intent, requestCode);
    }

    public static void doOperation(Activity activity, int requestCode, String responseMessage, String channelBinding) {
        if (activity == null || TextUtils.isEmpty(responseMessage)) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getUAFOperationIntent(new UAFMessage(responseMessage).toJson(), null, channelBinding);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void doOperation(Fragment fragment, int requestCode, String responseMessage, String channelBinding) {
        if (fragment == null || TextUtils.isEmpty(responseMessage)) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getUAFOperationIntent(new UAFMessage(responseMessage).toJson(), null, channelBinding);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static List<RegRecord> getRegRecords(String username) {
        if (TextUtils.isEmpty(username)) {
            throw new IllegalArgumentException();
        }
        return null;
    }

    public static String getFacetId(Context context) {
        return Utils.getFacetId(context);
    }

    public static DeregistrationRequest[] getDeRegistrationRequests(RegRecord regRecord) {
        if (regRecord == null) {
            throw new IllegalArgumentException("regRecord must not be null!");
        }
        DeregistrationRequest[] deRegistrationRequests = new DeregistrationRequest[1];
        deRegistrationRequests[0] = new DeregistrationRequest();
        deRegistrationRequests[0].authenticators = new DeregisterAuthenticator[1];
        deRegistrationRequests[0].authenticators[0] = new DeregisterAuthenticator();
        deRegistrationRequests[0].authenticators[0].aaid = regRecord.aaid;
        deRegistrationRequests[0].authenticators[0].keyID = regRecord.keyId;

        return deRegistrationRequests;
    }

    public static AsmInfo getDefaultAsmInfo() {
        return null;
    }

    public static void clearDefaultAsm() {
    }
}
