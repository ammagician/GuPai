package com.lanfeng.gupai.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lanfeng.gupai.exception.GPException;
import com.lanfeng.gupai.utils.common.JSONObject;
import com.lanfeng.gupai.utils.common.StringUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExceptionInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 108731374647184060L;
	protected static final Log log = LogFactory.getLog(ExceptionInterceptor.class);
	private static long counter = 0;
	private static long lastTime = 0;
	private static long lastCounter = 0;

	@Override
	public String intercept(ActionInvocation action) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer = response.getWriter();
		try {
			// check need kicked out or not

			return action.invoke();
		} catch (Exception ex) {
			JSONObject result = new JSONObject();

			if (ex instanceof GPException) {
				GPException aex = (GPException) ex;
				switch (aex.getType()) {
				case BusinessException:
					result.put("code", aex.getBusinessCode());
					break;
				case InvalidUser:
				case InvalidOwner:
					result.put("code", -1); // cookie invalid
					result.put("data", getLoginUrl(request));
					break;
				case SocketTimeoutError:
					result.put("code", -2); // timeout
					break;
				case MssError:
					result.put("code", -3); // MSS exception
					break;
				case CalculationDateChanged:
					result.put("code", -5); // account calculation result does
					// not match, need to re-calculate
					break;
				case JsonFormat:
					result.put("code", -6); // wrong JSON format
					break;
				case NoHoldings:
					result.put("code", -7); // no holdings
					break;
				case RtqError:
					result.put("code", -8); // rtq exception
					break;
				case EndDateIsZero:
					result.put("code", -9); // end date is zero for portfolio
					// value
					break;
				case NoWidgetSetting:
					result.put("code", -10); // no widget setting
					break;
				case ColumnSet:
					result.put("code", -11); // column set
					break;
				case WrongExcelFormat:
					result.put("code", -12); // wrong excel format
					break;
				case WrongSecurityToken:
					result.put("code", -98); // wrong security token
					break;
				default:
					result.put("code", -99); // others
					ex.printStackTrace();
				}
				writer.write(result.toString());
			} else if (ex instanceof NoSuchMethodException) {
				ex.printStackTrace();
				// do nothing
			} else {
				result.put("code", -100); // not PAException
				writer.write(result.toString());
				ex.printStackTrace();
			}
		} finally {
			writer.close();
		}
		return null;
	}

	private String getLoginUrl(HttpServletRequest request) {
		String redir = request.getHeader("referer");
		if (!StringUtil.isValid(redir)) {
			return "";
		}

		if (redir.startsWith("https") == false) {
			redir = redir.replaceFirst("http", "https");
		}

		String url = "login.jsp";
		url += "&Redir=" + redir;

		return url;
	}
}
