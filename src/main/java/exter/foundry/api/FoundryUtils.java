package exter.foundry.api;

import cofh.thermalfoundation.ThermalFoundation;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold.SubItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class FoundryUtils {

	private static boolean exists(String ore) {
		return OreDictionary.doesOreNameExist(ore);
	}

	/**
	 * Check if an item is registered in the Ore Dictionary.
	 * @param name Ore name to check.
	 * @param stack Item to check.
	 * @return true if the item is registered, false otherwise.
	 */
	static public boolean isItemInOreDictionary(String name, ItemStack stack) {
		for (ItemStack i : OreDictionary.getOres(name, false))
			if (OreDictionary.itemMatches(i, stack, false)) return true;
		return false;
	}

	/**
	 * Helper method for registering basic melting recipes for a given metal.
	 * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
	 * @param fluid The liquid created by the smelter.
	 */
	static public void registerBasicMeltingRecipes(String partial_name, Fluid fluid) {
		if (FoundryAPI.MELTING_MANAGER != null) {
			if (exists("ingot" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("ingot" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

			if (exists("block" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("block" + partial_name), new FluidStack(fluid, FoundryAPI.getAmountBlock()));

			if (exists("nugget" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("nugget" + partial_name), new FluidStack(fluid, FoundryAPI.getAmountNugget()));

			if (exists("dust" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("dust" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));

			if (exists("ore" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("ore" + partial_name), new FluidStack(fluid, FoundryAPI.getAmountOre()));

			if (exists("orePoor" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("orePoor" + partial_name), new FluidStack(fluid, FoundryAPI.getAmountNugget() * 2));

			if (exists("dustSmall" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("dustSmall" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

			if (exists("dustTiny" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("dustTiny" + partial_name), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 4));

			if (exists("plate" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("plate" + partial_name), new FluidStack(fluid, FoundryAPI.getAmountPlate()));

			if (exists("gear" + partial_name)) FoundryAPI.MELTING_MANAGER.addRecipe(new OreIngredient("gear" + partial_name), new FluidStack(fluid, FoundryAPI.getAmountGear()));
		}
	}

	public static void tryAddToolArmorRecipes(String name, Fluid fluid) {
		if (fluid == FoundryFluids.liquid_cupronickel) name = "constantan";
		else if (fluid == FoundryFluids.liquid_aluminium) name = "aluminum";

		ItemStack helm = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.helmet_" + name)));
		ItemStack chest = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.plate_" + name)));
		ItemStack legs = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.legs_" + name)));
		ItemStack boots = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "armor.boots_" + name)));

		if (!helm.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(helm), new FluidStack(fluid, FoundryAPI.getAmountHelm()));
			FoundryAPI.CASTING_MANAGER.addRecipe((helm), new FluidStack(fluid, FoundryAPI.getAmountHelm()), FoundryItems.mold(SubItem.HELMET), null);
		}

		if (!chest.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(chest), new FluidStack(fluid, FoundryAPI.getAmountChest()));
			FoundryAPI.CASTING_MANAGER.addRecipe((chest), new FluidStack(fluid, FoundryAPI.getAmountChest()), FoundryItems.mold(SubItem.CHESTPLATE), null);
		}

		if (!legs.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(legs), new FluidStack(fluid, FoundryAPI.getAmountLegs()));
			FoundryAPI.CASTING_MANAGER.addRecipe((legs), new FluidStack(fluid, FoundryAPI.getAmountLegs()), FoundryItems.mold(SubItem.LEGGINGS), null);
		}

		if (!boots.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(boots), new FluidStack(fluid, FoundryAPI.getAmountBoots()));
			FoundryAPI.CASTING_MANAGER.addRecipe((boots), new FluidStack(fluid, FoundryAPI.getAmountBoots()), FoundryItems.mold(SubItem.BOOTS), null);
		}

		ItemStack pickaxe = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.pickaxe_" + name)));
		ItemStack axe = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.axe_" + name)));
		ItemStack shovel = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.shovel_" + name)));
		ItemStack hoe = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.hoe_" + name)));
		ItemStack sword = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(ThermalFoundation.MOD_ID, "tool.sword_" + name)));
		OreIngredient stick = new SizedOreIngredient("stickWood", 2);

		if (!pickaxe.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(pickaxe), new FluidStack(fluid, FoundryAPI.getAmountPickaxe()));
			FoundryAPI.CASTING_MANAGER.addRecipe((pickaxe), new FluidStack(fluid, FoundryAPI.getAmountPickaxe()), FoundryItems.mold(SubItem.PICKAXE), stick);
		}

		if (!axe.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(axe), new FluidStack(fluid, FoundryAPI.getAmountAxe()));
			FoundryAPI.CASTING_MANAGER.addRecipe((axe), new FluidStack(fluid, FoundryAPI.getAmountAxe()), FoundryItems.mold(SubItem.AXE), stick);
		}

		if (!shovel.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(shovel), new FluidStack(fluid, FoundryAPI.getAmountShovel()));
			FoundryAPI.CASTING_MANAGER.addRecipe((shovel), new FluidStack(fluid, FoundryAPI.getAmountShovel()), FoundryItems.mold(SubItem.SHOVEL), stick);
		}

		if (!hoe.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(hoe), new FluidStack(fluid, FoundryAPI.getAmountHoe()));
			FoundryAPI.CASTING_MANAGER.addRecipe((hoe), new FluidStack(fluid, FoundryAPI.getAmountHoe()), FoundryItems.mold(SubItem.HOE), stick);
		}

		if (!sword.isEmpty()) {
			FoundryAPI.MELTING_MANAGER.addRecipe(Ingredient.fromStacks(sword), new FluidStack(fluid, FoundryAPI.getAmountSword()));
			FoundryAPI.CASTING_MANAGER.addRecipe((sword), new FluidStack(fluid, FoundryAPI.getAmountSword()), FoundryItems.mold(SubItem.SWORD), new OreIngredient("stickWood"));
		}
	}
}
