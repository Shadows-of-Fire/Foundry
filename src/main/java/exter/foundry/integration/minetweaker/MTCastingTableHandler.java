package exter.foundry.integration.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.integration.jei.CastingTableJEI;
import exter.foundry.recipes.CastingTableRecipe;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.foundry.CastingTable")
public class MTCastingTableHandler {
	public static class CastingTableAction extends AddRemoveAction {

		ICastingTableRecipe recipe;

		public CastingTableAction(ICastingTableRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		protected void add() {
			CastingTableRecipeManager.instance.recipes.get(recipe.getTableType()).put(recipe.getInput().getFluid().getName(), recipe);
			CraftTweakerAPI.getIjeiRecipeRegistry().addRecipe(new CastingTableJEI.Wrapper(recipe));
		}

		@Override
		protected void remove() {
			CastingTableRecipeManager.instance.recipes.get(recipe.getTableType()).remove(recipe.getInput().getFluid().getName());
			CraftTweakerAPI.getIjeiRecipeRegistry().removeRecipe(new CastingTableJEI.Wrapper(recipe));
		}

		@Override
		public String getRecipeType() {
			return "casting table";
		}

		@Override
		public String getDescription() {
			return String.format("( %s, %s ) -> %s", MTHelper.getFluidDescription(recipe.getInput()), recipe.getTableType().toString(), MTHelper.getItemDescription(recipe.getOutput()));
		}
	}

	static private void addRecipe(IItemStack output, ILiquidStack input, ICastingTableRecipe.TableType table) {
		ItemStackMatcher out = new ItemStackMatcher(CraftTweakerMC.getItemStack(output));
		FluidStack in = CraftTweakerMC.getLiquidStack(input);
		CastingTableRecipe recipe;
		try {
			recipe = new CastingTableRecipe(out, in, table);
		} catch (IllegalArgumentException e) {
			CraftTweakerAPI.logError("Invalid casting recipe: " + e.getMessage());
			return;
		}
		CraftTweakerAPI.apply((new CastingTableAction(recipe).action_add));
	}

	@ZenMethod
	static public void addIngotRecipe(IItemStack output, ILiquidStack input) {
		addRecipe(output, input, ICastingTableRecipe.TableType.INGOT);
	}

	@ZenMethod
	static public void addPlateRecipe(IItemStack output, ILiquidStack input) {
		addRecipe(output, input, ICastingTableRecipe.TableType.PLATE);
	}

	@ZenMethod
	static public void addRodRecipe(IItemStack output, ILiquidStack input) {
		addRecipe(output, input, ICastingTableRecipe.TableType.ROD);
	}

	@ZenMethod
	static public void addBlockRecipe(IItemStack output, ILiquidStack input) {
		addRecipe(output, input, ICastingTableRecipe.TableType.BLOCK);
	}

	static public void removeRecipe(ILiquidStack input, ICastingTableRecipe.TableType table) {
		ICastingTableRecipe recipe = CastingTableRecipeManager.instance.findRecipe(CraftTweakerMC.getLiquidStack(input), table);
		if (recipe == null) {
			CraftTweakerAPI.logWarning("Casting table recipe not found.");
			return;
		}
		CraftTweakerAPI.apply((new CastingTableAction(recipe)).action_remove);
	}

	@ZenMethod
	static public void removeIngotRecipe(ILiquidStack input) {
		removeRecipe(input, ICastingTableRecipe.TableType.INGOT);
	}

	@ZenMethod
	static public void removePlateRecipe(ILiquidStack input) {
		removeRecipe(input, ICastingTableRecipe.TableType.PLATE);
	}

	@ZenMethod
	static public void removeRodRecipe(ILiquidStack input) {
		removeRecipe(input, ICastingTableRecipe.TableType.ROD);
	}

	@ZenMethod
	static public void removeBlockRecipe(ILiquidStack input) {
		removeRecipe(input, ICastingTableRecipe.TableType.BLOCK);
	}
}