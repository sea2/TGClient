package com.tangguo.tangguoxianjin.util;

import android.widget.TextView;

/**
 * Created by lhy on 2017/4/24.
 */

public class TextViewUtil {

    public static String getText(TextView textview) {
        if (textview == null) return "";
        else {
            if (textview.getText() == null) return "";
            else {
                return textview.getText().toString().trim();
            }
        }
    }


}
