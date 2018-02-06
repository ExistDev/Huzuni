package me.existdev.exist.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.command.Command;
import me.existdev.exist.module.Module;
import me.existdev.exist.utils.ChatUtils;

public class ToggleCommand implements Command {

	@Override
	public boolean run(String[] args) {
		if (args.length == 2) {
			Module module = Exist.moduleManager.getModuleByName(args[1]);
			if (module == null) {
				ChatUtils.tellPlayer(
						ChatFormatting.RED + "Error! " + ChatFormatting.WHITE + args[1] + " does not exist.");
				return true;
			}
			module.toggle();
			ChatUtils.tellPlayer(args[1] + " was Toggled!");
			return true;
		}
		return false;
	}

	@Override
	public String usage() {
		return " -toggle / -t [Module]";
	}

}
