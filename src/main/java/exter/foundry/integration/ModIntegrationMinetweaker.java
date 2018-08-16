package exter.foundry.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.CraftTweaker;
import exter.foundry.integration.minetweaker.MTAlloyFurnaceHandler;
import exter.foundry.integration.minetweaker.MTAlloyMixerHandler;
import exter.foundry.integration.minetweaker.MTAlloyingCrucibleHandler;
import exter.foundry.integration.minetweaker.MTAtomizerHandler;
import exter.foundry.integration.minetweaker.MTBurnerFuelHandler;
import exter.foundry.integration.minetweaker.MTCastingHandler;
import exter.foundry.integration.minetweaker.MTCastingTableHandler;
import exter.foundry.integration.minetweaker.MTInfuserHandler;
import exter.foundry.integration.minetweaker.MTMeltingHandler;
import exter.foundry.integration.minetweaker.MTMoldStationHandler;
import exter.foundry.integration.minetweaker.orestack.OreStackBracketHandler;

public class ModIntegrationMinetweaker implements IModIntegration {

	private static List<Runnable> addQueue = new ArrayList<>();
	private static List<Runnable> removeQueue = new ArrayList<>();
	private static List<Runnable> clearQueue = new ArrayList<>();

	@Override
	public String getModID() {
		return CraftTweaker.MODID;
	}

	@Override
	public void preInit() {
		CraftTweakerAPI.registerBracketHandler(new OreStackBracketHandler());
		CraftTweakerAPI.registerClass(MTMeltingHandler.class);
		CraftTweakerAPI.registerClass(MTCastingHandler.class);
		CraftTweakerAPI.registerClass(MTCastingTableHandler.class);
		CraftTweakerAPI.registerClass(MTAlloyMixerHandler.class);
		CraftTweakerAPI.registerClass(MTAlloyFurnaceHandler.class);
		CraftTweakerAPI.registerClass(MTAlloyingCrucibleHandler.class);
		CraftTweakerAPI.registerClass(MTAtomizerHandler.class);
		CraftTweakerAPI.registerClass(MTInfuserHandler.class);
		CraftTweakerAPI.registerClass(MTMoldStationHandler.class);
		CraftTweakerAPI.registerClass(MTBurnerFuelHandler.class);
	}

	@Override
	public void postInit() {
		for (Runnable r : clearQueue)
			r.run();
		for (Runnable r : removeQueue)
			r.run();
		for (Runnable r : addQueue)
			r.run();
		addQueue = removeQueue = clearQueue = null;
	}

	public static void queueAdd(Runnable action) {
		addQueue.add(action);
	}

	public static void queueRemove(Runnable action) {
		removeQueue.add(action);
	}

	public static void queueClear(Collection<?> recipes) {
		clearQueue.add(() -> recipes.clear());
	}

	public static void queueClear(Runnable run) {
		clearQueue.add(run);
	}
}
