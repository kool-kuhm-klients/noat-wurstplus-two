package me.travis.wurstplus.wurstplustwo.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.command.WurstplusCommand;
import me.travis.wurstplus.wurstplustwo.util.WurstplusWatermarkHudUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;

public class WurstplusWatemrark extends WurstplusCommand {

    public WurstplusWatermark() {
        super("chatwm", "change wurstplus chat wm thing");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            WurstplusMessageUtil.send_client_error_message("message needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder wm = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                wm.append(word).append(" ");
            }
            WurstplusWatermarkHudUtil.set_message(wm.toString());
            WurstplusMessageUtil.send_client_message("wm message changed to " + ChatFormatting.BOLD + wm.toString());
            Wurstplus.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}
