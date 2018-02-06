package me.existdev.exist.module.modules.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.BlockUtils;
import me.existdev.exist.utils.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {

	ArrayList<String> Modes = new ArrayList<>();
	private List<Block> invalid = Arrays.asList(new Block[] { Blocks.air, Blocks.water, Blocks.fire,
			Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.anvil, Blocks.enchanting_table,
			Blocks.chest, Blocks.ender_chest, Blocks.gravel });
	private Timer timer = new Timer();
	private BlockUtils blockData;

	public Scaffold() {
		super("Scaffold", 0, Category.World);
		Modes.add("AACFast");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "AACFast", Modes));
		Exist.settingManager.addSetting(new Setting(this, "Switch", true));
	}

	@EventTarget
	public void onPre(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		blockData = null;
		if (!mc.thePlayer.isSneaking()) {
			BlockPos blockBelow1 = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
			blockData = getBlockData(blockBelow1, invalid);
			if (blockData != null) {
				float yaw = RotationUtils.aimAtLocation((double) blockData.position.getX(),
						(double) blockData.position.getY(), (double) blockData.position.getZ(), blockData.face)[0];
				float pitch = RotationUtils.aimAtLocation((double) blockData.position.getX(),
						(double) blockData.position.getY(), (double) blockData.position.getZ(), blockData.face)[1];
				mc.thePlayer.rotationYawHead = yaw;
				mc.thePlayer.rotationPitchHead = pitch;
			}
		}
	}

	@EventTarget
	public void onPost(EventPreMotionUpdates e) {
		if (!this.isToggled()) {
			return;
		}
		if (blockData != null) {
			if (!(Exist.settingManager.getSetting(this, "Switch").getBooleanValue() ? this.getBlockAmount() != 0
					: (mc.thePlayer.getCurrentEquippedItem() != null
							&& mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)))
				return;
			if (timer.hasTimeElapsed(1000)) {
				mc.rightClickDelayTimer = 0;
				final String playerPos = new StringBuilder(String.valueOf(mc.thePlayer.posY)).toString();

				int heldItem = mc.thePlayer.inventory.currentItem;
				BlockPos bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
				for (int i = 0; i < 9; i++) {
					if (mc.thePlayer.inventory.getStackInSlot(i) != null
							&& mc.thePlayer.inventory.getStackInSlot(i).stackSize != 0
							&& mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock
							&& Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
						mc.thePlayer.sendQueue
								.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
						break;
					}
				}
				setSpeed(0.16F);
				if (this.shouldPlace(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) {
					if (mc.playerController.func_178890_a(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(),
							blockData.position, blockData.face,
							new Vec3(
									(double) blockData.position.getX() + rando05(0)
											+ blockData.face.getDirectionVec().getX() * rando05(1),
									(double) blockData.position.getY() + rando05(2)
											+ blockData.face.getDirectionVec().getY() * rando05(3),
									(double) blockData.position.getZ() + rando05(4)
											+ blockData.face.getDirectionVec().getZ() * rando05(5)))) {
						mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
						mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
								C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
					}
				}
				if (Exist.settingManager.getSetting(this, "Switch").getBooleanValue()) {
					mc.thePlayer.sendQueue
							.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = heldItem));
				}
			}
		}
	}

	private int getBlockAmount() {
		int n = 0;
		for (int i = 9; i < 45; ++i) {
			final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (stack != null && stack.getItem() instanceof ItemBlock
					&& ((ItemBlock) stack.getItem()).getBlock().isCollidable()) {
				n += stack.stackSize;
			}
		}
		return n;
	}

	public static float rando05(long seed) {
		seed = System.currentTimeMillis() + seed;
		return 0.30000000000f + (new Random(seed).nextInt(70000000) / 100000000.000000000000f) + 0.00000001458745f;
	}

	private BlockUtils getBlockData(BlockPos pos, List list) {
		return !list.contains(mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())
				? new BlockUtils(pos.add(0, -1, 0), EnumFacing.UP)
				: (!list.contains(mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())
						? new BlockUtils(pos.add(-1, 0, 0), EnumFacing.EAST)
						: (!list.contains(mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())
								? new BlockUtils(pos.add(1, 0, 0), EnumFacing.WEST)
								: (!list.contains(mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())
										? new BlockUtils(pos.add(0, 0, -1), EnumFacing.SOUTH)
										: (!list.contains(mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())
												? new BlockUtils(pos.add(0, 0, 1), EnumFacing.NORTH) : null))));
	}

	public boolean shouldPlace(double x, double y, double z) {
		BlockPos p1 = new BlockPos(x - 0.23999999463558197D, y - 0.5D, z - 0.23999999463558197D);
		BlockPos p2 = new BlockPos(x - 0.23999999463558197D, y - 0.5D, z + 0.23999999463558197D);
		BlockPos p3 = new BlockPos(x + 0.23999999463558197D, y - 0.5D, z + 0.23999999463558197D);
		BlockPos p4 = new BlockPos(x + 0.23999999463558197D, y - 0.5D, z - 0.23999999463558197D);
		return this.mc.thePlayer.worldObj.getBlockState(p1).getBlock() == Blocks.air
				&& this.mc.thePlayer.worldObj.getBlockState(p2).getBlock() == Blocks.air
				&& this.mc.thePlayer.worldObj.getBlockState(p3).getBlock() == Blocks.air
				&& this.mc.thePlayer.worldObj.getBlockState(p4).getBlock() == Blocks.air;
	}

	public void onEnable() {
		super.onEnable();
	}

	public void onDisable() {
		super.onDisable();
	}

	public final class Timer {
		private long time;

		public Timer() {
			this.time = System.nanoTime() / 1000000L;
		}

		public boolean hasTimeElapsed(final long time, final boolean reset) {
			if (this.time() >= time) {
				if (reset)
					this.reset();
				return true;
			}
			return false;
		}

		public boolean hasTimeElapsed(final long time) {
			if (this.time() >= time)
				return true;
			return false;
		}

		public boolean hasTicksElapsed(int ticks) {
			if (this.time() >= (1000 / ticks) - 50)
				return true;
			return false;
		}

		public boolean hasTicksElapsed(int time, int ticks) {
			if (this.time() >= (time / ticks) - 50)
				return true;
			return false;
		}

		public long time() {
			return System.nanoTime() / 1000000L - this.time;
		}

		public void reset() {
			this.time = System.nanoTime() / 1000000L;
		}
	}

}
