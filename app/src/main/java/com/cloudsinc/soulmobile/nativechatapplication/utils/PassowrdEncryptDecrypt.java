package com.cloudsinc.soulmobile.nativechatapplication.utils;

/**
 * Created by developers on 23/4/18.
 */

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class PassowrdEncryptDecrypt {

    private static final String algorithm = "AES/CBC/NoPadding";

    public static final byte[] keyValue = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static final byte[] ivValue = new byte[] { 'f', 'e', 'd', 'c', 'b', 'a', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' };

    private static final IvParameterSpec ivspec = new IvParameterSpec(ivValue);
    private static final SecretKeySpec keyspec = new SecretKeySpec(keyValue, "AES");

    // final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String encrypt(String Data) throws Exception {

        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encVal = c.doFinal(padString(Data).getBytes());
        String encryptedValue = new String(Base64.encode(encVal, Base64.DEFAULT));
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Cipher c = Cipher.getInstance(algorithm);
        c.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] decordedValue = Base64.decode(encryptedData,Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++)
        {
            source += paddingChar;
        }
        return source;
    }
}