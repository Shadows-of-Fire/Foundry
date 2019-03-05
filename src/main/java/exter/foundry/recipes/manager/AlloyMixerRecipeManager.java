package exter.foundry.recipes.manager;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.recipes.AlloyMixerRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;

public class AlloyMixerRecipeManager {

	private static final NonNullList<IAlloyMixerRecipe> RECIPES = NonNullList.create();

	public static void addRecipe(IAlloyMixerRecipe recipe) {
		RECIPES.add(recipe);
	}

	public static void addRecipe(FluidStack out, FluidStack... in) {
		RECIPES.add(new AlloyMixerRecipe(out, in));
	}

	public static Pair<IAlloyMixerRecipe, int[]> findRecipe(FluidStack[] in) {
		for (IAlloyMixerRecipe r : RECIPES) {
			int[] matched = r.matchesRecipe(in);
			if (matched != null) return Pair.of(r, matched);
		}
		return null;
	}

	public static List<IAlloyMixerRecipe> getRecipes() {
		return ImmutableList.copyOf(RECIPES);
	}

	public static void removeRecipe(IAlloyMixerRecipe recipe) {
		RECIPES.remove(recipe);
	}

}
