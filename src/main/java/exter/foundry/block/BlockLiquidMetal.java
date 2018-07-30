package exter.foundry.block;

import java.util.Random;

import exter.foundry.creativetab.FoundryTabFluids;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLiquidMetal extends BlockFluidClassic {
	private Object solid;

	private IBlockState solid_state = null;

	public BlockLiquidMetal(Fluid fluid, String name, Object solid_block) {
		super(fluid, Material.LAVA);
		setLightOpacity(0);
		setLightLevel(1.0f);
		solid = solid_block;
		setTranslationKey(name);
		setCreativeTab(FoundryTabFluids.INSTANCE);
		setRegistryName(name);
	}

	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isLiquid()) { return false; }
		return super.canDisplace(world, pos);
	}

	public void checkForHarden(World world, BlockPos pos, IBlockState state) {
		if (isSourceBlock(world, pos)) {
			if (solid_state == null && solid != null) {
				ItemStack item = ItemStack.EMPTY;
				if (solid instanceof ItemStack) {
					item = (ItemStack) solid;
				} else if (solid instanceof String) {
					for (ItemStack i : FoundryMiscUtils.getOresSafe((String) solid)) {
						if (i.getItem() instanceof ItemBlock) {
							item = i;
							break;
						}
					}
				} else {
					solid = null;
				}

				if (item.isEmpty()) {
					solid = null;
				}
				solid_state = getBlockStateFromItemStack(item);
			}
			if (solid_state == null) { return; }
			if (tryToHarden(world, pos, pos.add(-1, 0, 0))) { return; }
			if (tryToHarden(world, pos, pos.add(1, 0, 0))) { return; }
			if (tryToHarden(world, pos, pos.add(0, -1, 0))) { return; }
			if (tryToHarden(world, pos, pos.add(0, 1, 0))) { return; }
			if (tryToHarden(world, pos, pos.add(0, 0, -1))) { return; }
			if (tryToHarden(world, pos, pos.add(0, 0, 1))) { return; }
		}
	}

	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isLiquid()) { return false; }
		return super.displaceIfPossible(world, pos);
	}

	private IBlockState getBlockStateFromItemStack(ItemStack stack) {
		if (stack.getItem() instanceof ItemBlock) {
			Block block = ((ItemBlock) stack.getItem()).getBlock();
			int meta = stack.getMetadata();
			for (IBlockState state : block.getBlockState().getValidStates()) {
				if (state != null && block.damageDropped(state) == meta) { return state; }
			}
		}
		return null;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 300;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 0;
	}

	@Override
	public String getTranslationKey() {
		return stack.getUnlocalizedName();
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		super.neighborChanged(state, world, pos, block, fromPos);
		checkForHarden(world, pos, state);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		checkForHarden(world, pos, state);
	}

	@Override
	public void onEntityCollision(World wWorld, BlockPos pos, IBlockState state, Entity entity) {
		if (entity instanceof EntityLivingBase) {
			entity.motionX *= 0.5;
			entity.motionZ *= 0.5;
		}
		if (!entity.isImmuneToFire()) {
			if (!(entity instanceof EntityItem)) {
				entity.attackEntityFrom(DamageSource.LAVA, 4);
			}
			entity.setFire(15);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (temperature < 1200) { return; }
		double dx;
		double dy;
		double dz;

		if (world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && !world.getBlockState(pos.add(0, 1, 0)).isOpaqueCube()) {
			if (rand.nextInt(100) == 0) {
				dx = pos.getX() + rand.nextFloat();
				dy = pos.getY() + state.getBoundingBox(world, pos).maxY;
				dz = pos.getZ() + rand.nextFloat();
				world.spawnParticle(EnumParticleTypes.LAVA, dx, dy, dz, 0.0D, 0.0D, 0.0D);
				world.playSound(dx, dy, dz, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
			}

			if (rand.nextInt(200) == 0) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
			}
		}

		BlockPos down = pos.down();
		if (rand.nextInt(10) == 0 && world.getBlockState(down).isSideSolid(world, down, EnumFacing.UP) && !world.getBlockState(pos.add(0, -1, 0)).getMaterial().blocksMovement()) {
			dx = pos.getX() + rand.nextFloat();
			dy = pos.getY() - 1.05D;
			dz = pos.getZ() + rand.nextFloat();

			world.spawnParticle(EnumParticleTypes.DRIP_LAVA, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	private boolean tryToHarden(World world, BlockPos pos, BlockPos npos) {
		//Check if block is in contact with water.
		if (world.getBlockState(npos).getMaterial() == Material.WATER) {
			int i;
			world.setBlockState(pos, solid_state);
			world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f, false);
			for (i = 0; i < 8; i++) {
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + 1.2D, pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
			}
			return true;
		}
		return false;
	}
}
