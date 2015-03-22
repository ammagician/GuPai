/**
 *
 */
package com.lanfeng.gupai.action;

import java.io.IOException;

/**
 * @author apang
 *
 */
public class LoginAction extends BaseAction {

	public String doLogin() throws IOException {
		// JSONObject dataObj = new JSONObject(data);

		writer("hello");
		return NONE;
	}

}
