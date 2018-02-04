package com.quantech.misc;

import java.util.Arrays;

public class EntityFieldHandler {

    // Throws NullPointerException with custom error message.
    public static Object nullCheck(Object obj, String name) throws NullPointerException {
        if (obj == null)
            throw new NullPointerException("Error: " + name + " in patient cannot be assigned null value.");
        return obj;
    }

    // Checks if a given name is valid.
    public static String nameValidityCheck(String name) {
        nullCheck(name,"name");
        if (name.matches(" *")) {
            throw new IllegalArgumentException("Error: Name cannot take the given form (empty string)");
        }
        return name;
    }


    // Checks if a given email address is valid.
    public static String emailValidityCheck(String emailAddress) {
        nullCheck(emailAddress,"email address");
        if(!emailAddress.matches("^[-A-Za-z0-9]+@[-A-Za-z0-9]+(\\.[a-zA-Z]{2,})+"))
            throw new IllegalArgumentException("Error: Email address is not valid");
        return emailAddress;
    }

    // Places the given name into the correct form
    // e.g. nuha tumia -> Nuha Tumia
    // e.g. hARRy o'doNNel -> Harry O'Donnel etc..
    public static String putNameIntoCorrectForm(String name) {
        // Look at spaces.
        name = name.toLowerCase().trim();
        return formatIntoNameAroundString(formatIntoNameAroundString(formatIntoNameAroundString(name," "),"-"),"'");
    }

    private static String formatIntoNameAroundString(String name, String regex) {
        String[] s = name.split(regex+"+");
        for (int i = 0; i < s.length; i++) {
            s[i] = s[i].trim();
            String x = s[i].substring(0,1).toUpperCase();
            s[i] = x + s[i].substring(1);

            if (i < s.length - 1) {
                s[i] = s[i] + regex;
            }
        }
        return Arrays.stream(s).reduce("", String::concat);
    }
}
