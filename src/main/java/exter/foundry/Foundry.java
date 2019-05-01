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
import exter.foundry.tileentity.TileEntityCokeOven;
import net.minecraft.util.ResourceLocation;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import shadows.placebo.registry.RegistryInformation;
import shadows.placebo.util.RecipeHelper;

@Mod(modid = Foundry.MODID, name = Foundry.MODNAME, version = Foundry.MODVERSION, dependencies = "required-after:placebo@[1.2.0,);required-after:thermalfoundation;after:jei@[4.12,);after:tconstruct;after:mekanism")
public class Foundry {
	public static final String MODID = "foundry";
	public static final String MODNAME = "Foundry";
	public static final String MODVERSION = "3.3.6";

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
		if (FoundryConfig.block_cokeoven) GameRegistry.registerTileEntity(TileEntityCokeOven.class, reloc("coke_oven"));
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

	static ResourceLocation reloc(String s) {
		return new ResourceLocation(MODID, s);
	}
}
