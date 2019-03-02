package exter.foundry.block;

import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.tileentity.TileEntityFoundry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockFoundrySidedMachine extends Block {

	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	public static final PropertyEnum<EnumFacing> FACING = BlockHorizontal.FACING;

	public BlockFoundrySidedMachine(Material material) {
		super(material);
		setCreativeTab(FoundryTabMachines.INSTANCE);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);

		if (te != null && te instanceof TileEntityFoundry && !world.isRemote) {
			TileEntityFoundry tef = (TileEntityFoundry) te;
			IItemHandler handler = tef.getItemHandler(null);
			if (handler != null) {
				for (int i = 0; i < handler.getSlots(); i++) {
					ItemStack is = handler.getStackInSlot(i);
					if (!is.isEmpty()) Block.spawnAsEntity(world, pos, is);
				}
			}
		}
		world.removeTileEntity(pos);
		super.breakBlock(world, pos, state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ACTIVE, FACING);
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(pos));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() | (state.getValue(ACTIVE) ? 1 : 0 << 2);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 0b11)).withProperty(ACTIVE, (meta >> 2) == 1);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		if (!world.isRemote) {
			IBlockState block = world.getBlockState(pos.add(0, 0, -1));
			IBlockState block1 = world.getBlockState(pos.add(0, 0, 1));
			IBlockState block2 = world.getBlockState(pos.add(-1, 0, 0));
			IBlockState block3 = world.getBlockState(pos.add(1, 0, 0));
			EnumFacing facing = EnumFacing.NORTH;

			if (block.isOpaqueCube() && !block1.isOpaqueCube()) {
				facing = EnumFacing.NORTH;
			}
			if (block1.isOpaqueCube() && !block.isOpaqueCube()) {
				facing = EnumFacing.SOUTH;
			}
			if (block2.isOpaqueCube() && !block3.isOpaqueCube()) {
				facing = EnumFacing.EAST;
			}
			if (block3.isOpaqueCube() && !block2.isOpaqueCube()) {
				facing = EnumFacing.WEST;
			}
			world.setBlockState(pos, state.withProperty(FACING, facing));
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack item) {
		int dir = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		EnumFacing facing = EnumFacing.NORTH;
		if (dir == 0) {
			facing = EnumFacing.NORTH;
		}
		if (dir == 1) {
			facing = EnumFacing.EAST;
		}
		if (dir == 2) {
			facing = EnumFacing.SOUTH;
		}
		if (dir == 3) {
			facing = EnumFacing.WEST;
		}
		world.setBlockState(pos, state.withProperty(FACING, facing));
	}

}
