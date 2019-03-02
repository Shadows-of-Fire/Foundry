package exter.foundry.integration.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.config.FoundryConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public class MTHelper {

	public static String getFluidDescription(ILiquidStack stack) {
		return getFluidDescription(CraftTweakerMC.getLiquidStack(stack));
	}

	public static String getFluidDescription(FluidStack stack) {
		return String.format("<liquid:%s> * %d", stack.getFluid().getName(), stack.amount);
	}

	public static Ingredient getIngredient(IIngredient ingr) {
		return CraftTweakerMC.getIngredient(ingr);
	}

	public static String getItemDescription(IItemStack stack) {
		return getItemDescription(CraftTweakerMC.getItemStack(stack));
	}

	public static String getItemDescription(ItemStack stack) {
		String desc = String.format("<%s:%d> * %d", stack.getItem().getRegistryName(), stack.getItemDamage(), stack.getCount());
		if (stack.hasTagCompound()) desc += " with tag " + stack.getTagCompound().toString();
		return desc;
	}

	public static void printCrt(String print) {
		if (FoundryConfig.crtError) CraftTweakerAPI.logError(print);
		else CraftTweakerAPI.logInfo(print);
	}
}
