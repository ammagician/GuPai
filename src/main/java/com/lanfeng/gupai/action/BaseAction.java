package com.lanfeng.gupai.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.lanfeng.gupai.utils.common.JSONObject;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {

	private static final long serialVersionUID = 4468730695561812593L;
	protected String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Locale getCustomLocale() {
		return getRequest().getLocale();
	}

	public <E> E getParam(String key) {
		JSONObject params = JSONObject.fromObject(data);
		return (E) params.getMap().get(key);
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		HttpServletResponse response = ServletActionContext.getResponse();
		// response.setContentType(CONTENTTYPE_JSON);
		response.setCharacterEncoding("UTF-8");
		return response;
	}

	public HttpServletResponse getXmlResponse() {
		HttpServletResponse response = getResponse();
		// response.setContentType(CONTENTTYPE_XML);
		response.setCharacterEncoding("UTF-8");
		return response;
	}

	public HttpServletResponse getHtmlResponse() {
		HttpServletResponse response = getResponse();
		// response.setContentType(CONTENTTYPE_HTML);
		response.setCharacterEncoding("UTF-8");
		return response;
	}

	public HttpServletResponse getPlainResponse() {
		HttpServletResponse response = getResponse();
		// response.setContentType(CONTENTTYPE_PLAIN);
		response.setCharacterEncoding("UTF-8");
		return response;
	}

	public PrintWriter getPrintWriter() throws IOException {
		return getResponse().getWriter();
	}

	public void writer(Object resultData) throws IOException {
		JSONObject ret = new JSONObject();
		ret.put("data", resultData);
		ret.put("code", 0);
		PrintWriter out = getPrintWriter();
		out.print(ret.toString());
	}
}
