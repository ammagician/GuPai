package com.lanfeng.gupai.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;

public class LoginFilter extends StrutsPrepareAndExecuteFilter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain fc) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession session = ((HttpServletRequest)req).getSession();
		HttpServletResponse response = (HttpServletResponse)res;
		String user = (String) session.getAttribute("user");
		if(!StringUtil.isValid(user)){
			JSONObject rs = new JSONObject();
			rs.put("msg", "LoginFilter No login");
			JSONObject ret = new JSONObject();
			ret.put("data", rs);
			ret.put("code", -1);
			PrintWriter out = response.getWriter();
			out.print(ret.toString());
		}else{
			super.doFilter(req, res, fc);
		}
	}

}
