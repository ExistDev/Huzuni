package me.existdev.exist.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class InvCleaner extends Module {

	private TimeHelper timer = new TimeHelper();

	public InvCleaner() {
		super("InvCleaner", 0, Category.Player);
		Exist.settingManager.addSetting(new Setting(this, "OpenInv", true));
		Exist.settingManager.addSetting(new Setting(this, "Delay", 60.0D, 0.0D, 1000.0D, true));
	}

	public void onDisable() {
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates event) {
		if (!this.isToggled()) {
			return;
		}
		if (!(Exist.settingManager.getSetting(this, "OpenInv").getBooleanValue())
				|| this.mc.currentScreen instanceof GuiInventory) {
			InventoryPlayer invp = this.mc.thePlayer.inventory;

			for (int i = 9; i < 45; ++i) {
				ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (itemStack != null && this.shouldNotClean(itemStack, i) && this.timer.isDelayComplete(
						((Double) Exist.settingManager.getSetting(this, "Delay").getCurrentValue()).longValue())) {
					this.clean(i);
					this.timer.reset();
				}
			}
		}
	}

	private void clean(int i) {
		this.mc.playerController.windowClick(0, i, 0, 0, this.mc.thePlayer);
		this.mc.playerController.windowClick(0, -999, 0, 0, this.mc.thePlayer);
	}

	private boolean shouldClean(int i) {
		return true;
	}

	private boolean shouldNotClean(ItemStack item, int invId) {
		return !this.getTwice(261, item, invId) && !this.getTwice(346, item, invId)
				? (!(item.getItem() instanceof ItemAxe) && !(item.getItem() instanceof ItemSword)
						&& !(item.getItem() instanceof ItemArmor) ? false
								: ((item.getItem() instanceof ItemSword || item.getItem() instanceof ItemAxe)
										&& this.mc.thePlayer.getHeldItem() == item ? false
												: (this.needArmor(item) ? false : !this.needWeapon(item))))
				: true;
	}

	private boolean getTwice(int itemId, ItemStack item, int invId) {
		if (Item.getIdFromItem(item.getItem()) != itemId) {
			return false;
		} else {
			Object worst = null;
			boolean count = false;

			for (int i = 0; i < 45; ++i) {
				if (i != invId) {
					ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
					if (itemStack != null && Item.getIdFromItem(itemStack.getItem()) == itemId
							&& this.isBetterId(i, invId)) {
						return true;
					}
				}
			}
			return false;
		}
	}

	private boolean needWeapon(ItemStack item) {
		return item == this.getBestWeapon();
	}

	private boolean needArmor(ItemStack item) {
		String[] armorType = new String[] { "item.boots", "item.leggings", "item.chestplate", "item.helmet" };

		for (int i = 0; i < 4; ++i) {
			if (item == this.getBestArmor(armorType[i])) {
				return true;
			}
		}

		return false;
	}

	private ItemStack getBestWeapon() {
		ItemStack bestSword = null;
		int bestSwordId = -1;

		for (int i = 44; i > 8; --i) {
			ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null
					&& (itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemAxe)) {
				if (bestSword == null) {
					bestSword = itemStack;
					bestSwordId = i;
				} else if (this.isBetterWeapon(itemStack.getItem(), bestSword.getItem(), i, bestSwordId)) {
					bestSword = itemStack;
					bestSwordId = i;
				}
			}
		}

		return bestSword;
	}

	private ItemStack getBestArmor(String armorType) {
		ItemStack bestArmor = null;

		for (int i = 1; i < 45; ++i) {
			ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() instanceof ItemArmor
					&& itemStack.getItem().getUnlocalizedName().startsWith(armorType)) {
				if (bestArmor == null) {
					bestArmor = itemStack;
				} else if (this.isBetterArmor((ItemArmor) itemStack.getItem(), bestArmor.getItem())) {
					bestArmor = itemStack;
				}
			}
		}

		return bestArmor;
	}

	private boolean isBetterArmor(Item item1, Item item2) {
		if (item1 instanceof ItemArmor && item2 instanceof ItemArmor) {
			ItemArmor armor1 = (ItemArmor) item1;
			ItemArmor armor2 = (ItemArmor) item2;
			return armor1.damageReduceAmount > armor2.damageReduceAmount;
		} else {
			return false;
		}
	}

	private boolean isBetterWeapon(Item item1, Item item2, int id1, int id2) {
		if ((item1 instanceof ItemSword || item1 instanceof ItemAxe)
				&& (item2 instanceof ItemSword || item2 instanceof ItemAxe)) {
			float damageWeapon1 = item1 instanceof ItemSword ? ((ItemSword) item1).func_150931_i()
					: ((ItemAxe) item1).func_150931_i();
			float damageWeapon2 = item2 instanceof ItemSword ? ((ItemSword) item2).func_150931_i()
					: ((ItemAxe) item2).func_150931_i();
			return damageWeapon1 > damageWeapon2 ? true
					: (damageWeapon1 == damageWeapon2 ? this.isBetterId(id1, id2) : false);
		} else {
			return false;
		}
	}

	private boolean isBetterId(int id1, int id2) {
		return id1 >= 36 && id2 < 36 ? true
				: (id1 < 36 && id2 >= 36 ? false : (id1 >= 36 && id2 >= 36 ? id1 < id2 : true));
	}

	private Item getInventoryItem(int id) {
		return this.mc.thePlayer.inventoryContainer.getSlot(id).getStack() == null ? null
				: this.mc.thePlayer.inventoryContainer.getSlot(id).getStack().getItem();
	}

	public class TimeHelper {
		private long lastMs;

		public boolean isDelayComplete(long delay) {
			return System.currentTimeMillis() - this.lastMs > delay;
		}

		public void reset() {
			this.lastMs = System.currentTimeMillis();
		}

		public long getLastMs() {
			return this.lastMs;
		}

		public void setLastMs(int i) {
			this.lastMs = System.currentTimeMillis() + (long) i;
		}
	}
}
