package exter.foundry.block;

import java.util.List;
import java.util.Random;

import exter.foundry.Foundry;
import exter.foundry.proxy.FoundryGuiHandler;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAlloyFurnace extends BlockFoundrySidedMachine {
	public BlockAlloyFurnace() {
		super(Material.ROCK);
		setTranslationKey("foundry.alloyFurnace");
		setHardness(1.0F);
		setResistance(8.0F);
		setSoundType(SoundType.STONE);
		setRegistryName("alloyFurnace");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		FoundryMiscUtils.localizeTooltip("tooltip.foundry.alloyFurnace", tooltip);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityAlloyFurnace();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hit_x, float hit_y, float hit_z) {
		if (world.isRemote) {
			return true;
		} else {
			player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_ALLOYFURNACE, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		if (state.getValue(ACTIVE)) {
			EnumFacing facing = state.getValue(FACING);
			float f = pos.getX() + 0.5F;
			float f1 = pos.getY() + 0.0F + random.nextFloat() * 6.0F / 16.0F;
			float f2 = pos.getZ() + 0.5F;
			float f3 = 0.52F;
			float f4 = random.nextFloat() * 0.6F - 0.3F;

			switch (facing) {
			case NORTH:
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
				break;
			case EAST:
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				break;
			case WEST:
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
				break;
			case SOUTH:
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
				break;
			default:
			}
		}
	}
}
