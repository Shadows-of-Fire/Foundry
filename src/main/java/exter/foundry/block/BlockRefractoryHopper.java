package exter.foundry.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import exter.foundry.Foundry;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.FoundryGuiHandler;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.renderer.ISpoutPourDepth;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRefractoryHopper extends BlockContainer implements ISpoutPourDepth {

	public enum EnumHopperFacing implements IStringSerializable {
		NORTH(0, "north", EnumFacing.NORTH),
		SOUTH(1, "south", EnumFacing.SOUTH),
		EAST(2, "east", EnumFacing.EAST),
		WEST(3, "west", EnumFacing.WEST),
		DOWN(4, "down", EnumFacing.DOWN);

		static public EnumHopperFacing fromID(int num) {
			for (EnumHopperFacing m : values()) {
				if (m.id == num) { return m; }
			}
			return null;
		}

		public final int id;
		public final String name;

		public final EnumFacing facing;

		private EnumHopperFacing(int id, String name, EnumFacing target) {
			this.id = id;
			this.name = name;
			facing = target;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return getName();
		}
	}

	public static final PropertyEnum<EnumHopperFacing> FACING = PropertyEnum.create("facing", EnumHopperFacing.class);

	protected static final AxisAlignedBB AABB_SIDES = new AxisAlignedBB(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);

	protected static final AxisAlignedBB[] BOUNDS = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D), new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D) };

	private final Random rand = new Random();

	public BlockRefractoryHopper() {
		super(Material.IRON);
		setCreativeTab(FoundryTabMachines.INSTANCE);
		setHardness(1.0F);
		setResistance(8.0F);
		setTranslationKey("foundry.refractoryHopper");
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumHopperFacing.DOWN));
		setRegistryName("refractoryHopper");
	}

	@Override
	@Deprecated
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		for (AxisAlignedBB box : BOUNDS) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		FoundryMiscUtils.localizeTooltip("tooltip.foundry.refractoryHopper", tooltip);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);

		if (te != null && te instanceof TileEntityFoundry && !world.isRemote) {
			TileEntityFoundry tef = (TileEntityFoundry) te;
			int i;
			for (i = 0; i < tef.getSizeInventory(); i++) {
				ItemStack is = tef.getStackInSlot(i);

				if (!is.isEmpty()) {
					double drop_x = rand.nextFloat() * 0.3 + 0.35;
					double drop_y = rand.nextFloat() * 0.3 + 0.35;
					double drop_z = rand.nextFloat() * 0.3 + 0.35;
					EntityItem entityitem = new EntityItem(world, pos.getX() + drop_x, pos.getY() + drop_y, pos.getZ() + drop_z, is);
					entityitem.setPickupDelay(10);

					world.spawnEntity(entityitem);
				}
			}
		}
		world.removeTileEntity(pos);
		super.breakBlock(world, pos, state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRefractoryHopper();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(FACING) == EnumHopperFacing.DOWN ? FULL_BLOCK_AABB : AABB_SIDES;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).id;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getSpoutPourDepth(World world, BlockPos pos, IBlockState state) {
		return 11;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		switch (facing) {
		case EAST:
			return getDefaultState().withProperty(FACING, EnumHopperFacing.WEST);
		case NORTH:
			return getDefaultState().withProperty(FACING, EnumHopperFacing.SOUTH);
		case SOUTH:
			return getDefaultState().withProperty(FACING, EnumHopperFacing.NORTH);
		case WEST:
			return getDefaultState().withProperty(FACING, EnumHopperFacing.EAST);
		default:
			return getDefaultState().withProperty(FACING, EnumHopperFacing.DOWN);

		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumHopperFacing.fromID(meta));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		TileEntityFoundry te = (TileEntityFoundry) world.getTileEntity(pos);

		if (te != null) {
			te.updateRedstone();
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitx, float hity, float hitz) {
		if (world.isRemote) {
			return true;
		} else {
			player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_REFRACTORYHOPPER, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
}
