package me.existdev.exist.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class RotationUtils {

	public static float server_yaw;
	public static float server_pitch;

	public static float getDistanceYaw() {
		return MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationYaw) - server_yaw;
	}

	public static float getDistancePitch() {
		return MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch) - server_pitch;
	}

	public static float[] aimAtLocation(double x, double y, double z, EnumFacing facing) {
		EntitySnowball temp = new EntitySnowball(Minecraft.getMinecraft().theWorld);
		temp.posX = x + 0.5D;
		temp.posY = y - 2.70352523530000001D;
		temp.posZ = z + 0.5D;
		temp.posX += (double) facing.getDirectionVec().getX() * 0.25D;
		temp.posY += (double) facing.getDirectionVec().getY() * 0.25D;
		temp.posZ += (double) facing.getDirectionVec().getZ() * 0.25D;
		return aimAtLocation(temp.posX, temp.posY, temp.posZ);
	}

	private static float[] aimAtLocation(double positionX, double positionY, double positionZ) {
		double x = positionX - Minecraft.getMinecraft().thePlayer.posX;
		double y = positionY - Minecraft.getMinecraft().thePlayer.posY;
		double z = positionZ - Minecraft.getMinecraft().thePlayer.posZ;
		double distance = (double) MathHelper.sqrt_double(x * x + z * z);
		return new float[] { (float) (Math.atan2(z, x) * 180.0D / 3.141592653589793D) - 90.0F,
				(float) (-(Math.atan2(y, distance) * 180.0D / 3.141592653589793D)) };
	}

}
