package me.existdev.exist.module.modules.movement;

import me.existdev.exist.module.Module;

public class WallGlide extends Module {

	public WallGlide() {
		super("WallGlide", 0, Category.Movement);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.onGround) {
			mc.thePlayer.motionY = -0.192;
		}
		super.onUpdate();
	}

}
