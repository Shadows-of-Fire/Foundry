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
import exter.foundry.integration.ModIntegrationManager;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class ClientFoundryProxy extends CommonFoundryProxy {

	@Override
	public void preInit() {
		ModelLoaderRegistry.registerLoader(RFCModel.Loader.instance);
		MaterialRegistry.INSTANCE.initIcons();
		for (Map.Entry<String, FluidLiquidMetal> e : LiquidMetalRegistry.INSTANCE.getFluids().entrySet()) {
			Fluid fluid = e.getValue();
			Block block = fluid.getBlock();
			Item item = Item.getItemFromBlock(block);
			String name = e.getKey();
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, new LiquidMetalItemMeshDefinition(name));
			ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(BlockFluidBase.LEVEL).build());
		}
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonGun.class, manager -> new RenderSkeleton(manager));
		MinecraftForge.EVENT_BUS.register(this);
		ModIntegrationManager.apply(m -> m.preInitClient());
	}

	@Override
	public void init() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableIngot.class, new CastingTableRenderer(6, 10, 4, 12, 9, 12, "foundry:blocks/castingtable_top_ingot"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTablePlate.class, new CastingTableRenderer(3, 13, 3, 13, 11, 12, "foundry:blocks/castingtable_top_plate"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableRod.class, new CastingTableRenderer(7, 9, 2, 14, 10, 12, "foundry:blocks/castingtable_top_rod"));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCastingTableBlock.class, new CastingTableRendererBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractorySpout.class, new SpoutRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryHopper.class, new HopperRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankBasic.class, new TankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankStandard.class, new TankRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRefractoryTankAdvanced.class, new TankRenderer());
		ModIntegrationManager.apply(m -> m.initClient());
	}

	@Override
	public void postInit() {
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
		ModIntegrationManager.apply(m -> m.postInitClient());
	}

	@SubscribeEvent
	public void onModelRegister(ModelRegistryEvent e) {
		for (BlockFoundryMachine.EnumMachine m : BlockFoundryMachine.EnumMachine.values()) {
			registerItemModel(FoundryBlocks.block_machine, m.model, m.id);
		}
		for (BlockCastingTable.EnumTable m : BlockCastingTable.EnumTable.values()) {
			registerItemModel(FoundryBlocks.block_casting_table, m.model, m.id);
		}

		registerItemModel(FoundryBlocks.block_refractory_glass, "refractoryGlass");
		registerItemModel(FoundryBlocks.block_alloy_furnace, "alloyFurnace");
		registerItemModel(FoundryBlocks.block_mold_station, "moldStation");
		registerItemModel(FoundryBlocks.block_refractory_hopper, "refractoryHopper");
		registerItemModel(FoundryBlocks.block_burner_heater, "burnerHeater");
		registerItemModel(FoundryBlocks.block_refractory_spout, "refractorySpout");
		registerItemModel(FoundryBlocks.block_refractory_tank_basic, "refractoryTank");
		registerItemModel(FoundryBlocks.block_refractory_tank_standard, "refractoryTankStandard");
		registerItemModel(FoundryBlocks.block_refractory_tank_advanced, "infernoTank");
		registerItemModel(FoundryBlocks.block_cauldron_bronze, "bronzeCauldron");
		if (FoundryConfig.block_cokeoven) {
			registerItemModel(FoundryBlocks.block_coke_oven, "cokeOven");
		}

		for (BlockComponent.EnumVariant v : BlockComponent.EnumVariant.values()) {
			registerItemModel(FoundryBlocks.block_component, v.model, v.ordinal());
		}

		for (ItemComponent.SubItem c : ItemComponent.SubItem.values()) {
			registerItemModel(FoundryItems.item_component, c.name, c.id);
		}

		for (ItemMold.SubItem m : ItemMold.SubItem.values()) {
			registerItemModel(FoundryItems.item_mold, m.name, m.id);
		}

		registerItemModel(FoundryItems.item_revolver, "revolver", 0);
		registerItemModel(FoundryItems.item_shotgun, "shotgun", 0);
		registerItemModel(FoundryItems.item_round, "roundNormal", 0);
		registerItemModel(FoundryItems.item_round_hollow, "roundHollow", 0);
		registerItemModel(FoundryItems.item_round_jacketed, "roundJacketed", 0);
		registerItemModel(FoundryItems.item_round_fire, "roundFire", 0);
		registerItemModel(FoundryItems.item_round_poison, "roundPoison", 0);
		registerItemModel(FoundryItems.item_round_ap, "roundAP", 0);
		registerItemModel(FoundryItems.item_round_lumium, "roundLumium", 0);
		registerItemModel(FoundryItems.item_round_snow, "roundSnow", 0);
		registerItemModel(FoundryItems.item_shell, "shellNormal", 0);
		registerItemModel(FoundryItems.item_shell_ap, "shellAP", 0);
		registerItemModel(FoundryItems.item_shell_lumium, "shellLumium", 0);
		registerItemModel(FoundryItems.item_container, "container", 0);

		for (Block b : FoundryRegistry.BLOCKS) {
			if (b instanceof BlockLiquidMetal) {
				ModelLoader.setCustomStateMapper(b, new FluidStateMapper((BlockLiquidMetal) b));
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "normal"));
			}
		}
	}

	static void registerItemModel(Block block, String name) {
		registerItemModel(Item.getItemFromBlock(block), name);
	}

	static void registerItemModel(Block block, String name, int meta) {
		registerItemModel(Item.getItemFromBlock(block), name, meta);
	}

	static void registerItemModel(Item item, String name) {
		registerItemModel(item, name, 0);
	}

	static void registerItemModel(Item item, String name, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Foundry.MODID, name), "inventory"));
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
