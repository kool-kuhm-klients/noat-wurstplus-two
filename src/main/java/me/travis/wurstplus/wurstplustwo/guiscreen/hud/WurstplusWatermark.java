package me.travis.wurstplus.wurstplustwo.guiscreen.hud;

import java.awt.Color;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.guiscreen.render.pinnables.WurstplusPinnable;


public class WurstplusWatermark extends WurstplusPinnable {
	public WurstplusWatermark() {
		super("Watermark", "Watermark", 1, 0, 0);
	}

	@Override
	public void render() {
		cycle_rainbow();

		int nl_r = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorR").get_value(1);
		int nl_g = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorG").get_value(1);
		int nl_b = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorB").get_value(1);
		int nl_a = Wurstplus.get_setting_manager().get_setting_with_tag("HUD", "HUDStringsColorA").get_value(1);

		String line = "noat's Wurst+ 2" + Wurstplus.wh + "v" + Wurstplus.get_version();

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
	}

	public void cycle_rainbow() {

			float[] tick_color = {
							(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
			};

			int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

			nl_r.set_value((color_rgb_o >> 16) & 0xFF);
			nl_g.set_value((color_rgb_o >> 8) & 0xFF);
			nl_b.set_value(color_rgb_o & 0xFF);

	}
}
