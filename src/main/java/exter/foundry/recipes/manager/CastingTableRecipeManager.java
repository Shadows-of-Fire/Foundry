package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.ICastingTableRecipe.TableType;
import exter.foundry.recipes.CastingTableRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CastingTableRecipeManager {

	private static final EnumMap<TableType, Map<String, ICastingTableRecipe>> RECIPES = new EnumMap<>(TableType.class);

	static {
		for (TableType type : TableType.values())
			RECIPES.put(type, new HashMap<>());
	}

	public static void addRecipe(TableType tableType, ICastingTableRecipe recipe) {
		RECIPES.get(tableType).put(recipe.getInput().getFluid().getName(), recipe);

	}

	public static void addRecipe(ItemStack result, FluidStack input, TableType type) {
		ICastingTableRecipe recipe = new CastingTableRecipe(result, input, type);
		RECIPES.get(recipe.getTableType()).put(input.getFluid().getName(), recipe);
	}

	public static ICastingTableRecipe findRecipe(FluidStack fluid, TableType type) {
		ICastingTableRecipe recipe = RECIPES.get(type).get(fluid.getFluid().getName());
		return recipe;
	}

	public static List<ICastingTableRecipe> getAllRecipes() {
		List<ICastingTableRecipe> result = new ArrayList<>();
		for (Map<String, ICastingTableRecipe> map : RECIPES.values()) {
			result.addAll(map.values());
		}
		return result;
	}

	public static Map<TableType, Map<String, ICastingTableRecipe>> getRecipes() {
		return ImmutableMap.copyOf(RECIPES);
	}

	public static Collection<ICastingTableRecipe> getRecipes(TableType type) {
		return ImmutableList.copyOf(RECIPES.get(type).values());
	}

	public static void removeRecipe(ICastingTableRecipe recipe) {
		RECIPES.get(recipe.getTableType()).remove(recipe.getInput().getFluid().getName());
	}

	public static void removeRecipe(TableType tableType, String name) {
		RECIPES.get(tableType).remove(name);
	}
}
