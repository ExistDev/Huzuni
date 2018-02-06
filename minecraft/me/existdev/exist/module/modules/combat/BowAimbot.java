package me.existdev.exist.module.modules.combat;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.BlickWinkel3D;
import me.existdev.exist.utils.Location3D;
import me.existdev.exist.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BowAimbot extends Module {

	public BowAimbot() {
		super("BowAimbot", 0, Category.Combat);
		Exist.settingManager.addSetting(new Setting(this, "Silent", true));
		Exist.settingManager.addSetting(new Setting(this, "Fov", 5.0D, 360.0D, 5.0D, true));
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		EntityPlayer target = this.getNearest();
		if (target != null) {
			Location3D from = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D,
					this.mc.thePlayer.posZ);
			Location3D toNormal = new Location3D(target.posX, target.posY + 1.0D, target.posZ);
			BlickWinkel3D bl = new BlickWinkel3D(from, toNormal);
			float yaw = (float) Math
					.abs((double) MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw) - bl.getYaw());
			if ((double) yaw <= Exist.settingManager.getSetting(this, "Fov").getCurrentValue()) {
				if (this.mc.thePlayer.getCurrentEquippedItem() != null
						&& this.mc.thePlayer.getCurrentEquippedItem().getItem() == Items.bow) {
					if (this.mc.thePlayer.getItemInUseDuration() >= 5) {
						this.updateLook(target);
						super.onUpdate();
					}
				}
			}
		}
	}

	public void updateLook(Entity target) {
		Location3D from = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.6D, this.mc.thePlayer.posZ);
		Location3D toNormal = new Location3D(target.posX, target.posY + 1.0D, target.posZ);
		double movesAmount = from.distance(toNormal) + 8.0D;
		movesAmount /= 3.0D;
		double xOffSet = (target.posX - target.prevPosX) * movesAmount;
		double zOffSet = (target.posZ - target.prevPosZ) * movesAmount;
		Location3D to = new Location3D(toNormal.getX() + xOffSet, toNormal.getY(), toNormal.getZ() + zOffSet);
		BlickWinkel3D bl = new BlickWinkel3D(from, to);
		double bowUse = (double) this.mc.thePlayer.getItemInUseDuration();
		if (bowUse >= 20.0D) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, this.mc.thePlayer.getPosition(), EnumFacing.DOWN));
		}

		double pitchOffSet = (Math.abs(bl.getPitch()) - 90.0D) * from.distance(to) / (bowUse * 10.0D) / 2.5D;
		if (bl.getPitch() >= -90.0D && bl.getPitch() <= 90.0D && bl.getYaw() <= 999.0D && bl.getYaw() >= -999.0D) {
			if (Exist.settingManager.getSetting(this, "Silent").getBooleanValue()) {
				RotationUtils.server_yaw = (float) bl.getYaw();
				RotationUtils.server_pitch = (float) (bl.getPitch() + pitchOffSet);
			} else {
				this.mc.thePlayer.rotationYaw = (float) bl.getYaw();
				this.mc.thePlayer.rotationPitch = (float) (bl.getPitch() + pitchOffSet);
			}
		}
	}

	private EntityPlayer getNearest() {
		Location3D p = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
		EntityPlayer best = null;
		double distance = 50.0D;

		for (int i = 0; i < this.mc.theWorld.playerEntities.size(); ++i) {
			EntityPlayer ep = (EntityPlayer) this.mc.theWorld.playerEntities.get(i);
			if (!ep.equals(this.mc.thePlayer) && mc.thePlayer.canEntityBeSeen(ep)) {
				Location3D epl = new Location3D(ep.posX, ep.posY, ep.posZ);
				double newDis = epl.distance(p);
				if (newDis <= distance) {
					distance = newDis;
					best = ep;
				}
			}
		}
		return best;
	}
}
