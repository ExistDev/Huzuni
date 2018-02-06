package me.existdev.exist.font;

import java.awt.Font;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class NormalFontRenderer {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static final NormalFontManager font70 = new NormalFontManager(getFont(70), true, 8);
	public static final NormalFontManager font60 = new NormalFontManager(getFont(60), true, 8);
	public static final NormalFontManager font50 = new NormalFontManager(getFont(50), true, 8);
	public static final NormalFontManager font40 = new NormalFontManager(getFont(40), true, 8);
	public static final NormalFontManager font30 = new NormalFontManager(getFont(30), true, 8);
	public static final NormalFontManager font20 = new NormalFontManager(getFont(20), true, 8);
	public static final NormalFontManager font10 = new NormalFontManager(getFont(10), true, 8);
	public static final NormalFontManager fontArrayList = new NormalFontManager(getFont(35), true, 8);
	public static final NormalFontManager fontTabGUI = new NormalFontManager(getFont(35), true, 8);
	public static final NormalFontManager fontChat = new NormalFontManager(getFont(34), true, 8);
	public static final NormalFontManager fontTextField = new NormalFontManager(getFont(34), true, 8);
	public static final NormalFontManager fontClickGUI = new NormalFontManager(getFont(34), true, 8);

	private static Font getFont(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Exist/Font.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		return font;
	}
	
}
