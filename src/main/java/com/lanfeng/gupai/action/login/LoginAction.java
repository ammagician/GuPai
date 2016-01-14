/**
 *
 */
package com.lanfeng.gupai.action.login;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lanfeng.gupai.action.BaseAction;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

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
	
	public String doTestLogin() throws IOException {
		HttpServletRequest req = getRequest();
		HttpSession session = req.getSession();
		String user = (String)session.getAttribute("user");
		JSONObject d = new JSONObject();
		d.put("login", true);
		if(!StringUtil.isValid(user)){
			d.put("login", false);
			System.out.println("No login");
		}
		writer(d);
		return NONE;
	}

}
