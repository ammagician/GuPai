package com.lanfeng.gupai.utils.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {
	public static boolean isValid(String str) {
		return str != null && !str.isEmpty();
	}

	public static boolean equals(String str1, String str2) {
		if (!isValid(str1) && !isValid(str2)) {
			return true;
		} else if (isValid(str1) && isValid(str2)) {
			return str1.equals(str2);
		}
		return false;
	}

	public static String specialEncode(String symbol) {
		if (!StringUtil.isValid(symbol)) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for (char ch : symbol.toCharArray()) {
			try {
				String tmp = URLEncoder.encode(String.valueOf(ch), "UTF-8");
				if (tmp.indexOf("%") != -1) {
					tmp = URLDecoder.decode(tmp, "UTF-8");
				}
				builder.append(tmp);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return builder.toString();
	}

	public static String[] split(String text, String separator) {
		List<String> list = new ArrayList<String>();
		if (!isValid(text) || !isValid(separator)) {
			return list.toArray(new String[0]);
		}

		int pos = 0;
		int len = text.length();
		int sepLen = separator.length();
		while (pos < len) {
			int end = text.indexOf(separator, pos);
			String temp = null;
			if (end == -1) {
				temp = text.substring(pos);
				pos = len;
			} else {
				temp = text.substring(pos, end);
				pos = end + sepLen;
			}

			if (isValid(temp)) {
				list.add(temp);
			}
		}

		return list.toArray(new String[0]);
	}

	public static void remove(String[] res, String s) {
		int index = -1;
		for (int i = 0; i < res.length; i++) {
			if (res[i].equals(s)) {
				index = i;
			}
		}
		if (index != -1) {
			for (int i = index + 1; i < res.length; i++) {
				res[i - 1] = res[i];
			}
		}
	}

	public static int search(String[] res, String s) {
		for (int i = 0; i < res.length; i++) {
			if (s.equals(res[i])) {
				return i;
			}
		}
		return -1;
	}

	public static String getDecimalPlace(boolean decimalPlaceFormat, String decimalPlaceStr) {
		String decimalPlace = "0.";
		if (decimalPlaceFormat) {
			int count = Integer.parseInt(decimalPlaceStr.substring(4, 5));
			while (count > 0) {
				decimalPlace = decimalPlace + "0";
				count--;
			}
		} else {
			decimalPlace = "0.00000";
		}
		return decimalPlace;
	}

	/**
	 * @param str
	 * @return a string wrap with double quotes. for example, hello would be
	 *         return "hello"
	 */
	public static String wrapWithDoubleQuotes(String str) {
		return "\"" + str + "\"";
	}

	public static boolean isNumeric(String str) {
		if (!isValid(str)) {
			return false;
		}

		for (int i = 0, len = str.length(); i < len; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
