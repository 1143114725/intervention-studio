package com.investigate.newsupper.util;

public final class StringUtils {

	private StringUtils() {
	}
	
    public static String ToSBC(final String input) {
    	try {
            char c[] = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == ' ') {
                    c[i] = '\u3000';
                } else if (c[i] < '\177') {
                    c[i] = (char) (c[i] + 65248);
                }
            }
            return new String(c);
		} catch (Exception e) {
			return input;
		}
    }
}

