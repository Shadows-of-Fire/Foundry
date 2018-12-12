package exter.foundry.init;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exter.foundry.api.recipe.IAlloyFurnaceRecipe;
import exter.foundry.config.FoundryConfig;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryModifiable;

public class InitHardCore {
	static private boolean hasOreDictionaryPrefix(ItemStack item, String prefix) {
		for (String name : FoundryMiscUtils.getAllItemOreDictionaryNames(item)) {
			if (name.startsWith(prefix)) { return true; }
		}
		return false;
	}

	static public void init() {
		if (FoundryConfig.hardcore_remove_ingot_nugget) { // Remove 9 nuggets -> ingot recipes (only if they can be melted)
			removeCompressionCrafting("nugget", "ingot");
		}

		if (FoundryConfig.hardcore_remove_block_ingot) { // Remove 9 ingots -> block recipes (only if they can be melted)
			removeCompressionCrafting("ingot", "block");
		}

		if (FoundryConfig.hardcore_remove_ingot_block) { // Remove block -> 9 ingots recipes (only if they can be melted)
			removeDecompressionCrafting("block", "ingot");
		}

		if (FoundryConfig.hardcore_remove_nugget_ingot) { // Remove ingot -> 9 nuggets recipes (only if they can be melted)
			removeDecompressionCrafting("ingot", "nugget");
		}

		if (FoundryConfig.hardcore_furnace_remove_ingots) { // Remove furnace recipes that outputs ingots (except the ones in the keep list or can't be melted).
			Iterator<Map.Entry<ItemStack, ItemStack>> iter = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<ItemStack, ItemStack> recipe = iter.next();
				Set<String> ore_names = FoundryMiscUtils.getAllItemOreDictionaryNames(recipe.getValue());
				for (String name : ore_names) {
					if (name.startsWith("ingot") && !FoundryConfig.hardcore_furnace_keep_ingots.contains(name) && MeltingRecipeManager.INSTANCE.findRecipe(recipe.getValue()) != null) {
						iter.remove();
						break;
					}
				}
			}

			// Remove alloy furnace recipes as well.
			List<IAlloyFurnaceRecipe> remove = new ArrayList<>();
			for (IAlloyFurnaceRecipe recipe : AlloyFurnaceRecipeManager.INSTANCE.getRecipes()) {
				ItemStack output = recipe.getOutput();
				Set<String> ore_names = FoundryMiscUtils.getAllItemOreDictionaryNames(output);
				for (String name : ore_names) {
					if (name.startsWith("ingot") && !FoundryConfig.hardcore_furnace_keep_ingots.contains(name) && MeltingRecipeManager.INSTANCE.findRecipe(output) != null) {
						remove.add(recipe);
						break;
					}
				}
			}
			for (IAlloyFurnaceRecipe recipe : remove) {
				AlloyFurnaceRecipeManager.INSTANCE.removeRecipe(recipe);
			}
		}
	}

	static private void removeCompressionCrafting(String prefix, String result_prefix) {
		Set<IRecipe> remove = new HashSet<>();
		InventoryCrafting grid = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer playerIn) {
				return false;
			}
		}, 3, 3);

		for (String ore_name : OreDictionary.getOreNames()) {
			if (ore_name.startsWith(prefix)) {
				for (ItemStack item : OreDictionary.getOres(ore_name, false)) {
					if (MeltingRecipeManager.INSTANCE.findRecipe(item) != null) {
						for (int i = 0; i < 9; i++) {
							grid.setInventorySlotContents(i, item);
						}
						for (IRecipe recipe : ForgeRegistries.RECIPES) {
							if (recipe.matches(grid, null) && hasOreDictionaryPrefix(recipe.getCraftingResult(grid), result_prefix)) {
								remove.add(recipe);
							}
						}
					}
				}
			}
		}
		for (IRecipe rec : remove)
			((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES).remove(rec.getRegistryName());
	}

	static private void removeDecompressionCrafting(String prefix, String result_prefix) {
		Set<IRecipe> remove = new HashSet<>();
		InventoryCrafting grid = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer playerIn) {
				return false;
			}
		}, 1, 1);

		for (String ore_name : OreDictionary.getOreNames()) {
			if (ore_name.startsWith(prefix)) {
				for (ItemStack item : OreDictionary.getOres(ore_name, false)) {
					if (MeltingRecipeManager.INSTANCE.findRecipe(item) != null) {
						grid.setInventorySlotContents(0, item);
						for (IRecipe recipe : ForgeRegistries.RECIPES) {
							if (recipe.matches(grid, null) && 
									hasOreDictionaryPrefix(recipe.getCraftingResult(grid), result_prefix)) {
								remove.add(recipe);
							}
						}
					}
				}
			}
		}

		for (IRecipe rec : remove)
			((IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES).remove(rec.getRegistryName());
	}
}
