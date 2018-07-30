package exter.foundry.item;

import exter.foundry.block.IBlockVariants;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti extends ItemBlock {

	public <T extends Block & IBlockVariants> ItemBlockMulti(T block) {
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int dmg) {
		return dmg;
	}

	protected int getSubIndex(ItemStack stack) {
		return stack.getItemDamage();
	}

	@Override
	public final String getTranslationKey(ItemStack stack) {
		return ((IBlockVariants) block).getUnlocalizedName(getSubIndex(stack));
	}
}
