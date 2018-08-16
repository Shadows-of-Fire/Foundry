package exter.foundry.integration;

import cofh.thermalfoundation.init.TFFluids;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

//This is outdated and will not be updated until EnderIO becomes available on a maven.
@Deprecated
public class ModIntegrationEnderIO implements IModIntegration {

	private Fluid redstoneAlloy;
	private Fluid energeticAlloy;
	private Fluid vibrantAlloy;
	private Fluid darkSteel;
	private Fluid electricalSteel;
	private Fluid phasedIron;
	private Fluid soularium;

	@Override
	public String getModID() {
		return ModIntegrationManager.ENDERIO;
	}

	@Override
	public void preInit() {
		redstoneAlloy = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("RedstoneAlloy", 1000, 14);
		energeticAlloy = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("EnergeticAlloy", 2500, 15);
		vibrantAlloy = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("VibrantAlloy", 2500, 15);
		darkSteel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("DarkSteel", 1850, 12);
		electricalSteel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("ElectricalSteel", 1850, 15);
		phasedIron = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("PulsatingIron", 1850, 15);
		soularium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("Soularium", 1350, 12);

		FoundryUtils.registerBasicMeltingRecipes("RedstoneAlloy", redstoneAlloy);
		FoundryUtils.registerBasicMeltingRecipes("EnergeticAlloy", energeticAlloy);
		FoundryUtils.registerBasicMeltingRecipes("VibrantAlloy", vibrantAlloy);
		FoundryUtils.registerBasicMeltingRecipes("PhasedGold", vibrantAlloy);
		FoundryUtils.registerBasicMeltingRecipes("DarkSteel", darkSteel);
		FoundryUtils.registerBasicMeltingRecipes("PulsatingIron", phasedIron);
		FoundryUtils.registerBasicMeltingRecipes("ElectricalSteel", electricalSteel);
		FoundryUtils.registerBasicMeltingRecipes("Soularium", soularium);
	}

	@Override
	public void postInitEarly() {
		if (FoundryConfig.recipe_equipment) {
			OreMatcher extra_sticks1 = new OreMatcher("stickWood", 1);
			OreMatcher extra_sticks2 = new OreMatcher("stickWood", 2);

			ItemStack dark_steel_pickaxe = getItemStack("item_dark_steel_pickaxe");
			ItemStack dark_steel_axe = getItemStack("item_dark_steel_axe");
			ItemStack dark_steel_sword = getItemStack("item_dark_steel_sword");

			ItemStack dark_steel_helmet = getItemStack("item_dark_steel_helmet");
			ItemStack dark_steel_chestplate = getItemStack("item_dark_steel_chestplate");
			ItemStack dark_steel_leggings = getItemStack("item_dark_steel_leggings");
			ItemStack dark_steel_boots = getItemStack("item_dark_steel_boots");

			FoundryMiscUtils.registerCasting(dark_steel_chestplate, darkSteel, 8, ItemMold.SubItem.CHESTPLATE, null);
			FoundryMiscUtils.registerCasting(dark_steel_helmet, darkSteel, 5, ItemMold.SubItem.HELMET, null);
			FoundryMiscUtils.registerCasting(dark_steel_leggings, darkSteel, 7, ItemMold.SubItem.LEGGINGS, null);
			FoundryMiscUtils.registerCasting(dark_steel_boots, darkSteel, 4, ItemMold.SubItem.BOOTS, null);

			FoundryMiscUtils.registerCasting(dark_steel_pickaxe, darkSteel, 3, ItemMold.SubItem.PICKAXE, extra_sticks2);
			FoundryMiscUtils.registerCasting(dark_steel_axe, darkSteel, 3, ItemMold.SubItem.AXE, extra_sticks2);
			FoundryMiscUtils.registerCasting(dark_steel_sword, darkSteel, 2, ItemMold.SubItem.SWORD, extra_sticks1);

		}
		ItemStack silicon = getItemStack("item_material", 5);

		Fluid redstone = TFFluids.fluidRedstone;
		Fluid enderpearl = TFFluids.fluidEnder;
		Fluid glowstone = TFFluids.fluidGlowstone;

		if (silicon != null) {
			InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(redstoneAlloy, 108), new FluidStack(redstone, 100), new ItemStackMatcher(silicon), 50000);

			InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(electricalSteel, 108), new FluidStack(FoundryFluids.liquid_steel, 108), new ItemStackMatcher(silicon), 30000);
		}

		AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(energeticAlloy, 54), new FluidStack[] { new FluidStack(FoundryFluids.liquid_gold, 54), new FluidStack(redstone, 50), new FluidStack(glowstone, 125) });

		AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(vibrantAlloy, 54), new FluidStack[] { new FluidStack(energeticAlloy, 54), new FluidStack(enderpearl, 125) });

		AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(phasedIron, 54), new FluidStack[] { new FluidStack(FoundryFluids.liquid_iron, 54), new FluidStack(enderpearl, 125) });

		AlloyMixerRecipeManager.INSTANCE.addRecipe(new FluidStack(darkSteel, 27), new FluidStack[] { new FluidStack(FoundryFluids.liquid_steel, 27), new FluidStack(FluidRegistry.LAVA, 250), });

		InfuserRecipeManager.INSTANCE.addRecipe(new FluidStack(soularium, 108), new FluidStack(FoundryFluids.liquid_gold, 108), new ItemStackMatcher(new ItemStack(Blocks.SOUL_SAND)), 50000);
	}
}
