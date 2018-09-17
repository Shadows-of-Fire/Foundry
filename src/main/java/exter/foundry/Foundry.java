package exter.foundry;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cofh.cofhworld.init.WorldProps;
import cofh.cofhworld.util.Utils;
import exter.foundry.api.FoundryAPI;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.capability.CapabilityFirearmRound;
import exter.foundry.capability.CapabilityHeatProvider;
import exter.foundry.config.FoundryConfig;
import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.init.InitRecipes;
import exter.foundry.integration.ModIntegrationManager;
import exter.foundry.item.FoundryItems;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.network.MessageTileEntitySync;
import exter.foundry.proxy.CommonFoundryProxy;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AlloyingCrucibleRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.sound.FoundrySounds;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
import exter.foundry.tileentity.TileEntityCokeOven;
import exter.foundry.tileentity.TileEntityInductionHeater;
import exter.foundry.tileentity.TileEntityMaterialRouter;
import exter.foundry.tileentity.TileEntityMeltingCrucibleAdvanced;
import exter.foundry.tileentity.TileEntityMeltingCrucibleBasic;
import exter.foundry.tileentity.TileEntityMeltingCrucibleStandard;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import exter.foundry.tileentity.TileEntityMetalCaster;
import exter.foundry.tileentity.TileEntityMetalInfuser;
import exter.foundry.tileentity.TileEntityMoldStation;
import exter.foundry.tileentity.TileEntityRefractoryHopper;
import exter.foundry.tileentity.TileEntityRefractorySpout;
import exter.foundry.tileentity.TileEntityRefractoryTankAdvanced;
import exter.foundry.tileentity.TileEntityRefractoryTankBasic;
import exter.foundry.tileentity.TileEntityRefractoryTankStandard;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import shadows.placebo.registry.RegistryInformation;
import shadows.placebo.util.RecipeHelper;

@Mod(modid = Foundry.MODID, name = Foundry.MODNAME, version = Foundry.MODVERSION, dependencies = "required-after:placebo@[1.2.0,);required-after:thermalfoundation;after:jei@[4.12,);after:tconstruct;after:mekanism")
public class Foundry {
	public static final String MODID = "foundry";
	public static final String MODNAME = "Foundry";
	public static final String MODVERSION = "3.3.4";

	@SidedProxy(clientSide = "exter.foundry.proxy.ClientFoundryProxy", serverSide = "exter.foundry.proxy.CommonFoundryProxy")
	public static CommonFoundryProxy proxy;

	@Instance
	public static Foundry INSTANCE = null;

	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	public static final RegistryInformation INFO = new RegistryInformation(MODID, null);

	public static final RecipeHelper HELPER = new RecipeHelper(MODID, MODNAME, INFO.getRecipeList());

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new FoundryRegistry());
		FoundryConfig.load(new Configuration(event.getSuggestedConfigurationFile()));

		ModIntegrationManager.registerDefaults();

		FoundryAPI.FLUIDS = LiquidMetalRegistry.INSTANCE;

		FoundryAPI.MELTING_MANAGER = MeltingRecipeManager.INSTANCE;
		FoundryAPI.CASTING_MANAGER = CastingRecipeManager.INSTANCE;
		FoundryAPI.CASTING_TABLE_MANAGER = CastingTableRecipeManager.INSTANCE;
		FoundryAPI.ALLOY_MIXER_MANAGER = AlloyMixerRecipeManager.INSTANCE;
		FoundryAPI.INFUSER_MANAGER = InfuserRecipeManager.INSTANCE;
		FoundryAPI.ALLOY_FURNACE_MANAGER = AlloyFurnaceRecipeManager.INSTANCE;
		FoundryAPI.ATOMIZER_MANAGER = AtomizerRecipeManager.INSTANCE;
		FoundryAPI.MOLD_MANAGER = MoldRecipeManager.INSTANCE;
		FoundryAPI.ALLOYING_CRUCIBLE_MANAGER = AlloyingCrucibleRecipeManager.INSTANCE;

		FoundryAPI.MATERIALS = MaterialRegistry.INSTANCE;
		FoundryAPI.BURNER_HEATER_FUEL = BurnerHeaterFuelManager.INSTANCE;

		CapabilityHeatProvider.init();
		CapabilityFirearmRound.init();
		FoundrySounds.init();

		FoundryItems.registerItems();
		FoundryBlocks.registerBlocks();

		FoundryFluids.init();

		NETWORK.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.SERVER);
		NETWORK.registerMessage(MessageTileEntitySync.Handler.class, MessageTileEntitySync.class, 0, Side.CLIENT);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		try {
			File f = new File(WorldProps.worldGenDir, "04_foundry_aluminium.json");
			if (f.createNewFile()) Utils.copyFileUsingStream("assets/foundry/world/04_foundry_aluminium.json", f);
		} catch (IOException e) {
			LOGGER.error("Failed to copy foundry aluminium generation file!");
			e.printStackTrace();
		}

		ModIntegrationManager.apply(m -> m.preInit());
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		InitRecipes.init();

		GameRegistry.registerTileEntity(TileEntityMeltingCrucibleBasic.class, reloc("melt_crucible_basic"));
		GameRegistry.registerTileEntity(TileEntityMeltingCrucibleStandard.class, reloc("melt_crucible_standard"));
		GameRegistry.registerTileEntity(TileEntityMetalCaster.class, reloc("metal_caster"));
		GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, reloc("alloy_mixer"));
		GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, reloc("metal_infuser"));
		GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, reloc("alloy_furnace"));
		GameRegistry.registerTileEntity(TileEntityMoldStation.class, reloc("mold_station"));
		GameRegistry.registerTileEntity(TileEntityMaterialRouter.class, reloc("material_router"));
		GameRegistry.registerTileEntity(TileEntityRefractoryHopper.class, reloc("refractory_hopper"));
		GameRegistry.registerTileEntity(TileEntityMetalAtomizer.class, reloc("atomizer"));
		GameRegistry.registerTileEntity(TileEntityInductionHeater.class, reloc("induction_heater"));
		GameRegistry.registerTileEntity(TileEntityBurnerHeater.class, reloc("burner_heater"));
		GameRegistry.registerTileEntity(TileEntityCastingTableIngot.class, reloc("cast_table_ingot"));
		GameRegistry.registerTileEntity(TileEntityCastingTablePlate.class, reloc("cast_table_plate"));
		GameRegistry.registerTileEntity(TileEntityCastingTableRod.class, reloc("cast_table_rod"));
		GameRegistry.registerTileEntity(TileEntityCastingTableBlock.class, reloc("cast_table_block"));
		GameRegistry.registerTileEntity(TileEntityRefractorySpout.class, reloc("refractory_spout"));
		GameRegistry.registerTileEntity(TileEntityMeltingCrucibleAdvanced.class, reloc("melt_crucible_advanced"));
		GameRegistry.registerTileEntity(TileEntityRefractoryTankBasic.class, reloc("tank_basic"));
		GameRegistry.registerTileEntity(TileEntityRefractoryTankStandard.class, reloc("tank_standard"));
		GameRegistry.registerTileEntity(TileEntityRefractoryTankAdvanced.class, reloc("tank_advanced"));
		GameRegistry.registerTileEntity(TileEntityAlloyingCrucible.class, reloc("alloy_crucible"));
		if (FoundryConfig.block_cokeoven) GameRegistry.registerTileEntity(TileEntityCokeOven.class, reloc("coke_oven"));

		EntityRegistry.registerModEntity(new ResourceLocation("foundry", "gun_skeleton"), EntitySkeletonGun.class, "foundry.gunSkeleton", 0, this, 80, 1, true);
		EntityRegistry.registerEgg(new ResourceLocation("foundry", "gun_skeleton"), 0xd3d3d3, 0x808080);
		LootTableList.register(new ResourceLocation("foundry", "gun_skeleton"));

		EntityRegistry.addSpawn(EntitySkeletonGun.class, 8, 1, 2, EnumCreatureType.MONSTER, BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS).toArray(new Biome[0]));

		ModIntegrationManager.apply(m -> m.init());
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ModIntegrationManager.apply(m -> m.postInitEarly());
		InitRecipes.postInit();
		proxy.postInit();
		ModIntegrationManager.apply(m -> m.postInit());
	}

	private static ResourceLocation reloc(String s) {
		return new ResourceLocation(MODID, s);
	}
}
