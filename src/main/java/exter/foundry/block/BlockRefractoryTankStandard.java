package exter.foundry.block;

import java.util.List;
import java.util.Random;

import exter.foundry.Foundry;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.FoundryGuiHandler;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityRefractoryTankStandard;
import exter.foundry.tileentity.renderer.ISpoutPourDepth;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRefractoryTankStandard extends BlockContainer implements ISpoutPourDepth {

	private final Random rand = new Random();

	public BlockRefractoryTankStandard() {
		super(Material.IRON);
		setSoundType(SoundType.STONE);
		setCreativeTab(FoundryTabMachines.INSTANCE);
		setHardness(1.0F);
		setResistance(8.0F);
		setTranslationKey("foundry.refractoryTankStandard");
		setRegistryName("refractoryTankStandard");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		FoundryMiscUtils.localizeTooltip("tooltip.foundry.refractoryTankStandard", tooltip);
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
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityRefractoryTankStandard();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getSpoutPourDepth(World world, BlockPos pos, IBlockState state) {
		return 2;
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
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
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
			player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_REFRACTORYTANK, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
}
