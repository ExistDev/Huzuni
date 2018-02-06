package me.existdev.exist.utils;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {

	public static void tellPlayer(String text) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(ChatFormatting.WHITE
				+ "[" + ChatFormatting.AQUA + Exist.getClientName() + ChatFormatting.WHITE + "] " + text));
	}

}
