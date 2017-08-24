package com.tangguo.tangguoxianjin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 判断给定字符串是否空白串。
     * 空白串是指由空格、制表符、回车符、换行符组成的字符串
     * 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input) || "NULL".equals(input)) return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String input) {
        return !isEmpty(input);
    }

    public static boolean isEmpty2(String input) {
        if (input == null || "".equals(input) || "null".equals(input) || "[]".equals(input) || "{}".equals(input) || "NULL".equals(input))
            return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }


    public static boolean isEquals(String s1, String s2) {
        if (null == s1 && null == s2) {
            return true;
        }
        if (null != s1 && null != s2) {
            if (s1.trim().equals(s2.trim())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //前一个String是否包括后一个
    public static boolean isContains(String s1, String s2) {
        if (null == s1 && null == s2) {
            return true;
        }
        if (null != s1 && null != s2) {
            if (s1.trim().contains(s2.trim())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public static float toFloat(String str, float defValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static double toDouble(String str, float defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static boolean isStringDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null) return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 文本转换,主要用于“非富文本”的换行处理，解决中文换行混乱问题
     *
     * @param oriString
     * @return
     */
    public static String formatText(String oriString) {
        oriString = stringFilter(oriString);
        return toDBC(oriString);
    }

    /**
     * * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":").replaceAll("，", ",").replaceAll("。", ".");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    /**
     * @param str 要验证的字符串,是否是正确的电话号码
     * @return 返回true表示通过，返回false表示失败
     */
    public static boolean checkPhoneNum(String str) {
        boolean bo = false;

        if (!isEmpty(str)) {
            if (str.length() >= 11) bo = true;
        }
        return bo;
    }


    /**
     * 11位手机号掩码
     *
     * @param strNum
     * @return
     */
    public static String setPhoneNumMask(String strNum) {
        String maskNum = "";
        if ((!isEmpty(strNum) && strNum.length() >= 11)) {
            maskNum = strNum.replace(strNum.substring(3, 7), "****");
        } else maskNum = strNum;
        return maskNum;
    }

    /**
     * 姓名掩码
     *
     * @param strNum
     * @return
     */
    public static String setNameMask(String strNum) {
        String maskNum = "";
        if ((!isEmpty(strNum) && strNum.length() >= 2)) {
            maskNum = strNum.replace(strNum.substring(0, 1), "*");
        } else maskNum = strNum;
        return maskNum;
    }

    /**
     * 姓名掩码
     *
     * @param strNum
     * @return
     */
    public static String setIdCardMask(String strNum) {
        String maskNum = "";
        if ((!isEmpty(strNum) && strNum.length() >= 14)) {
            maskNum = strNum.replace(strNum.substring(6, 14), "********");
        } else maskNum = strNum;
        return maskNum;
    }


    // base64字符串转化成Bitmap
    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String bitmaptoString(Bitmap bitmap) {
        //将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }


}
