package me.existdev.exist.gui;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventKeyboard;
import me.existdev.exist.events.EventRender2D;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.Module.Category;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class TabGUI {

	private ArrayList<Category> categoryValues;
	private int currentCategoryIndex, currentModIndex, currentSettingIndex;
	private boolean editMode;
	int animation = 30;
	Minecraft mc = Minecraft.getMinecraft();

	private int screen;

	private int blackRectColor = 0x96000000;

	public TabGUI() {
		this.categoryValues = new ArrayList();
		this.currentCategoryIndex = 0;
		this.currentModIndex = 0;
		this.currentSettingIndex = 0;
		this.editMode = false;
		this.screen = 0;
		for (Category c : Module.Category.values()) {
			this.categoryValues.add(c);
		}
		this.categoryValues.remove(Category.Gui);
	}

	@EventTarget
	public void renderTabGUI(EventRender2D e) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int startX = 2;
		int startY = (5 + 9) + 35;
		int blackRectColor = 0xFF313131;
		RenderHelper.drawRect(startX, startY - 1, startX + this.getWidestCategory() + 15,
				startY + this.categoryValues.size() * (9 + 2) + 15, blackRectColor);
		for (Category c : this.categoryValues) {
			if (this.getCurrentCategorry().equals(c)) {
				RenderHelper.drawRect(startX, startY - 1, startX + this.getWidestCategory() + 15, startY + 14,
						0xFFFFFFFF);
			}

			String name = c.name();
			Exist.normalFontRenderer.fontTabGUI.drawStringWithShadow(name, startX + 4, startY + 1, 0xFF939393);
			startY += 9 + 5;
		}

		if (screen == 1 || screen == 2) {
			int startModsX = startX + this.getWidestCategory() + 16;
			int startModsY = ((5 + 9) + 11) + currentCategoryIndex * (9 + 2) + 24;
			RenderHelper.drawRect(startModsX, startModsY - 1, startModsX + this.getWidestModule() + 15, startModsY
					+ this.getModsForCurrentCategory().size() * (Exist.normalFontRenderer.fontTabGUI.FONT_HEIGHT + 3),
					blackRectColor);
			for (Module m : getModsForCurrentCategory()) {
				if (this.getCurrentModule().equals(m)) {
					RenderHelper.drawRect(startModsX, startModsY - 1, startModsX + this.getWidestModule() + 15,
							startModsY + 9 + 3, 0xFFFFFFFF);
				}
				Exist.normalFontRenderer.fontTabGUI.drawStringWithShadow(m.getName(),
						startModsX + 2 + (this.getCurrentModule().equals(m) ? 4 : 2), startModsY, 0xFF939393);
				Exist.normalFontRenderer.fontTabGUI.drawStringWithShadow(
						Exist.settingManager.getSettingsForModule(m) != null ? ">" : "",
						startModsX + this.getWidestModule() + 8, startModsY, 0xFF939393);

				RenderHelper.drawRect(startModsX + this.getWidestModule() + 15, startModsY - 1,
						startModsX + this.getWidestModule() + 16, startModsY + 11,
						m.isToggled() ? 0xFFFFFFFF : blackRectColor);
				startModsY += Exist.normalFontRenderer.fontTabGUI.FONT_HEIGHT + 3;
			}
		}

		if (screen != 2) {
			editMode = false;
		}
		if (screen == 2) {
			int startSettingX = (startX + this.getWidestCategory() + 6) + this.getWidestCategory() + 8;
			int startSettingY = ((5 + 9) + 4) + (currentCategoryIndex * (9 + 4)) + currentModIndex * (9 + 4) + 29;
			int addx = 31;

			RenderHelper.drawRect(startSettingX + addx, startSettingY,
					startSettingX + addx + this.getWidestSetting() + 12,
					startSettingY + this.getSettingForCurrentMod().size() * (9 + 3), blackRectColor);
			for (Setting s : this.getSettingForCurrentMod()) {

				if (this.getCurrentSetting().equals(s)) {
					RenderHelper.drawRect(startSettingX + addx, startSettingY,
							startSettingX + this.getWidestSetting() + 12 + addx, startSettingY + 12, 0xFFFFFFFF);
				}
				if (s.isBoolean()) {
					Exist.normalFontRenderer.fontTabGUI
							.drawStringWithShadow(
									editMode && this.getCurrentSetting().equals(s)
											? s.getName() + ChatFormatting.DARK_GRAY + ": " + s.getBooleanValue()
											: s.getName() + ChatFormatting.GRAY + ": " + s.getBooleanValue(),
									startSettingX + 2 + addx + 5, startSettingY + 1, 0xFF939393);
				} else if (s.isDigit()) {
					Exist.normalFontRenderer.fontTabGUI
							.drawStringWithShadow(
									editMode && this.getCurrentSetting().equals(s)
											? s.getName() + ChatFormatting.DARK_GRAY + ": " + s.getCurrentValue()
											: s.getName() + ChatFormatting.GRAY + ": " + s.getCurrentValue(),
									startSettingX + 2 + addx + 5, startSettingY + 1, 0xFF939393);
				} else {
					Exist.normalFontRenderer.fontTabGUI
							.drawStringWithShadow(
									editMode && this.getCurrentSetting().equals(s)
											? s.getName() + ChatFormatting.DARK_GRAY + ": " + s.getCurrentOption()
											: s.getName() + ChatFormatting.GRAY + ": " + s.getCurrentOption(),
									startSettingX + 2 + addx + 5, startSettingY + 1, 0xFF939393);
				}
				startSettingY += 9 + 3;
			}
		}
	}

	private void up() {
		if (this.currentCategoryIndex > 0 && this.screen == 0) {
			this.currentCategoryIndex--;
		} else if (this.currentCategoryIndex == 0 && this.screen == 0) {
			this.currentCategoryIndex = this.categoryValues.size() - 1;
		}

		else if (this.currentModIndex > 0 && this.screen == 1) {
			this.currentModIndex--;
		} else if (this.currentModIndex == 0 && this.screen == 1) {
			this.currentModIndex = this.getModsForCurrentCategory().size() - 1;
		}

		else if (this.currentSettingIndex > 0 && this.screen == 2 && !this.editMode) {
			this.currentSettingIndex--;
		} else if (this.currentSettingIndex == 0 && this.screen == 2 && !this.editMode) {
			this.currentSettingIndex = this.getSettingForCurrentMod().size() - 1;
		}

		if (editMode) {
			Setting s = this.getCurrentSetting();
			if (s.isBoolean()) {
				s.setBooleanValue(!s.getBooleanValue());
			} else if (s.isDigit()) {
				if (s.isOnlyInt()) {
					s.setCurrentValue(s.getCurrentValue() + 1);
				} else {
					s.setCurrentValue(s.getCurrentValue() + 0.100);
				}

			} else {
				try {
					s.setCurrentOption(s.getOptions().get(s.getCurrentOptionIndex() - 1));
				} catch (Exception e) {
					s.setCurrentOption(s.getOptions().get(s.getOptions().size() - 1));
				}

			}
		}

	}

	private void down() {
		if (this.currentCategoryIndex < this.categoryValues.size() - 1 && this.screen == 0) {
			this.currentCategoryIndex++;
		} else if (this.currentCategoryIndex == this.categoryValues.size() - 1 && this.screen == 0) {
			this.currentCategoryIndex = 0;
		}

		else if (this.currentModIndex < this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
			this.currentModIndex++;
		} else if (this.currentModIndex == this.getModsForCurrentCategory().size() - 1 && this.screen == 1) {
			this.currentModIndex = 0;
		}

		else if (this.currentSettingIndex < this.getSettingForCurrentMod().size() - 1 && this.screen == 2
				&& !this.editMode) {
			this.currentSettingIndex++;
		} else if (this.currentSettingIndex == this.getSettingForCurrentMod().size() - 1 && this.screen == 2
				&& !this.editMode) {
			this.currentSettingIndex = 0;
		}

		if (editMode) {
			Setting s = this.getCurrentSetting();
			if (s.isBoolean()) {
				s.setBooleanValue(!s.getBooleanValue());
			} else if (s.isDigit()) {
				if (s.isOnlyInt()) {
					BigDecimal bd = new BigDecimal(s.getCurrentValue());
					BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
					s.setCurrentValue(s.getCurrentValue() - 1);
				} else {
					s.setCurrentValue(s.getCurrentValue() - 0.1);
					BigDecimal bd = new BigDecimal(s.getCurrentValue());
					BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

				}

			} else {
				try {
					s.setCurrentOption(s.getOptions().get(s.getCurrentOptionIndex() + 1));
				} catch (Exception e) {
					s.setCurrentOption(s.getOptions().get(0));
				}
			}
		}
	}

	private void right(int key) {
		if (this.screen == 0) {
			this.screen = 1;
		} else if (this.screen == 1 && this.getCurrentModule() != null && this.getSettingForCurrentMod() == null) {
			this.getCurrentModule().toggle();
		} else if (this.screen == 1 && this.getSettingForCurrentMod() != null && this.getCurrentModule() != null
				&& key == Keyboard.KEY_RETURN) {
			this.getCurrentModule().toggle();
		} else if (this.screen == 1 && this.getSettingForCurrentMod() != null && this.getCurrentModule() != null) {
			this.screen = 2;
		} else if (this.screen == 2) {
			this.editMode = !this.editMode;
		}

	}

	private void left() {
		if (this.screen == 1) {
			this.screen = 0;
			this.currentModIndex = 0;
		} else if (this.screen == 2) {
			this.screen = 1;
			this.currentSettingIndex = 0;
		}

	}

	@EventTarget
	public void onKey(EventKeyboard e) {
		switch (e.getKey()) {
		case Keyboard.KEY_UP:
			this.up();
			break;
		case Keyboard.KEY_DOWN:
			this.down();
			break;
		case Keyboard.KEY_RIGHT:
			this.right(Keyboard.KEY_RIGHT);
			break;
		case Keyboard.KEY_LEFT:
			this.left();
			break;
		case Keyboard.KEY_RETURN:
			this.right(Keyboard.KEY_RETURN);
			break;
		}
	}

	private Setting getCurrentSetting() {
		return getSettingForCurrentMod().get(currentSettingIndex);

	}

	private ArrayList<Setting> getSettingForCurrentMod() {
		return Exist.settingManager.getSettingsForModule(getCurrentModule());
	}

	private Category getCurrentCategorry() {
		return this.categoryValues.get(this.currentCategoryIndex);
	}

	private int getWidestCategory() {
		int width = 0;
		for (Category c : this.categoryValues) {
			String name = c.name();
			int cWidth = Exist.normalFontRenderer.fontTabGUI.getStringWidth(
					name.substring(0, 1).toUpperCase() + name.substring(1, name.length()).toLowerCase());
			if (cWidth > width) {
				width = cWidth;
			}
		}
		return width;
	}

	private ArrayList<Module> getModsForCurrentCategory() {
		ArrayList<Module> mods = new ArrayList();
		mods.sort((o1, o2) -> Exist.normalFontRenderer.fontTabGUI.getStringWidth(o2.getName())
				- Exist.normalFontRenderer.fontTabGUI.getStringWidth(o1.getName()));
		Category c = getCurrentCategorry();
		for (Module m : Exist.moduleManager.getModules()) {
			if (m.getCategory().equals(c)) {
				mods.add(m);
			}
		}
		return mods;
	}

	private int getWidestModule() {
		int width = 0;
		for (Module m : Exist.moduleManager.getModules()) {
			int cWidth = Exist.normalFontRenderer.fontTabGUI.getStringWidth(m.getName());
			if (cWidth > width) {
				width = cWidth;
			}
		}
		return width;
	}

	private Module getCurrentModule() {
		return getModsForCurrentCategory().get(currentModIndex);
	}

	private int getWidestSetting() {
		int width = 0;
		for (Setting s : getSettingForCurrentMod()) {
			String name;
			if (s.isBoolean()) {
				name = s.getName() + ": " + s.getBooleanValue();

			} else if (s.isDigit()) {
				name = s.getName() + ": " + s.getCurrentValue();
			} else {
				name = s.getName() + ": " + s.getCurrentOption();
			}
			if (Exist.normalFontRenderer.fontTabGUI.getStringWidth(name) > width) {
				width = Exist.normalFontRenderer.fontTabGUI.getStringWidth(name);
			}
		}
		return width;
	}
}