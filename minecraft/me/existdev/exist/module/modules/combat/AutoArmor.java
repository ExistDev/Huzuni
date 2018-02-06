package me.existdev.exist.module.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class AutoArmor extends Module {

	private int[] itemHelmet = new int[] { 298, 302, 306, 310, 314 };
	private int[] itemChestplate = new int[] { 299, 303, 307, 311, 315 };
	private int[] itemLeggings = new int[] { 300, 304, 308, 312, 316 };
	private int[] itemBoots = new int[] { 301, 305, 309, 313, 317 };
	public static boolean openedInventory;

	private Map armorContains = new HashMap();
	private Timings wearTimings = new Timings();
	private Timings dropTimings = new Timings();
	private Timings waitTimings = new Timings();

	public AutoArmor() {
		super("AutoArmor", 0, Category.Combat);
		Exist.settingManager.addSetting(new Setting(this, "WearDelay", 150.5D, 1000.0D, 1.0D, false));
		Exist.settingManager.addSetting(new Setting(this, "DropDelay", 1.0D, 1000.0D, 1.0D, false));
		Exist.settingManager.addSetting(new Setting(this, "ContainsDelay", 1.0D, 2000.0D, 1.0D, false));
		Exist.settingManager.addSetting(new Setting(this, "OpenInv", false));
		Exist.settingManager.addSetting(new Setting(this, "NoMove", false));
		Exist.settingManager.addSetting(new Setting(this, "OpenInvBypass", false));
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (!Exist.settingManager.getSetting(this, "NoMove").getBooleanValue()
				|| this.mc.thePlayer.moveForward == 0.0F && this.mc.thePlayer.moveStrafing == 0.0F) {
			boolean wearArmor = Exist.settingManager.getSetting(this, "OpenInv").getBooleanValue()
					&& this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiInventory
					|| !Exist.settingManager.getSetting(this, "OpenInv").getBooleanValue()
					|| Exist.settingManager.getSetting(this, "OpenInvBypass").getBooleanValue();
			boolean finished = true;
			Iterator var4 = this.getArmors().iterator();

			while (true) {
				String armorNames;
				int currentSlot;
				int bestArmorSlot;
				do {
					if (!var4.hasNext()) {
						if (finished) {
							this.mc.thePlayer.sendQueue.addToSendQueue(
									new C0DPacketCloseWindow(this.mc.thePlayer.inventoryContainer.windowId));
							openedInventory = false;
						}

						return;
					}

					armorNames = (String) var4.next();
					finished = false;
					currentSlot = this.getSlotByName(armorNames);
					bestArmorSlot = this.getBestInInventory(armorNames);
					boolean shouldAdd = true;
					if (bestArmorSlot != -1) {
						shouldAdd = this.getValence(
								this.mc.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack()) > this
										.getValence(
												this.mc.thePlayer.inventoryContainer.getSlot(currentSlot).getStack());
					}

					if (shouldAdd && bestArmorSlot != -1 && !this.armorContains
							.containsKey(this.mc.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack())) {
						this.armorContains.put(this.mc.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack(),
								Long.valueOf(System.currentTimeMillis()));
					}
				} while (!wearArmor);

				if (bestArmorSlot != -1 && this.mc.thePlayer.inventoryContainer.getSlot(currentSlot).getHasStack()
						&& this.getValence(
								this.mc.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack()) < this
										.getValence(
												this.mc.thePlayer.inventoryContainer.getSlot(currentSlot).getStack())) {
					bestArmorSlot = -1;
				}

				if (this.wearTimings
						.hasReached((long) Exist.settingManager.getSetting(this, "WearDelay").getCurrentValue())
						&& bestArmorSlot != -1) {
					if (Exist.settingManager.getSetting(this, "OpenInvBypass").getBooleanValue()
							&& this.mc.currentScreen == null && !openedInventory) {
						this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer,
								C0BPacketEntityAction.Action.OPEN_INVENTORY));
						openedInventory = true;
					}

					if (this.armorContains
							.containsKey(this.mc.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack())
							&& System.currentTimeMillis() - ((Long) this.armorContains
									.get(this.mc.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack()))
											.longValue() >= (long) Exist.settingManager
													.getSetting(this, "ContainsDelay").getCurrentValue()) {
						this.putOnItem(currentSlot, bestArmorSlot);
						this.armorContains
								.remove(this.mc.thePlayer.inventoryContainer.getSlot(bestArmorSlot).getStack());
						this.wearTimings.resetTimings();
					}
				}

				Iterator var9 = this.findArmor(armorNames).iterator();

				while (var9.hasNext()) {
					int anotherArmors = ((Integer) var9.next()).intValue();
					boolean isOldBetter = false;
					if (currentSlot != -1) {
						isOldBetter = this.getValence(
								this.mc.thePlayer.inventoryContainer.getSlot(currentSlot).getStack()) >= this
										.getValence(
												this.mc.thePlayer.inventoryContainer.getSlot(anotherArmors).getStack());
					}

					if (isOldBetter) {
						finished = false;
						if (this.dropTimings.hasReached(
								(long) Exist.settingManager.getSetting(this, "DropDelay").getCurrentValue())) {
							this.dropOldArmor(anotherArmors);
							this.dropTimings.resetTimings();
						}
					}
				}
			}
		} else {
			if (openedInventory) {
				this.mc.thePlayer.sendQueue
						.addToSendQueue(new C0DPacketCloseWindow(this.mc.thePlayer.inventoryContainer.windowId));
				openedInventory = false;
			}
		}
		super.onUpdate();
	}

	private void putOnItem(int armorSlot, int slot) {
		if (armorSlot != -1 && this.mc.thePlayer.inventoryContainer.getSlot(armorSlot).getStack() != null) {
			this.dropOldArmor(armorSlot);
		}

		this.inventoryAction(slot);
	}

	private void dropOldArmor(int slot) {
		this.mc.thePlayer.inventoryContainer.slotClick(slot, 0, 4, this.mc.thePlayer);
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4,
				this.mc.thePlayer);
	}

	private void inventoryAction(int click) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, click, 1, 1,
				this.mc.thePlayer);
	}

	private List getArmors() {
		return Arrays.asList(new String[] { "helmet", "leggings", "chestplate", "boots" });
	}

	private int[] getIdsByName(String armorName) {
		switch (armorName.hashCode()) {
		case -1220934547:
			if (armorName.equals("helmet")) {
				return this.itemHelmet;
			}
			break;
		case 93922241:
			if (armorName.equals("boots")) {
				return this.itemBoots;
			}
			break;
		case 1069952181:
			if (armorName.equals("chestplate")) {
				return this.itemChestplate;
			}
			break;
		case 1735676010:
			if (armorName.equals("leggings")) {
				return this.itemLeggings;
			}
		}

		return new int[0];
	}

	private List findArmor(String armorName) {
		int[] itemIds = this.getIdsByName(armorName);
		ArrayList availableSlots = new ArrayList();

		for (int slots = 9; slots < this.mc.thePlayer.inventoryContainer.getInventory().size(); ++slots) {
			ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack();
			if (itemStack != null) {
				int itemId = Item.getIdFromItem(itemStack.getItem());
				int[] var10 = itemIds;
				int var9 = itemIds.length;

				for (int var8 = 0; var8 < var9; ++var8) {
					int ids = var10[var8];
					if (itemId == ids) {
						availableSlots.add(Integer.valueOf(slots));
					}
				}
			}
		}

		return availableSlots;
	}

	private int getBestInInventory(String armorName) {
		int slot = -1;
		Iterator var4 = this.findArmor(armorName).iterator();

		while (var4.hasNext()) {
			int slots = ((Integer) var4.next()).intValue();
			if (slot == -1) {
				slot = slots;
			}

			if (this.mc.thePlayer.inventoryContainer.getSlot(slots) != null
					&& this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack().getItem() instanceof ItemArmor
					&& this.getValence(this.mc.thePlayer.inventoryContainer.getSlot(slots).getStack()) > this
							.getValence(this.mc.thePlayer.inventoryContainer.getSlot(slot).getStack())) {
				slot = slots;
			}
		}

		return slot;
	}

	private int getSlotByName(String armorName) {
		byte id = -1;
		switch (armorName.hashCode()) {
		case -1220934547:
			if (armorName.equals("helmet")) {
				id = 5;
			}
			break;
		case 93922241:
			if (armorName.equals("boots")) {
				id = 8;
			}
			break;
		case 1069952181:
			if (armorName.equals("chestplate")) {
				id = 6;
			}
			break;
		case 1735676010:
			if (armorName.equals("leggings")) {
				id = 7;
			}
		}

		return id;
	}

	private int getValence(ItemStack itemStack) {
		int valence = 0;
		if (itemStack == null) {
			return 0;
		} else {
			if (itemStack.getItem() instanceof ItemArmor) {
				valence += ((ItemArmor) itemStack.getItem()).damageReduceAmount;
			}
			if (itemStack != null && itemStack.hasTagCompound()) {
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(0).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(1).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(2).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(3).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(4).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(5).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(6).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(7).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(8).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(9).getDouble("lvl");
				valence += (int) itemStack.getEnchantmentTagList().getCompoundTagAt(34).getDouble("lvl");
			}
			valence += itemStack.getMaxDamage() - itemStack.getItemDamage();
			return valence;
		}
	}

	public class Timings {
		private long lastMS = Minecraft.getSystemTime();

		public boolean hasReached(long ms) {
			return Math.abs(Minecraft.getSystemTime() - this.lastMS) >= ms;
		}

		public void resetTimings() {
			this.lastMS = Minecraft.getSystemTime();
		}
	}
}
