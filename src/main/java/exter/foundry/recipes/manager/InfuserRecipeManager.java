package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.recipes.InfuserRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class InfuserRecipeManager {

	private static final NonNullList<IInfuserRecipe> RECIPES = NonNullList.create();

	public void addRecipe(IInfuserRecipe recipe) {
		RECIPES.add(recipe);
	}

	public void addRecipe(FluidStack result, Ingredient item, FluidStack fluid, int energy) {
		RECIPES.add(new InfuserRecipe(result, item, fluid, energy));
	}

	public IInfuserRecipe findRecipe(FluidStack fluid, ItemStack item) {
		if (item.isEmpty()) return null;
		for (IInfuserRecipe ir : RECIPES) {
			if (ir.matchesRecipe(fluid, item)) return ir;
		}
		return null;
	}

	public List<IInfuserRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public void removeRecipe(IInfuserRecipe recipe) {
		RECIPES.remove(recipe);
	}
}
