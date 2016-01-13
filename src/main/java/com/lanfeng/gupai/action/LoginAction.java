/**
 *
 */
package com.lanfeng.gupai.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lanfeng.gupai.utils.common.JSONObject;

/**
 * @author apang
 *
 */
public class LoginAction extends BaseAction {

	public String doLogin() throws IOException {
		JSONObject d = JSONObject.fromObject(data);
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();
		session.setAttribute("user", d.get("user"));
		writer("success");
		return NONE;
	}

}
