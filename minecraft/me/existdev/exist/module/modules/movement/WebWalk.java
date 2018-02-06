package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;

public class WebWalk extends Module {

	ArrayList<String> Modes = new ArrayList<>();

	public WebWalk() {
		super("WebWalk", 0, Category.Movement);
		Exist.settingManager.addSetting(new Setting(this, "WebUp", 10, 0, 20, true));
	}

}
