package com.lanfeng.gupai.utils.common;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {
    /*
    public static void main(String[] args) throws Exception {
        byte[] b = EncryptUtil.getKey();
        System.out.println("Length:" + b.length);
        for (byte temp : b) {
            System.out.print(temp + " ");
        }
        System.out.println("");

        String s = "I love you.";
        System.out.println(s);
        byte[] encryptedData = EncryptUtil.encrypt(s.getBytes(), b);
        //System.out.println(new String(encryptedData));
        System.out.println(Arrays.toString(encryptedData));
        byte[] decryptedData = EncryptUtil.decrypt(encryptedData, b);
        //System.out.println(new String(decryptedData));
        System.out.println(Arrays.toString(decryptedData));
    }

    static byte[] encrypt(byte[] data, byte[] b) throws Exception {
        DESKeySpec dks = new DESKeySpec(b);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }

    static byte[] decrypt(byte[] data, byte[] b) throws Exception {
        DESKeySpec dks = new DESKeySpec(b);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        SecureRandom sr = new SecureRandom();
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        byte[] decryptedData = cipher.doFinal(data);
        return decryptedData;
    }

    static byte[] getKey() throws Exception {
        SecureRandom sr = new SecureRandom();
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        kg.init(sr);
        SecretKey key = kg.generateKey();
        byte[] b = key.getEncoded();
        return b;
    }
    */
    
    public static void main(String[] args) {
        //byte[] bys = { 0, 1, 2, 3, 4, 5, 6, 7 };
        String s = "c18e4af8-7d1f-4ec8-962e-19808da45d6d";
        
        String base64 = encodeBase64(s);
        System.out.println("encode base64: " + base64);
        System.out.println("decode base64: " + decodeBase64(base64));
    }
    
    public static String encodeBase64(String s) {
        if(s == null) {
            return null;
        }
        byte[] bys = s.getBytes();

        return new String(Base64.encodeBase64(bys));
    }
    
    public static String decodeBase64(String base64) {
        if(base64 == null) {
            return null;
        }
        return new String(Base64.decodeBase64(base64.getBytes()));
    }
    /*
    public static String getBASE64(String s){
        if(s == null) return null;
        return new sun.misc.BASE64Encoder().encode(s.getBytes());
    }
    
    public static String getFromBASE64(String s){
        if(s == null) return null;
        try {
            byte[] b = new sun.misc.BASE64Decoder().decodeBuffer(s);
            return new String(b);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    */
}
