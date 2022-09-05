package com.mobio.analytics.client.inapp;

import static com.mobio.analytics.client.activity.PopupBuilderActivity.M_KEY_PUSH;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.mobio.analytics.client.MobioSDK;
import com.mobio.analytics.client.activity.PopupBuilderActivity;
import com.mobio.analytics.client.model.digienty.Push;
import com.mobio.analytics.client.inapp.htmlPopup.HtmlController;
import com.mobio.analytics.client.inapp.nativePopup.InAppNativeFragment;

import java.security.SecureRandom;

public class InAppController {

    private static final String IN_APP_TYPE_POPUP_BUILDER = "popup";
    private static final String IN_APP_TYPE_HTML = "html";
    private static final String IN_APP_TYPE_ALERT = "text";

    private Push push;
    private Class<?> des;
    private String assetPath;

    public InAppController(Push push, Class<?> des, String assetPath) {
        this.push = push;
        this.des = des;
        this.assetPath = assetPath;
    }

    public static void showInApp(Activity activity, Push push, String assetPath, Class<?> des){
        InAppNativeFragment inAppFragment = null;
        String type = push.getAlert().getContentType();
        switch (type) {
            case IN_APP_TYPE_ALERT:
                int reqId = new SecureRandom().nextInt(10000);
                MobioSDK.getInstance().showGlobalNotification(push, reqId);
                break;
            case IN_APP_TYPE_HTML:
                startPopupActivity(activity, push);
                break;
            case IN_APP_TYPE_POPUP_BUILDER:
                if(getPopupPosition(push).equals("cc")){
                    startPopupActivity(activity, push);
                }
                else {
                    HtmlController.showHtmlPopup(activity, push, assetPath);
                }
                break;
            default:
                break;
        }
    }

    private static void startPopupActivity(Activity currentActivity, Push push) {
        Intent i = new Intent(currentActivity, PopupBuilderActivity.class);
        i.putExtra(M_KEY_PUSH, new Gson().toJson(push));
        currentActivity.startActivity(i);
    }

    private static String getPopupPosition(Push push){
        String positionPopup = push.getData().getString("position");
        if(positionPopup == null || positionPopup.isEmpty()) return "cc";
        return positionPopup;
    }
}
