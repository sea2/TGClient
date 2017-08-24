package com.tangguo.tangguoxianjin.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tangguo.tangguoxianjin.R;


/**
 * 消息提示的显现与消失
 *
 * @author
 */
public class ToastShow {
    private Toast toast = null;
    private TextView tv_toast_text = null;

    public ToastShow(Context context) {
        View layout = null;
        if (tv_toast_text == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            tv_toast_text = (TextView) layout.findViewById(R.id.tv_toast_text);
        }
        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            if (layout == null) {
                layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
                tv_toast_text = (TextView) layout.findViewById(R.id.tv_toast_text);
            }
            toast.setView(layout);
        }
    }

    public ToastShow(Context context, boolean isLong) {
        View layout = null;
        if (tv_toast_text == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            tv_toast_text = (TextView) layout.findViewById(R.id.tv_toast_text);
        }

        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            if (isLong) toast.setDuration(Toast.LENGTH_LONG);
            else toast.setDuration(Toast.LENGTH_SHORT);
            if (layout == null) {
                layout = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
                tv_toast_text = (TextView) layout.findViewById(R.id.tv_toast_text);
            }
            toast.setView(layout);
        }
    }

    public Toast getToast() {
        return toast;
    }

    public void setToast(Toast toast) {
        this.toast = toast;
    }

    // 消息框的显现
    public void show(CharSequence text) {
        if (!TextUtils.isEmpty(text) && toast != null && tv_toast_text != null) {
            tv_toast_text.setText(text);
            toast.show();
        }
    }


    // 消息框的消失
    public void cancel() {
        if (toast != null) toast.cancel();
    }

}
