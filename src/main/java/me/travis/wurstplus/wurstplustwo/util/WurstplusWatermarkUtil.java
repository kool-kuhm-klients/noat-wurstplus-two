package me.travis.wurstplus.wurstplustwo.util;

public class WurstplusWatermarkUtil {

    private static String message;

    public static void set_message(String message) {
        WurstplusChatSuffixUtil.message = message;
    }

    public static String get_message() {
        return message;
    }

}
