package me.existdev.exist.module.modules.render;

import java.util.ArrayList;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;

public class ESP extends Module {

	ArrayList<String> Modes = new ArrayList();

	public ESP() {
		super("ESP", 0, Category.Render);
		Modes.add("CSGO");
		Modes.add("Box");
		Modes.add("CSGOLine");
		Modes.add("Outline");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "CSGO", Modes));
	}

}
