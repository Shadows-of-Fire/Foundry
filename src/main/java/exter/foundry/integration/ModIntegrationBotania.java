package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationBotania implements IModIntegration {

	private FluidLiquidMetal manasteel;
	private FluidLiquidMetal terrasteel;
	private FluidLiquidMetal elementium;

	@Override
	public String getModID() {
		return ModIntegrationManager.BOTANIA;
	}

	@Override
	public void preInit() {
		manasteel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("Manasteel", 1950, 15);
		terrasteel = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("Terrasteel", 2100, 15);
		elementium = LiquidMetalRegistry.INSTANCE.registerLiquidMetal("ElvenElementium", 2400, 15);

		FoundryUtils.registerBasicMeltingRecipes("Manasteel", manasteel);
		FoundryUtils.registerBasicMeltingRecipes("Terrasteel", terrasteel);
		FoundryUtils.registerBasicMeltingRecipes("ElvenElementium", elementium);
	}

	@Override
	public void postInit() {
		ItemStackMatcher manasteelBlock = new ItemStackMatcher(getItemStack("storage", 0));
		ItemStackMatcher terrasteelBlock = new ItemStackMatcher(getItemStack("storage", 1));
		ItemStackMatcher elementiumBlock = new ItemStackMatcher(getItemStack("storage", 2));
		ItemStack blockMold = FoundryItems.mold(ItemMold.SubItem.BLOCK);

		MeltingRecipeManager.INSTANCE.addRecipe(manasteelBlock, new FluidStack(manasteel, FoundryAPI.getAmountBlock()));
		MeltingRecipeManager.INSTANCE.addRecipe(terrasteelBlock, new FluidStack(terrasteel, FoundryAPI.getAmountBlock()));
		MeltingRecipeManager.INSTANCE.addRecipe(elementiumBlock, new FluidStack(elementium, FoundryAPI.getAmountBlock()));

		CastingRecipeManager.INSTANCE.addRecipe(manasteelBlock, new FluidStack(manasteel, FoundryAPI.getAmountBlock()), blockMold, null);
		CastingRecipeManager.INSTANCE.addRecipe(terrasteelBlock, new FluidStack(terrasteel, FoundryAPI.getAmountBlock()), blockMold, null);
		CastingRecipeManager.INSTANCE.addRecipe(elementiumBlock, new FluidStack(elementium, FoundryAPI.getAmountBlock()), blockMold, null);

		if (FoundryConfig.recipe_equipment) {
			ItemStack livingwoodTwig = getItemStack("manaresource", 3);
			ItemStack dreamwoodTwig = getItemStack("manaresource", 13);

			ItemStack livingwoodTwig2 = livingwoodTwig.copy();
			livingwoodTwig2.setCount(2);
			ItemStackMatcher oneLivingStick = new ItemStackMatcher(livingwoodTwig);
			ItemStackMatcher twoLivingStick = new ItemStackMatcher(livingwoodTwig2);

			ItemStack dreamwoodTwig2 = dreamwoodTwig.copy();
			dreamwoodTwig2.setCount(2);
			ItemStackMatcher oneDreamStick = new ItemStackMatcher(dreamwoodTwig);
			ItemStackMatcher twoDreamStick = new ItemStackMatcher(dreamwoodTwig2);

			FoundryMiscUtils.registerCasting(getItemStack("manasteelpick"), manasteel, 3, ItemMold.SubItem.PICKAXE, twoLivingStick);
			FoundryMiscUtils.registerCasting(getItemStack("manasteelaxe"), manasteel, 3, ItemMold.SubItem.AXE, twoLivingStick);
			FoundryMiscUtils.registerCasting(getItemStack("manasteelshovel"), manasteel, 1, ItemMold.SubItem.SHOVEL, twoLivingStick);
			FoundryMiscUtils.registerCasting(getItemStack("manasteelsword"), manasteel, 2, ItemMold.SubItem.SWORD, oneLivingStick);
			FoundryMiscUtils.registerCasting(getItemStack("manasteelchest"), manasteel, 8, ItemMold.SubItem.CHESTPLATE);
			FoundryMiscUtils.registerCasting(getItemStack("manasteellegs"), manasteel, 7, ItemMold.SubItem.LEGGINGS);
			FoundryMiscUtils.registerCasting(getItemStack("manasteelhelm"), manasteel, 5, ItemMold.SubItem.HELMET);
			FoundryMiscUtils.registerCasting(getItemStack("manasteelboots"), manasteel, 4, ItemMold.SubItem.BOOTS);

			FoundryMiscUtils.registerCasting(getItemStack("terrasword"), new FluidStack(terrasteel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), ItemMold.SubItem.SWORD, oneLivingStick);

			FoundryMiscUtils.registerCasting(getItemStack("elementiumpick"), elementium, 3, ItemMold.SubItem.PICKAXE, twoDreamStick);
			FoundryMiscUtils.registerCasting(getItemStack("elementiumaxe"), elementium, 3, ItemMold.SubItem.AXE, twoDreamStick);
			FoundryMiscUtils.registerCasting(getItemStack("elementiumshovel"), elementium, 1, ItemMold.SubItem.SHOVEL, twoDreamStick);
			FoundryMiscUtils.registerCasting(getItemStack("elementiumsword"), elementium, 2, ItemMold.SubItem.SWORD, oneDreamStick);
			FoundryMiscUtils.registerCasting(getItemStack("elementiumchest"), elementium, 8, ItemMold.SubItem.CHESTPLATE);
			FoundryMiscUtils.registerCasting(getItemStack("elementiumlegs"), elementium, 7, ItemMold.SubItem.LEGGINGS);
			FoundryMiscUtils.registerCasting(getItemStack("elementiumhelm"), elementium, 5, ItemMold.SubItem.HELMET);
			FoundryMiscUtils.registerCasting(getItemStack("elementiumboots"), elementium, 4, ItemMold.SubItem.BOOTS);
		}
	}
}