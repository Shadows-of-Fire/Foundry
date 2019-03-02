package exter.foundry.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Miscellaneous utility methods
 */
public class FoundryMiscUtils {
	static public int divCeil(int a, int b) {
		return a / b + (a % b == 0 ? 0 : 1);
	}

	static public FluidStack drainFluidFromWorld(World world, BlockPos pos, boolean do_drain) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IFluidBlock) {
			IFluidBlock fluid_block = (IFluidBlock) state.getBlock();
			if (!fluid_block.canDrain(world, pos)) { return null; }
			return fluid_block.drain(world, pos, do_drain);
		}

		if (state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0) {
			if (do_drain) {
				world.setBlockToAir(pos);
			}
			return new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
		}

		if (state.getMaterial() == Material.LAVA && state.getValue(BlockLiquid.LEVEL) == 0) {
			if (do_drain) {
				world.setBlockToAir(pos);
			}
			return new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME);
		}
		return null;
	}

	static public Set<String> getAllItemOreDictionaryNames(ItemStack stack) {
		Set<String> result = new HashSet<>();
		for (String name : OreDictionary.getOreNames()) {
			List<ItemStack> ores = FoundryMiscUtils.getOresSafe(name);
			for (ItemStack i : ores) {
				if (i.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(i, stack)) {
					result.add(name);
				}
			}
		}
		return result;
	}

	static public String getItemOreDictionaryName(ItemStack stack) {
		for (String name : OreDictionary.getOreNames()) {
			List<ItemStack> ores = getOresSafe(name);
			for (ItemStack i : ores) {
				if (i.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(i, stack)) { return name; }
			}
		}
		return null;
	}

	static public ItemStack getModItemFromOreDictionary(String modid, String orename) {
		return getModItemFromOreDictionary(modid, orename, 1);
	}

	static public ItemStack getModItemFromOreDictionary(String modid, String orename, int amount) {
		return getStackFromDictWithPreference(modid, orename, amount);
	}

	public static NonNullList<ItemStack> getOresSafe(String orename) {
		return OreDictionary.getOres(orename, false);
	}

	public static ItemStack getStackFromDictWithPreference(String domain, String ore, int amount) {
		for (ItemStack is : FoundryMiscUtils.getOresSafe(ore)) {
			if (is.getItem().getRegistryName().getNamespace().equals(domain)) {
				is = is.copy();
				is.setCount(amount);
				return is;
			}
		}
		for (ItemStack is : FoundryMiscUtils.getOresSafe(ore)) {
			is = is.copy();
			is.setCount(amount);
			return is;
		}
		return ItemStack.EMPTY;
	}

	static public void localizeTooltip(String key, Object[] formatting, List<String> tooltip) {
		for (String str : new TextComponentTranslation(key, formatting).getUnformattedText().split("//")) {
			tooltip.add(TextFormatting.GRAY + str);
		}
	}

	static public void localizeTooltip(String key, List<String> tooltip) {
		localizeTooltip(key, new Object[0], tooltip);
	}

	static public void registerCasting(ItemStack item, Fluid liquid_metal, int ingots, ItemMold.SubItem mold_meta) {
		registerCasting(item, new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots), mold_meta, null);
	}

	static public void registerCasting(ItemStack item, Fluid liquid_metal, int ingots, ItemMold.SubItem mold_meta, IItemMatcher extra) {
		registerCasting(item, new FluidStack(liquid_metal, FoundryAPI.FLUID_AMOUNT_INGOT * ingots), mold_meta, extra);
	}

	static public void registerCasting(ItemStack item, FluidStack fluid, ItemMold.SubItem mold_meta, IItemMatcher extra) {
		if (!item.isEmpty()) {
			ItemStack mold = FoundryItems.mold(mold_meta);
			ItemStack extra_item = extra != null ? extra.getItem() : ItemStack.EMPTY;
			if (CastingRecipeManager.INSTANCE.findRecipe(new FluidStack(fluid.getFluid(), FoundryAPI.CASTER_TANK_CAPACITY), mold, extra_item) == null) {
				CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(item), fluid, mold, extra);
			}
		} else Foundry.LOGGER.error("Attempted to add a casting recipe with an invalid output!  Item: {}, Fluid: {}, Mold: {}, Extra: {}", item, fluid, mold_meta, extra);
	}

	/**
	 * Register item in the ore dictionary only if it's not already registered.
	 * @param name Ore Dictionary name.
	 * @param stack Item to register.
	 */
	static public void registerInOreDictionary(String name, ItemStack stack) {
		if (!stack.isEmpty() && !FoundryUtils.isItemInOreDictionary(name, stack)) OreDictionary.registerOre(name, stack);
	}

}
