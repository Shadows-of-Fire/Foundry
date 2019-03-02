package exter.foundry.api.recipe;

import net.minecraft.item.crafting.Ingredient;

public interface IBurnerHeaterFuel {

	int getBurnTime();

	Ingredient getFuel();

	int getHeat();
}
