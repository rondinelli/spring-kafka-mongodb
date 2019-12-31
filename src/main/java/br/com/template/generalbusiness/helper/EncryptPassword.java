package br.com.template.generalbusiness.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Base64;

public class EncryptPassword {

    public static synchronized String getSha (String input) {
        MessageDigest mda = null;
        try {
            mda = MessageDigest.getInstance ("SHA-512");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        }

        try {
            mda.update (input.getBytes ("UTF-8"));
        }
        catch (java.io.UnsupportedEncodingException ex) {}
        byte[] rawData = mda.digest ();
        byte[] encoded = Base64.encode (rawData);
        String retValue = new String (encoded);
        return retValue;

    }

}