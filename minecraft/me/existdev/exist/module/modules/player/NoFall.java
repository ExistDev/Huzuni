
package me.existdev.exist.module.modules.player;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPacket;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {

	ArrayList<String> Modes = new ArrayList<>();

	public NoFall() {
		super("NoFall", 0, Category.Player);
		Modes.add("AAC");
		Modes.add("Mineplex");
		Modes.add("Hypixel");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", Modes));
	}

	@EventTarget
	public void onPacketSend(EventPacket e) {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")) {
			AAC();
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Hypixel")) {
			Hypixel(e);
		}
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Mineplex")) {
			if (mc.thePlayer.fallDistance > 2 && !mc.thePlayer.onGround) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
			}
		}
		super.onUpdate();
	}

	public void Hypixel(EventPacket e) {
		if (e.getPacket() instanceof C03PacketPlayer) {
			if (Minecraft.getMinecraft().thePlayer.fallDistance > 3.0F && !(mc.thePlayer.onGround)) {
				C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
				packet.field_149474_g = true;
			}
		}
	}

	public void AAC() {
		if (mc.theWorld == null || mc.thePlayer == null) {
			return;
		}
		if (mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder()) {
			mc.thePlayer.motionY = -9.899999618530273D;
		}
	}
}
