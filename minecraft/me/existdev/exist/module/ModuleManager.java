package me.existdev.exist.module;

import java.util.ArrayList;
import java.util.List;

import me.existdev.exist.module.Module.Category;
import me.existdev.exist.module.modules.combat.AntiBot;
import me.existdev.exist.module.modules.combat.AutoArmor;
import me.existdev.exist.module.modules.combat.BowAimbot;
import me.existdev.exist.module.modules.combat.ChestSteal;
import me.existdev.exist.module.modules.combat.KillAura;
import me.existdev.exist.module.modules.combat.Velocity;
import me.existdev.exist.module.modules.movement.FastLadder;
import me.existdev.exist.module.modules.movement.FastStair;
import me.existdev.exist.module.modules.movement.Flight;
import me.existdev.exist.module.modules.movement.Glide;
import me.existdev.exist.module.modules.movement.InvMove;
import me.existdev.exist.module.modules.movement.Jesus;
import me.existdev.exist.module.modules.movement.Longjump;
import me.existdev.exist.module.modules.movement.NoSlowdown;
import me.existdev.exist.module.modules.movement.SlimeJump;
import me.existdev.exist.module.modules.movement.Speed;
import me.existdev.exist.module.modules.movement.Spider;
import me.existdev.exist.module.modules.movement.Sprint;
import me.existdev.exist.module.modules.movement.Step;
import me.existdev.exist.module.modules.movement.WallGlide;
import me.existdev.exist.module.modules.movement.WaterSpeed;
import me.existdev.exist.module.modules.movement.WebWalk;
import me.existdev.exist.module.modules.player.AntiCactus;
import me.existdev.exist.module.modules.player.FastEat;
import me.existdev.exist.module.modules.player.InvCleaner;
import me.existdev.exist.module.modules.player.NoFall;
import me.existdev.exist.module.modules.player.NoHurtTime;
import me.existdev.exist.module.modules.render.BlockOverlay;
import me.existdev.exist.module.modules.render.Chams;
import me.existdev.exist.module.modules.render.ChestESP;
import me.existdev.exist.module.modules.render.ESP;
import me.existdev.exist.module.modules.render.Fullbright;
import me.existdev.exist.module.modules.render.HUD;
import me.existdev.exist.module.modules.render.NoBob;
import me.existdev.exist.module.modules.render.NoFov;
import me.existdev.exist.module.modules.world.BedFucker;
import me.existdev.exist.module.modules.world.Scaffold;

public class ModuleManager {

	private static ArrayList<Module> modules = new ArrayList<>();

	public ModuleManager() {
		registerModules();
	}

	public void registerModules() {
		addModule(new KillAura());
		addModule(new AntiBot());
		addModule(new AutoArmor());
		addModule(new ChestSteal());
		addModule(new Velocity());
		addModule(new FastLadder());
		addModule(new Flight());
		addModule(new Glide());
		addModule(new Jesus());
		addModule(new Longjump());
		addModule(new NoSlowdown());
		addModule(new WebWalk());
		addModule(new Speed());
		addModule(new InvMove());
		addModule(new InvCleaner());
		addModule(new NoFall());
		addModule(new ESP());
		addModule(new ChestESP());
		addModule(new Scaffold());
		addModule(new Sprint());
		addModule(new Fullbright());
		addModule(new FastStair());
		addModule(new Step());
		addModule(new BedFucker());
		addModule(new FastEat());
		addModule(new HUD());
		addModule(new WaterSpeed());
		addModule(new NoBob());
		addModule(new AntiCactus());
		addModule(new Chams());
		addModule(new Spider());
		addModule(new SlimeJump());
		addModule(new BlockOverlay());
		addModule(new WallGlide());
		addModule(new NoHurtTime());
		addModule(new NoFov());
		addModule(new BowAimbot());
	}

	public void addModule(Module m) {
		modules.add(m);
	}

	public static ArrayList<Module> getModules() {
		return modules;
	}

	public static ArrayList<Module> getToggledModules() {
		ArrayList<Module> modules = new ArrayList<Module>();
		for (Module m : modules) {
			if (m.isToggled())
				modules.add(m);
		}
		return modules;
	}

	public static List<Module> getModulesInCategory(Category cat) {
		List<Module> modules = new ArrayList();
		for (Module m : modules) {
			if (m.getCategory() == cat) {
				modules.add(m);
			}
		}
		return modules;
	}

	public static Module getModuleByName(String name) {
		for (Module m : modules) {
			if (!m.getName().equalsIgnoreCase(name)) {
				continue;
			}
			return m;
		}
		return null;
	}

	public static Module getModule(Class<? extends Module> clazz) {
		for (Module mod : modules) {
			if (mod.getClass() == clazz) {
				return mod;
			}
		}
		return null;
	}
}
