package exter.foundry.block;

import java.util.List;
import java.util.Random;

import exter.foundry.Foundry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.creativetab.FoundryTabMachines;
import exter.foundry.proxy.FoundryGuiHandler;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import exter.foundry.tileentity.TileEntityFoundry;
import exter.foundry.tileentity.TileEntityInductionHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMeltingCrucibleAdvanced;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import exter.foundry.tileentity.TileEntityMeltingCrucibleStandard;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFoundryMachine extends Block implements ITileEntityProvider, IBlockVariants {
	static public enum EnumMachine implements IStringSerializable {
		CRUCIBLE_BASIC(0, "crucible_basic", "machineCrucibleBasic"),
		CASTER(1, "caster", "machineCaster"),
		ALLOYMIXER(2, "alloymixer", "machineAlloyMixer"),
		INFUSER(3, "infuser", "machineInfuser"),
		MATERIALROUTER(4, "router", "machineMaterialRouter"),
		ATOMIZER(5, "atomizer", "machineAtomizer"),
		INDUCTIONHEATER(6, "heater_induction", "machineInductionHeater"),
		CRUCIBLE_ADVANCED(7, "crucible_advanced", "machineCrucibleAdvanced"),
		CRUCIBLE_STANDARD(8, "crucible_standard", "machineCrucibleStandard"),
		ALLOYING_CRUCIBLE(9, "alloying_crucible", "machineAlloyingCrucible");

		static public EnumMachine fromID(int num) {
			for (EnumMachine m : values()) {
				if (m.id == num) { return m; }
			}
			return null;
		}

		public final int id;
		public final String name;
		public final String model;
		private String tooltip;

		private EnumMachine(int id, String name, String model) {
			this.id = id;
			this.name = name;
			this.model = model;
			this.tooltip = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return getName();
		}

		public String getTooltipKey() {
			return tooltip;
		}

		public void setTooltip(String tooltip) {
			this.tooltip = tooltip;
		}
	}

	public static final PropertyEnum<EnumMachine> MACHINE = PropertyEnum.create("machine", EnumMachine.class);

	private final Random rand = new Random();

	public BlockFoundryMachine() {
		super(Material.IRON);
		setHardness(1.0F);
		setResistance(8.0F);
		setSoundType(SoundType.STONE);
		setTranslationKey("foundry.machine");
		setCreativeTab(FoundryTabMachines.INSTANCE);
		setRegistryName("machine");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		IBlockState state = getStateFromMeta(stack.getMetadata());
		FoundryMiscUtils.localizeTooltip("tooltip.foundry.machine." + state.getValue(MACHINE).getTooltipKey(), getFormatting(state), tooltip);
	}

	public ItemStack asItemStack(EnumMachine machine) {
		return new ItemStack(this, 1, getMetaFromState(getDefaultState().withProperty(MACHINE, machine)));
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
		return new BlockStateContainer(this, MACHINE);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return createTileEntity(world, getStateFromMeta(meta));
	}

	public static Object[] getFormatting(IBlockState state) {
		switch (state.getValue(MACHINE)) {
		case CRUCIBLE_BASIC:
			return new Object[] { FoundryAPI.CRUCIBLE_BASIC_MAX_TEMP / 100 };
		case CRUCIBLE_STANDARD:
			return new Object[] { FoundryAPI.CRUCIBLE_STANDARD_MAX_TEMP / 100 };
		case CRUCIBLE_ADVANCED:
			return new Object[] { FoundryAPI.CRUCIBLE_ADVANCED_MAX_TEMP / 100 };
		default:
			return new Object[0];
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (state.getValue(MACHINE)) {
		case CRUCIBLE_BASIC:
			return new TileEntityMeltingCrucibleBasic();
		case CASTER:
			return new TileEntityMetalCaster();
		case ALLOYMIXER:
			return new TileEntityAlloyMixer();
		case INFUSER:
			return new TileEntityMetalInfuser();
		case MATERIALROUTER:
			return new TileEntityMaterialRouter();
		case ATOMIZER:
			return new TileEntityMetalAtomizer();
		case INDUCTIONHEATER:
			return new TileEntityInductionHeater();
		case CRUCIBLE_ADVANCED:
			return new TileEntityMeltingCrucibleAdvanced();
		case CRUCIBLE_STANDARD:
			return new TileEntityMeltingCrucibleStandard();
		case ALLOYING_CRUCIBLE:
			return new TileEntityAlloyingCrucible();
		}
		return null;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(MACHINE).id;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(MACHINE, EnumMachine.fromID(meta));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (EnumMachine m : EnumMachine.values()) {
			list.add(new ItemStack(this, 1, m.id));
		}
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return getTranslationKey() + "." + getStateFromMeta(meta).getValue(MACHINE).name;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hit_x, float hit_y, float hit_z) {
		if (world.isRemote) {
			return true;
		} else {
			switch (state.getValue(MACHINE)) {
			case CRUCIBLE_BASIC:
			case CRUCIBLE_STANDARD:
			case CRUCIBLE_ADVANCED:
				player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_CRUCIBLE, world, pos.getX(), pos.getY(), pos.getZ());
				break;
			case CASTER:
				player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_CASTER, world, pos.getX(), pos.getY(), pos.getZ());
				break;
			case ALLOYMIXER:
				player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_ALLOYMIXER, world, pos.getX(), pos.getY(), pos.getZ());
				break;
			case INFUSER:
				player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_INFUSER, world, pos.getX(), pos.getY(), pos.getZ());
				break;
			case MATERIALROUTER:
				player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_MATERIALROUTER, world, pos.getX(), pos.getY(), pos.getZ());
				break;
			case ATOMIZER:
				player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_ATOMIZER, world, pos.getX(), pos.getY(), pos.getZ());
				break;
			case ALLOYING_CRUCIBLE:
				player.openGui(Foundry.INSTANCE, FoundryGuiHandler.GUI_ALLOYINGCRUCIBLE, world, pos.getX(), pos.getY(), pos.getZ());
				break;
			default:
				return false;
			}
			return true;
		}
	}
}
