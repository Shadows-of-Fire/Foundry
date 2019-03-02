package exter.foundry.api;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class SizedOreIngredient extends OreIngredient {

	final int count;
	final List<ItemStack> ores;

	public SizedOreIngredient(String ore, int count) {
		super(ore);
		this.count = count;
		ores = OreDictionary.getOres(ore);
	}

	@Override
	public boolean apply(ItemStack input) {
		return super.apply(input) && input.getCount() >= count;
	}

	ItemStack[] stacks;

	@Override
	public ItemStack[] getMatchingStacks() {
		if (stacks == null || stacks.length != ores.size()) {
			stacks = super.getMatchingStacks();
			for (int i = 0; i < stacks.length; i++) {
				stacks[i] = stacks[i].copy();
				stacks[i].setCount(count);
			}
		}
		return stacks;
	}

}
