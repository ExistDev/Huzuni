package me.existdev.exist.gui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.render.HUD;
import me.existdev.exist.utils.ColorUtils;
import me.existdev.exist.utils.PictureUtils;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class IngameGUI extends GuiIngame {

	Minecraft mc = Minecraft.getMinecraft();

	private int item = 0;
	private float xPos = 0;
	private static float xPos1 = 0;
	private double xOffset = 0;
	private double xOffset1 = 0;
	private int yOffset = 0;

	private int min = 100;
	private int max = 255;
	private int cur = 100;
	public boolean increasing = true;

	public IngameGUI(Minecraft mcIn) {
		super(mcIn);
	}

	@Override
	public void func_175180_a(float p_175180_1_) {
		super.func_175180_a(p_175180_1_);
		if (!Exist.moduleManager.getModule(HUD.class).isToggled()) {
			return;
		}
		renderArraylist();
		renderWaterMark();
		renderHotbar();
	}

	public void renderWaterMark() {
		Exist.normalFontRenderer.fontTabGUI.drawStringWithShadow("0.3", 35, 30, 0xFFFFFFFF);
		GL11.glPushMatrix();
		PictureUtils p = new PictureUtils(new ResourceLocation("Exist/Logo.png"), 1, 5, 40, 40,
				ColorUtils.getClientColor());

		p.draw();
		GL11.glPopMatrix();
	}

	public void renderHotbar() {
		try {
			ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			if (mc.currentScreen instanceof GuiChat) {
				if (xOffset < (float) (sr.getScaledWidth() / 2 - 89) + 22f - 27f) {
					xOffset += 5;
				}
				if (xOffset1 < sr.getScaledWidth_double() - (float) (sr.getScaledWidth() / 2 - 89) + 22f - 24.5f
						- 178f) {
					xOffset1 += 5;
				}
			} else {
				if (xOffset > 0) {
					xOffset -= 5;
				}
				if (xOffset1 > 0) {
					xOffset1 -= 5;
				}
				if (yOffset > 0) {
					yOffset--;
				}
			}
			RenderHelper.drawRect(0 + xOffset - 0.8, sr.getScaledHeight() - 23 - yOffset,
					(float) (sr.getScaledWidth() / 2 - 89) + 22f - 24.5f, sr.getScaledHeight() - yOffset, 0x30FFFFFF);
			RenderHelper.drawRect((float) (sr.getScaledWidth() / 2 - 89) + 22f - 24.5f,
					sr.getScaledHeight() - 23 - yOffset, sr.getScaledWidth_double() - xOffset1 + 1,
					sr.getScaledHeight() - yOffset, 0x40FFFFFF);
			item = mc.thePlayer.inventory.currentItem + 1;
			xPos = item * 20 + 1.5f;
			if (xPos1 < xPos) {
				this.xPos1 += 1.5;
			} else if (xPos1 > xPos) {
				this.xPos1 -= 1.5;
			} else {
			}
			if (xPos1 == xPos + 0.5 || xPos1 == xPos - 0.5) {
				xPos1 = xPos;
			}
			float selectionheight = 23;
			RenderHelper.drawRect((float) (sr.getScaledWidth() / 2 - 88) + xPos1 - 25.2f,
					sr.getScaledHeight() - selectionheight, (float) (sr.getScaledWidth() / 2 - 90) + xPos1 - 1,
					sr.getScaledHeight(), 0x90000000);
		} catch (Exception e) {
		}
	}

	public void renderArraylist() {
		Gui.drawRect(0, 0, 0, 0, 0);
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		this.getCurrentFade();
		int yCount = 2;
		int curMod = cur;
		boolean incMod = increasing;
		for (Module m : Exist.moduleManager.getModules()) {
			if (!m.isToggled()) {
				continue;
			}
			String name = m.getName();
			int posX = sr.getScaledWidth() - Exist.normalFontRenderer.fontArrayList.getStringWidth(name);
			Color k = new Color(ColorUtils.getClientColor().hashCode());
			int r = k.getRed() + min - curMod + 10;
			if (r < 50)
				r = 50;
			if (r > 255)
				r = 255;
			int g = k.getGreen() + min - curMod + 10;
			if (g < 50)
				g = 50;
			if (g > 255)
				g = 255;
			int b = k.getBlue() + min - curMod + 10;
			if (b < 50)
				b = 50;
			if (b > 255)
				b = 255;
			int cc = new Color(r, g, b).getRGB();

			int c = cc;
			RenderHelper.drawRect(posX - 4, yCount, sr.getScaledWidth(),
					yCount + Exist.normalFontRenderer.fontArrayList.FONT_HEIGHT + 2, 0x60000000);
			Exist.normalFontRenderer.fontArrayList.drawStringWithShadow(name, posX - 2, yCount,
					this.transparency(c, 1));
			curMod += incMod ? 17 : -17;
			if (curMod > max) {
				incMod = false;
				curMod = max;
			}
			if (curMod < min) {
				incMod = true;
				curMod = min;
			}
			yCount += 11.5;
		}
	}

	public static int transparency(int color, double alpha) {
		Color c = new Color(color);
		float r = ((float) 1f / 255f) * c.getRed();
		float g = ((float) 1f / 255f) * c.getGreen();
		float b = ((float) 1f / 255f) * c.getBlue();
		return new Color(r, g, b, (float) alpha).getRGB();
	}

	public int getCurrentFade() {
		cur += increasing ? 1 : -1;
		if (cur > max) {
			increasing = false;
			cur = max;
		}
		if (cur < min) {
			increasing = true;
			cur = min;
		}
		return new Color(0, 120, cur).getRGB();
	}
}
