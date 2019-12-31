package br.com.template.rest.token;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;

public class UtilSecurity {

    public static Credentials parseHeader(String authCredentials) {

        if (null == authCredentials) {
            return null;
        }

        final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.decodeBase64(encodedUserPassword.getBytes());
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        Credentials credentials = new Credentials(username, password);

        return credentials;
    }

    public static String parseCredentials(Credentials credentials) {
        String cred = String.format("%s:%s", credentials.getUsername(), credentials.getPassword());
        byte[] decodedBytes = Base64.encodeBase64(cred.getBytes());
        String encoded = null;
        try {
            encoded = new String(decodedBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format("Basic %s", encoded);
    }

}
