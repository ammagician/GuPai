package com.lanfeng.gupai.utils.common;

public class HtmlEncoder {
    public static String encode(String text) {
        if (text == null) {
            return null;
        }

        text = text.replaceAll("(&)", "&amp;");
        text = text.replaceAll("(<)", "&lt;");
        text = text.replaceAll("(>)", "&gt;");
        text = text.replaceAll("(\")", "&quot;");
        text = text.replaceAll("(\')", "&apos;");
        // text = text.replaceAll("( )", "&nbsp;");

        return text;
    }

    /**replace '&apos;' to '&amp;apos;'
     * @param quote
     * @return
     */
    public static String encodeSingleQuote(String quote) {
        if(quote == null){
            return null;
        }
        return quote.replaceAll("&apos;", "&amp;apos;");
    }
    public static String encodeForGrid(String text) {
        if (text == null) {
            return null;
        }

        text = text.replaceAll("(&)", "&amp;");
        text = text.replaceAll("(<)", "&lt;");
        text = text.replaceAll("(>)", "&gt;");
        text = text.replaceAll("(\")", "&quot;");
        return text;
    }

    public static String decode(String text) {
        if (text == null) {
            return "";
        }

        text = text.replaceAll("(&amp;)", "&");
        text = text.replaceAll("(&lt;)", "<");
        text = text.replaceAll("(&gt;)", ">");
        text = text.replaceAll("(&quot;)", "\"");
        text = text.replaceAll("(&apos;)", "\'");
        text = text.replaceAll("(&#39;)", "\'");
        // text = text.replaceAll("( )", "&nbsp;");
        return text;
    }

    // public static void main(String[] args) {
    // String str = "<script> alert(\"1&\")</script>";
    // String s = new String(str);
    // System.out.println(encode(str));
    // String test = "&amp;";
    // System.out.println("test" + decode(test));
    // }
}
