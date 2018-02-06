package me.existdev.exist;

import org.lwjgl.opengl.Display;

import me.existdev.exist.command.CommandManager;
import me.existdev.exist.file.FileManager;
import me.existdev.exist.font.NormalFontRenderer;
import me.existdev.exist.gui.TabGUI;
import me.existdev.exist.gui.altchanger.AltManager;
import me.existdev.exist.module.ModuleManager;
import me.existdev.exist.setting.SettingManager;

public class Exist {

	private final static String clientName = "Exist";
	private final static double clientVersion = 0.3;

	public static SettingManager settingManager;
	public static CommandManager commandManager;
	public static ModuleManager moduleManager;
	public static FileManager fileManager;
	public static NormalFontRenderer normalFontRenderer;
	public static AltManager altManager;
	public static TabGUI tabGUI;

	public Exist() {
		tabGUI = new TabGUI();
		settingManager = new SettingManager();
		moduleManager = new ModuleManager();
		fileManager = new FileManager();
		normalFontRenderer = new NormalFontRenderer();
		commandManager = new CommandManager();
		altManager = new AltManager();
		commandManager.loadCommands();
		altManager.setupAlts();
	}

	public static String getClientName() {
		return clientName;
	}
	
	public static double getClientVersion() {
		return clientVersion;
	}

}
