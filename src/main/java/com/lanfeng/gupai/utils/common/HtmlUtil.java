package com.lanfeng.gupai.utils.common;

public class HtmlUtil {
    public static String createBmkDivs(String dpValue) {
        dpValue = (dpValue == null ? "" : dpValue);
        return "<div class='dash-bmk'><div class='dash-bmk-text'>" + dpValue
                + "</div><div class='dash-bmk-find'></div><div class='dash-bmk-ddl'></div></div>";
    }
}
