package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.recipes.CastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class CastingRecipeManager {

	private static final NonNullList<ICastingRecipe> RECIPES = NonNullList.create();

	public static void addRecipe(ICastingRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static void addRecipe(ItemStack result, FluidStack input, Ingredient item, Ingredient mold) {
		addRecipe(result, input, item, mold, 100);
	}

	public static void addRecipe(ItemStack result, FluidStack input, Ingredient item, Ingredient mold, int cast_speed) {
		ICastingRecipe recipe = new CastingRecipe(result, input, item, mold, cast_speed);
		if (recipe.getItemInput() != Ingredient.EMPTY) RECIPES.add(0, recipe);
		else RECIPES.add(recipe);

	}

	public static void addRecipe(int i, ICastingRecipe recipe) {
		RECIPES.add(i, recipe);
	}

	public static ICastingRecipe findRecipe(FluidStack fluid, ItemStack item, ItemStack mold) {
		if (mold.isEmpty()) return null;
		for (ICastingRecipe cr : RECIPES) {
			if (cr.matchesRecipe(fluid, item, mold)) return cr;
		}
		return null;
	}

	public static List<ICastingRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public static boolean isMold(ItemStack stack) {
		if (stack.isEmpty()) return false;
		for (ICastingRecipe cr : RECIPES) {
			if (cr.getMold().apply(stack)) return true;
		}
		return false;
	}

	public static void removeRecipe(ICastingRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
