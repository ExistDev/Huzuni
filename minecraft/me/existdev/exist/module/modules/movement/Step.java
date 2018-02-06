package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.Line3D;
import me.existdev.exist.utils.Location3D;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Step extends Module {

	private ArrayList<String> Modes = new ArrayList<>();
	public static boolean Step = false;
	double groundy = 0.0D;

	public Step() {
		super("Step", 0, Category.Movement);
		Modes.add("AAC");
		Modes.add("NewAAC");
		Modes.add("Normal");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AAC", Modes));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.moduleManager.getModule(Speed.class).isToggled()) {
			return;
		}
		if (mc.thePlayer.isOnLadder()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("NewAAC")) {
			if (this.mc.gameSettings.keyBindForward.pressed) {
				if (!this.mc.thePlayer.isInWater()) {
					Line3D line = new Line3D(
							new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ),
							(double) (-this.mc.thePlayer.rotationYaw), 0.0D, 1.0D);
					BlockPos willBeTop = new BlockPos(line.getEnd().getX(), line.getEnd().getY() + 1.0D,
							line.getEnd().getZ());
					if (this.mc.theWorld.getBlockState(willBeTop).getBlock()
							.getBlockBoundsMaxY() != this.mc.theWorld.getBlockState(willBeTop).getBlock()
									.getBlockBoundsMinY() + 1.0D
							|| this.mc.theWorld.getBlockState(willBeTop).getBlock().isTranslucent()
							|| this.mc.theWorld.getBlockState(willBeTop).getBlock() == Blocks.water
							|| this.mc.theWorld.getBlockState(willBeTop).getBlock() instanceof BlockSlab) {
						if (this.mc.thePlayer.isCollidedHorizontally
								&& Math.abs(this.mc.thePlayer.motionX) + Math.abs(this.mc.thePlayer.motionZ) < 0.1D
								&& this.mc.thePlayer.motionY <= 0.0D) {
							this.mc.thePlayer.jump();
						} else {
							BlockPos willBe = new BlockPos(line.getEnd().getX(), line.getEnd().getY(),
									line.getEnd().getZ());
							if (this.mc.theWorld.getBlockState(willBe).getBlock()
									.getBlockBoundsMaxY() == this.mc.theWorld.getBlockState(willBe).getBlock()
											.getBlockBoundsMinY() + 1.0D
									&& !this.mc.theWorld.getBlockState(willBe).getBlock().isTranslucent()
									&& this.mc.theWorld.getBlockState(willBe).getBlock() != Blocks.water
									&& !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockSlab)
									&& !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockStairs)
									&& !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockFence)
									&& this.mc.thePlayer.posY < (double) ((int) this.mc.thePlayer.posY) + 0.5D
									&& !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockSign)
									&& !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockFence)
									&& !(this.mc.theWorld.getBlockState(willBe)
											.getBlock() instanceof BlockStainedGlassPane)) {
								if (this.mc.thePlayer.motionY <= 0.0D) {
									this.groundy = this.mc.thePlayer.posY;
									this.mc.thePlayer.motionY = 0.41999998688697815D;
								}

								this.move(this.mc.thePlayer.rotationYaw, 0.25F);
								Step = true;
							}

							if (this.mc.thePlayer.posY > this.groundy + 0.8D && Step) {
								this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, (double) ((int) this.groundy + 1),
										this.mc.thePlayer.posZ);
								this.mc.thePlayer.motionY = -1.0D;
								this.move(this.mc.thePlayer.rotationYaw, 0.2F);
								Step = false;
							}
						}
					}
				}
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("AAC")
				&& this.isToggled()) {
			if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.4;
			}
			if (mc.thePlayer.hurtTime > 0) {
				return;
			}
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Normal")
				&& this.isToggled()) {
			if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.38;
			}
		}
		super.onUpdate();
	}

	public void move(float yaw, float multiplyer) {
		double moveX = -Math.sin(Math.toRadians((double) yaw)) * (double) multiplyer;
		double moveZ = Math.cos(Math.toRadians((double) yaw)) * (double) multiplyer;
		this.mc.thePlayer.motionX = moveX;
		this.mc.thePlayer.motionZ = moveZ;
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0f;
		super.onDisable();
	}

}
