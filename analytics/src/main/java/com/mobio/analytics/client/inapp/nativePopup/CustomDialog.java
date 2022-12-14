package com.mobio.analytics.client.inapp.nativePopup;

import android.app.Activity;
import android.content.Intent;

import com.mobio.analytics.client.model.digienty.Push;

public class CustomDialog extends BaseDialog{
    private Push push;
    private Class<?> des;

    public CustomDialog(Activity activity, Push push, Class<?> des) {
        this.activity = activity;
        this.push = push;
        this.des = des;
    }

    public static void showCustomDialog(Activity activity, Push push, Class<?> des){
        new CustomDialog(activity, push, des).show();
    }

    @Override
    void close() {
        //empty body
    }

    @Override
    void cancel() {
        //empty body
    }

    @Override
    void action() {
        if (des != null && !activity.getClass().getSimpleName().equals("LoginActivity")) {
            Intent desIntent = new Intent(activity, des);
            activity.startActivity(desIntent);
        }
    }

    @Override
    void doDismiss() {
        //empty body
    }

    @Override
    String getDetail() {
        return push.getAlert().getContentType().equals(Push.Alert.TYPE_TEXT)
                ? push.getAlert().getBody()
                : push.getAlert().getBodyHTML() ;
    }

    @Override
    String getTitle() {
        return push.getAlert().getTitle();
    }

    @Override
    String getAction() {
        return "Đồng ý";
    }

    @Override
    String getImage() {
        return push.getAlert().getString("background_image");
    }
}
