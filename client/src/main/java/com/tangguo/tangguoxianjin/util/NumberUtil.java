package com.tangguo.tangguoxianjin.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数据工具类
 * 
 * @author xiaocaimi@xcm.com
 *
 */
public class NumberUtil {
	/**
	 * #,###.##
	 */
	public static DecimalFormat commonDF = new DecimalFormat("#,##0.##");
	/**
	 * #,###.00
	 */
	public static DecimalFormat commonDD = new DecimalFormat("#,##0.00");
	public static DecimalFormat common = new DecimalFormat("#,###");

	public static String commonFormatNumStr(String arg) {
		if (TextUtils.isEmpty(arg)) {
			return "";
		}
		try {
			Double d = Double.valueOf(arg);
			return commonDF.format(d);
		} catch (Exception e) {
			LogManager.e(e.getMessage());
		}
		return "";
	}

	public static String commonFormatNumStr(Double arg) {
		try {
			return commonDF.format(arg);
		} catch (Exception e) {
			LogManager.e(e.getMessage());
		}
		return "";
	}

	public static String commonFormatNumZeroStr(String arg) {
		if (TextUtils.isEmpty(arg)) {
			return "";
		}
		try {
			Double d = Double.valueOf(arg);
			return commonDD.format(d);
		} catch (Exception e) {
			LogManager.e(e.getMessage());
		}
		return "";
	}

	/**
	 * #,###.00
	 * 
	 * @param arg
	 * @return
	 */
	public static String commonFormatNumZeroStr(Double arg) {
		try {
			return commonDD.format(arg);
		} catch (Exception e) {
			LogManager.e(e.getMessage());
		}
		return "";
	}

	public static String commonFormatNum(Double arg) {
		try {
			return common.format(arg);
		} catch (Exception e) {
			LogManager.e(e.getMessage());
		}
		return "";
	}

	public static String commonFormatNumStr(DecimalFormat df, Double arg) {
		try {
			return df.format(arg);
		} catch (Exception e) {
			LogManager.e(e.getMessage());
		}
		return "";
	}

	public static String commonFormatNumStr(DecimalFormat df, BigDecimal arg) {
		try {
			return df.format(arg);
		} catch (Exception e) {
			LogManager.e(e.getMessage());
		}
		return "";
	}

	public static String formatNumber(Long argDouble, String format) {
		return new DecimalFormat(format).format(argDouble);
	}

	public static String formatNumber(Double argDouble, String format) {
		return new DecimalFormat(format).format(argDouble);
	}

	/**
	 * 判断两个Integer对象是否相等
	 * 
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static boolean isEquals(Integer arg1, Integer arg2) {
		if (null == arg1 && null == arg2) {
			return true;
		} else {
			if (null != arg1 && null != arg2
					&& arg1.intValue() == arg2.intValue()) {
				return true;
			}
			return false;
		}
	}

}
