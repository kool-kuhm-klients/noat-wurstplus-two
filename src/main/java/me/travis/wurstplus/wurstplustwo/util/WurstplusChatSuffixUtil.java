package me.travis.wurstplus.wurstplustwo.util;

public class WurstplusChatSuffixUtil {

    private static String message;

    public static void set_message(String message) {
        WurstplusChatSuffixUtil.message = message;
    }

    public static String get_message() {
        return message;
    }

}
