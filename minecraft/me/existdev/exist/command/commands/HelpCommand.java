package me.existdev.exist.command.commands;

import me.existdev.exist.Exist;
import me.existdev.exist.command.Command;
import me.existdev.exist.utils.ChatUtils;

public class HelpCommand implements Command {

	@Override
	public boolean run(String[] args) {
		ChatUtils.tellPlayer("Here are the list of commands:");
		for (Command c : Exist.commandManager.getCommands().values()) {
			ChatUtils.tellPlayer(c.usage());
		}
		return true;
	}

	@Override
	public String usage() {
		return " -help";
	}

}
