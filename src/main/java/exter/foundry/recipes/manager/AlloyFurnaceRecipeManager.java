package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.recipes.AlloyFurnaceRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class AlloyFurnaceRecipeManager {

	private static final NonNullList<IAlloyFurnaceRecipe> RECIPES = NonNullList.create();

	public static void addRecipe(IAlloyFurnaceRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static void addRecipe(ItemStack out, Ingredient in_a, Ingredient in_b) {
		RECIPES.add(new AlloyFurnaceRecipe(out, in_a, in_b));
	}

	public static IAlloyFurnaceRecipe findRecipe(ItemStack in_a, ItemStack in_b) {
		for (IAlloyFurnaceRecipe r : RECIPES) {
			if (r.matchesRecipe(in_a, in_b)) return r;
		}
		return null;
	}

	public static List<IAlloyFurnaceRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public static void removeRecipe(IAlloyFurnaceRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
