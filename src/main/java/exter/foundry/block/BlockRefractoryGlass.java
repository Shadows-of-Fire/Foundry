package exter.foundry.block;

import java.util.Random;

import exter.foundry.creativetab.FoundryTabMaterials;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRefractoryGlass extends BlockBreakable {

	public BlockRefractoryGlass() {
		super(Material.GLASS, false);
		setCreativeTab(FoundryTabMaterials.INSTANCE);
		setHardness(0.4F);
		setSoundType(SoundType.GLASS);
		setTranslationKey("foundry.refractoryGlass");
		setRegistryName("refractoryGlass");
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
}