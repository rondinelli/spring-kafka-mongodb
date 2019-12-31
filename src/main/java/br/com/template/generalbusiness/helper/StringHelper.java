package br.com.template.generalbusiness.helper;

import java.util.ArrayList;
import java.util.List;


public class StringHelper {


    public static String stringByReplacingInvalidChars(String str) {
        return str.replace("\u00ad", "-")
                .replaceAll("((\u0000)|(\u00ad))", "");
    }


}
