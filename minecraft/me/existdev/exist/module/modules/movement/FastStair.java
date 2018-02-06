package me.existdev.exist.module.modules.movement;

import java.util.ArrayList;
import java.util.Iterator;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.events.EventPreMotionUpdates.EventType;
import me.existdev.exist.module.Module;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.ItemFood;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class FastStair extends Module {

	double boost = 0.0D;
	private boolean hasJumped = false;
	double lastY = 0.0D;

	public FastStair() {
		super("FastStair", 0, Category.Movement);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (!this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.motionY <= 0.0D) {
			if (this.boost == 0.10000000149011612D) {
				this.hasJumped = false;
			}

			this.hasJumped = true;

			BlockPos pos1 = new BlockPos(this.mc.thePlayer.posX + 0.3D, this.mc.thePlayer.posY,
					this.mc.thePlayer.posZ + 0.3D);
			BlockPos pos2 = new BlockPos(this.mc.thePlayer.posX + 0.3D, this.mc.thePlayer.posY,
					this.mc.thePlayer.posZ - 0.3D);
			BlockPos pos3 = new BlockPos(this.mc.thePlayer.posX - 0.3D, this.mc.thePlayer.posY,
					this.mc.thePlayer.posZ + 0.3D);
			BlockPos pos4 = new BlockPos(this.mc.thePlayer.posX - 0.3D, this.mc.thePlayer.posY,
					this.mc.thePlayer.posZ - 0.3D);
			ArrayList pos = new ArrayList();
			pos.add(pos1);
			pos.add(pos2);
			pos.add(pos3);
			pos.add(pos4);
			boolean doIt = false;
			Iterator var8 = pos.iterator();

			BlockPos next;
			while (var8.hasNext()) {
				next = (BlockPos) var8.next();
				if (this.mc.theWorld.getBlockState(next).getBlock() instanceof BlockStairs
						|| this.mc.theWorld.getBlockState(next).getBlock() instanceof BlockSnow) {
					doIt = true;
					break;
				}
			}

			if (this.lastY < this.mc.thePlayer.posY && this.mc.thePlayer.motionY <= 0.0D && doIt) {
				this.boost += 0.10000000149011612D;
			}

			if (this.boost > 0.7D) {
				this.boost = 0.699999988079071D;
			}

			if (this.boost > 0.2D) {
				this.move(this.mc.thePlayer.rotationYaw, (float) this.boost);
				this.boost /= 1.0199999809265137D;
			}

			this.lastY = this.mc.thePlayer.posY;
			if (doIt && this.mc.gameSettings.keyBindJump.pressed) {
				this.mc.gameSettings.keyBindJump.pressed = false;
				this.hasJumped = true;
			}
			if (this.hasJumped) {
				if (!this.mc.gameSettings.keyBindForward.pressed || !this.mc.thePlayer.onGround) {
					this.boost = 0.2D;
					this.lastY = this.mc.thePlayer.posY;
					return;
				}

				if (this.lastY < this.mc.thePlayer.posY && this.mc.thePlayer.motionY <= 0.0D) {
					this.boost += 0.10000000149011612D;
				}

				if (this.boost > 0.7D) {
					this.boost = 0.699999988079071D;
				}

				if (this.boost > 0.2D) {
					this.move(this.mc.thePlayer.rotationYaw, (float) this.boost);
					this.boost /= 1.0199999809265137D;
				}
				this.lastY = this.mc.thePlayer.posY;
			}
		} else {
			this.boost = 0.10000000149011612D;
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
		this.boost = 0.10000000149011612D;
		this.lastY = this.mc.thePlayer.posY;
		super.onDisable();
	}
}
