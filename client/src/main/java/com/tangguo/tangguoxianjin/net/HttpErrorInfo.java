package com.tangguo.tangguoxianjin.net;

import java.io.Serializable;

/**
 * 网络请求错误信息
 * 
 * @author zhangmingxue@niwodai.net
 *
 */
public class HttpErrorInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String msg;
	private String code;
	private String response;

	@Override
	public String toString() {
		return msg;
	}

	public HttpErrorInfo() {

	}

	public HttpErrorInfo(String argMsg, String argCode, String argResponse) {
		msg = argMsg;
		code = argCode;
		response = argResponse;
	}

	public static HttpErrorInfo getSimpleErrorInfo(String message) {
		HttpErrorInfo errorInfo = new HttpErrorInfo();
		errorInfo.msg = message;
		return errorInfo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
