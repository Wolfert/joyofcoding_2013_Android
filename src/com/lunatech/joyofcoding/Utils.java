package com.lunatech.joyofcoding;

public final class Utils {

    public static boolean isNotEmpty(final String str) {
        return (str != null && str.length() > 1);
    }

    public static String getFormattedTwitterHandler(final String twitterHandle) {
        if (isNotEmpty(twitterHandle)) {
            if (twitterHandle.toString().startsWith("@"))
                return twitterHandle.substring(1).replaceAll("\\w", "%20");
        }
        return twitterHandle.replaceAll("\\w", "%20");
    }
}
