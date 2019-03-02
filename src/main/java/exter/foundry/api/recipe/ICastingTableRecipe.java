package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingTableRecipe {

	public static enum TableType {
		INGOT,
		PLATE,
		ROD,
		BLOCK
	}

	/**
	 * Get the fluid required for casting.
	 * @return FluidStack containing the required fluid.
	 */
	FluidStack getInput();

	/**
	 * Get the actual item produced by casting.
	 * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
	 */
	ItemStack getOutput();

	/**
	 * Get the Casting Table type.
	 */
	TableType getTableType();
}
