package com.lanfeng.gupai.utils.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class MD5Util {

    private MD5Util() {

    }

    public static String digest(String beforeMD5) {
        return digest(beforeMD5.getBytes());
    }

    public static String digest(byte[] beforeMD5) {
        MessageDigest md5 = getMD5();
        byte[] bytes = md5.digest(beforeMD5);
        return getHexString(bytes);
    }

    private static MessageDigest getMD5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHexString(byte[] b) {
        String result = "";
        for (byte element : b) {
            result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static void main(String[] s) throws Exception {
        System.out.println(MD5Util.digest("andy"));
        System.out.println("----------------------");
        System.out.println(MD5Util.digest("andy"));
        System.out.println("----------------------");
        System.out.println(MD5Util.digest("lily"));
        System.out.println("----------------------");
        System.out.println(MD5Util.digest("andy").equals(MD5Util.digest("andy")));

        System.out.println(new Date().getTime());
        for (int i = 0; i < 100000; i++) {
            MD5Util.digest("andyssssssssssssssssssssssss");
        }
        System.out.println(new Date().getTime());
    }

}
