package exter.foundry.proxy;

import java.util.List;
import java.util.Map;

import exter.foundry.Foundry;
import exter.foundry.FoundryRegistry;
import exter.foundry.block.BlockCastingTable;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockLiquidMetal;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.model.RFCModel;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractorySpout;
import exter.foundry.tileentity.TileEntityRefractoryTankAdvanced;
import exter.foundry.tileentity.TileEntityRefractoryTankBasic;
import exter.foundry.tileentity.TileEntityRefractoryTankStandard;
import exter.foundry.tileentity.renderer.CastingTableRenderer;
import exter.foundry.tileentity.renderer.CastingTableRendererBlock;
import exter.foundry.tileentity.renderer.HopperRenderer;
import exter.foundry.tileentity.renderer.SpoutRenderer;
import exter.foundry.tileentity.renderer.TankRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import shadows.placebo.event.LoaderEvents.PlaceboInit;
import shadows.placebo.event.LoaderEvents.PlaceboPostInit;
import shadows.placebo.event.LoaderEvents.PlaceboPreInit;
import shadows.placebo.util.PlaceboUtil;

@EventBusSubscriber(modid = Foundry.MODID, value = Side.CLIENT)
public class ClientFoundryProxy {

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent e) {
		for (BlockFoundryMachine.EnumMachine m : BlockFoundryMachine.EnumMachine.values()) {
			PlaceboUtil.sMRL(FoundryBlocks.block_machine, m.id, m.model);
		}

		for (BlockCastingTable.EnumTable m : BlockCastingTable.EnumTable.values()) {
			PlaceboUtil.sMRL(FoundryBlocks.block_casting_table, m.id, m.model);
		}

		PlaceboUtil.sMRL(FoundryBlocks.block_refractory_glass, 0, "refractoryGlass");
		PlaceboUtil.sMRL(FoundryBlocks.block_alloy_furnace, 0, "alloyFurnace");
		PlaceboUtil.sMRL(FoundryBlocks.block_mold_station, 0, "moldStation");
		PlaceboUtil.sMRL(FoundryBlocks.block_refractory_hopper, 0, "refractoryHopper");
		PlaceboUtil.sMRL(FoundryBlocks.block_burner_heater, 0, "burnerHeater");
		PlaceboUtil.sMRL(FoundryBlocks.block_refractory_spout, 0, "refractorySpout");
		PlaceboUtil.sMRL(FoundryBlocks.block_refractory_tank_basic, 0, "refractoryTank");
		PlaceboUtil.sMRL(FoundryBlocks.block_refractory_tank_standard, 0, "refractoryTankStandard");
		PlaceboUtil.sMRL(FoundryBlocks.block_refractory_tank_advanced, 0, "infernoTank");
		PlaceboUtil.sMRL(FoundryBlocks.block_cauldron_bronze, 0, "bronzeCauldron");
		if (FoundryConfig.block_cokeoven) {
			PlaceboUtil.sMRL(FoundryBlocks.block_coke_oven, 0, "cokeOven");
		}

		for (BlockComponent.EnumVariant v : BlockComponent.EnumVariant.values()) {
			PlaceboUtil.sMRL(FoundryBlocks.block_component, v.ordinal(), v.model);
		}

		for (ItemComponent.SubItem c : ItemComponent.SubItem.values()) {
			PlaceboUtil.sMRL(FoundryItems.item_component, c.id, c.name);
		}

		for (ItemMold.SubItem m : ItemMold.SubItem.values()) {
			PlaceboUtil.sMRL(FoundryItems.item_mold, m.id, m.name);
		}

		PlaceboUtil.sMRL(FoundryItems.item_revolver, 0, "revolver");
		PlaceboUtil.sMRL(FoundryItems.item_shotgun, 0, "shotgun");
		PlaceboUtil.sMRL(FoundryItems.item_round, 0, "roundNormal");
		PlaceboUtil.sMRL(FoundryItems.item_round_hollow, 0, "roundHollow");
		PlaceboUtil.sMRL(FoundryItems.item_round_jacketed, 0, "roundJacketed");
		PlaceboUtil.sMRL(FoundryItems.item_round_fire, 0, "roundFire");
		PlaceboUtil.sMRL(FoundryItems.item_round_poison, 0, "roundPoison");
		PlaceboUtil.sMRL(FoundryItems.item_round_ap, 0, "roundAP");
		PlaceboUtil.sMRL(FoundryItems.item_round_lumium, 0, "roundLumium");
		PlaceboUtil.sMRL(FoundryItems.item_round_snow, 0, "roundSnow");
		PlaceboUtil.sMRL(FoundryItems.item_shell, 0, "shellNormal");
		PlaceboUtil.sMRL(FoundryItems.item_shell_ap, 0, "shellAP");
		PlaceboUtil.sMRL(FoundryItems.item_shell_lumium, 0, "shellLumium");
		PlaceboUtil.sMRL(FoundryItems.item_container, 0, "container");

		for (Block b : FoundryRegistry.BLOCKS) {
			if (b instanceof BlockLiquidMetal) {
				ModelLoader.setCustomStateMapper(b, new FluidStateMapper((BlockLiquidMetal) b));
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "normal"));
			}
		}
	}

	@SubscribeEvent
	public static void preInit(PlaceboPreInit e) {
		ModelLoaderRegistry.registerLoader(RFCModel.Loader.instance);
		MaterialRegistry.INSTANCE.initIcons();
		for (Map.Entry<String, FluidLiquidMetal> ent : LiquidMetalRegistry.INSTANCE.getFluids().entrySet()) {
			Fluid fluid = ent.getValue();
			Block block = fluid.getBlock();
			Item item = Item.getItemFromBlock(block);
			String name = ent.getKey();
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, new LiquidMetalItemMeshDefinition(name));
			ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
		}
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonGun.class, manager -> new RenderSkeleton(manager));
	}

	@SubscribeEvent
	public static void init(PlaceboInit e) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableIngot.class, new CastingTableRenderer(6, 10, 4, 12, 9, 12, "foundry:blocks/castingtable_top_ingot"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTablePlate.class, new CastingTableRenderer(3, 13, 3, 13, 11, 12, "foundry:blocks/castingtable_top_plate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableRod.class, new CastingTableRenderer(7, 9, 2, 14, 10, 12, "foundry:blocks/castingtable_top_rod"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableBlock.class, new CastingTableRendererBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractorySpout.class, new SpoutRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryHopper.class, new HopperRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankBasic.class, new TankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankStandard.class, new TankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankAdvanced.class, new TankRenderer());
	}

	@SubscribeEvent
	public static void postInit(PlaceboPostInit e) {
		for (OreDictMaterial material : OreDictMaterial.MATERIALS) {
			List<ItemStack> ores = OreDictionary.getOres(material.default_prefix + material.suffix, false);
			if (ores.size() > 0) {
				MaterialRegistry.INSTANCE.registerMaterialIcon(material.suffix, ores.get(0));
			} else {
				for (OreDictType type : OreDictType.TYPES) {
					ores = OreDictionary.getOres(type.prefix + material.suffix, false);
					if (ores.size() > 0) {
						MaterialRegistry.INSTANCE.registerMaterialIcon(material.suffix, ores.get(0));
						break;
					}
				}
			}
		}

		for (OreDictType type : OreDictType.TYPES) {
			List<ItemStack> ores = OreDictionary.getOres(type.prefix + type.default_suffix, false);
			if (ores.size() > 0) {
				MaterialRegistry.INSTANCE.registerTypeIcon(type.name, ores.get(0));
			} else {
				for (OreDictMaterial material : OreDictMaterial.MATERIALS) {
					ores = OreDictionary.getOres(type.prefix + material.suffix, false);
					if (ores.size() > 0) {
						MaterialRegistry.INSTANCE.registerTypeIcon(type.name, ores.get(0));
						break;
					}
				}
			}
		}
	}

	static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

		public final BlockLiquidMetal fluid;
		public final ModelResourceLocation location;

		public FluidStateMapper(BlockLiquidMetal fluid) {
			this.fluid = fluid;
			this.location = new ModelResourceLocation(fluid.getRegistryName(), "normal");
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return this.location;
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			return this.location;
		}
	}

	static class LiquidMetalItemMeshDefinition implements ItemMeshDefinition {

		private final ModelResourceLocation model;

		LiquidMetalItemMeshDefinition(String name) {
			model = new ModelResourceLocation("foundry:liquid" + name);
		}

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack) {
			return model;
		}
	}
}
