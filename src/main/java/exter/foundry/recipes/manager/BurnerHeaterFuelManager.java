package exter.foundry.recipes.manager;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.api.recipe.IBurnerHeaterFuel;
import exter.foundry.recipes.BurnerHeaterFuel;
import exter.foundry.tileentity.TileEntityFoundryHeatable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class BurnerHeaterFuelManager {

	private static final NonNullList<IBurnerHeaterFuel> FUELS = NonNullList.create();

	public static void addFuel(IBurnerHeaterFuel fuel) {
		FUELS.add(fuel);
	}

	public static void addFuel(Ingredient fuel, int time, int heat) {
		addFuel(new BurnerHeaterFuel(fuel, time, heat));
	}

	public static IBurnerHeaterFuel getFuel(ItemStack item) {
		for (IBurnerHeaterFuel f : FUELS)
			if (f.getFuel().apply(item)) return f;
		return null;
	}

	public static List<IBurnerHeaterFuel> getFuels() {
		return ImmutableList.copyOf(FUELS);
	}

	public static int getHeatNeeded(int temp, int tempLossRate) {
		return TileEntityFoundryHeatable.getMaxHeatRecieve(temp, tempLossRate);
	}

	public static void removeFuel(IBurnerHeaterFuel fuel) {
		FUELS.remove(fuel);
	}
}
