package exter.foundry.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class SizedIngredient extends Ingredient {

	final int count;

	public SizedIngredient(ItemStack... stacks) {
		super(stacks);
		count = stacks[0].getCount();
	}

	@Override
	public boolean apply(ItemStack stack) {
		return super.apply(stack) && stack.getCount() >= count;
	}

}
