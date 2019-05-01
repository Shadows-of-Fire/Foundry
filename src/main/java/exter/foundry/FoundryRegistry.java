package exter.foundry;

import java.util.List;

import exter.foundry.entity.EntitySkeletonGun;
import exter.foundry.init.InitRecipes;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.tileentity.TileEntityAlloyFurnace;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import exter.foundry.tileentity.TileEntityAlloyingCrucible;
import exter.foundry.tileentity.TileEntityBurnerHeater;
import exter.foundry.tileentity.TileEntityCastingTableBlock;
import exter.foundry.tileentity.TileEntityCastingTableIngot;
import exter.foundry.tileentity.TileEntityCastingTablePlate;
import exter.foundry.tileentity.TileEntityCastingTableRod;
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
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber
public class FoundryRegistry {

	public static final List<Block> BLOCKS = Foundry.INFO.getBlockList();
	public static final List<Item> ITEMS = Foundry.INFO.getItemList();
	public static final List<SoundEvent> SOUNDS = Foundry.INFO.getSoundList();
	public static final List<IRecipe> RECIPES = Foundry.INFO.getRecipeList();

	@SubscribeEvent
	public void registerBlocks(Register<Block> e) {
		e.getRegistry().registerAll(BLOCKS.toArray(new Block[BLOCKS.size()]));
		GameRegistry.registerTileEntity(TileEntityMeltingCrucibleBasic.class, Foundry.reloc("melt_crucible_basic"));
		GameRegistry.registerTileEntity(TileEntityMeltingCrucibleStandard.class, Foundry.reloc("melt_crucible_standard"));
		GameRegistry.registerTileEntity(TileEntityMetalCaster.class, Foundry.reloc("metal_caster"));
		GameRegistry.registerTileEntity(TileEntityAlloyMixer.class, Foundry.reloc("alloy_mixer"));
		GameRegistry.registerTileEntity(TileEntityMetalInfuser.class, Foundry.reloc("metal_infuser"));
		GameRegistry.registerTileEntity(TileEntityAlloyFurnace.class, Foundry.reloc("alloy_furnace"));
		GameRegistry.registerTileEntity(TileEntityMoldStation.class, Foundry.reloc("mold_station"));
		GameRegistry.registerTileEntity(TileEntityMaterialRouter.class, Foundry.reloc("material_router"));
		GameRegistry.registerTileEntity(TileEntityRefractoryHopper.class, Foundry.reloc("refractory_hopper"));
		GameRegistry.registerTileEntity(TileEntityMetalAtomizer.class, Foundry.reloc("atomizer"));
		GameRegistry.registerTileEntity(TileEntityInductionHeater.class, Foundry.reloc("induction_heater"));
		GameRegistry.registerTileEntity(TileEntityBurnerHeater.class, Foundry.reloc("burner_heater"));
		GameRegistry.registerTileEntity(TileEntityCastingTableIngot.class, Foundry.reloc("cast_table_ingot"));
		GameRegistry.registerTileEntity(TileEntityCastingTablePlate.class, Foundry.reloc("cast_table_plate"));
		GameRegistry.registerTileEntity(TileEntityCastingTableRod.class, Foundry.reloc("cast_table_rod"));
		GameRegistry.registerTileEntity(TileEntityCastingTableBlock.class, Foundry.reloc("cast_table_block"));
		GameRegistry.registerTileEntity(TileEntityRefractorySpout.class, Foundry.reloc("refractory_spout"));
		GameRegistry.registerTileEntity(TileEntityMeltingCrucibleAdvanced.class, Foundry.reloc("melt_crucible_advanced"));
		GameRegistry.registerTileEntity(TileEntityRefractoryTankBasic.class, Foundry.reloc("tank_basic"));
		GameRegistry.registerTileEntity(TileEntityRefractoryTankStandard.class, Foundry.reloc("tank_standard"));
		GameRegistry.registerTileEntity(TileEntityRefractoryTankAdvanced.class, Foundry.reloc("tank_advanced"));
		GameRegistry.registerTileEntity(TileEntityAlloyingCrucible.class, Foundry.reloc("alloy_crucible"));
	}

	@SubscribeEvent
	public void registerItems(Register<Item> e) {
		e.getRegistry().registerAll(ITEMS.toArray(new Item[ITEMS.size()]));
	}

	@SubscribeEvent
	public void registerRecipes(Register<IRecipe> e) {
		OreDictionary.registerOre("fuelCoke", FoundryItems.component(ItemComponent.SubItem.COAL_COKE));
		OreDictionary.registerOre("dustSmallBlaze", FoundryItems.component(ItemComponent.SubItem.DUST_SMALL_BLAZE));
		OreDictionary.registerOre("dustSmallGunpowder", FoundryItems.component(ItemComponent.SubItem.DUST_SMALL_GUNPOWDER));
		OreDictionary.registerOre("rodCupronickel", FoundryItems.component(ItemComponent.SubItem.ROD_CUPRONICKEL));
		InitRecipes.preInit();
		e.getRegistry().registerAll(RECIPES.toArray(new IRecipe[RECIPES.size()]));
	}

	@SubscribeEvent
	public void registerSounds(Register<SoundEvent> e) {
		e.getRegistry().registerAll(SOUNDS.toArray(new SoundEvent[SOUNDS.size()]));
	}

	@SubscribeEvent
	public void registerEntities(Register<EntityEntry> e) {
		EntityEntryBuilder<EntitySkeletonGun> builder = EntityEntryBuilder.create();
		builder.entity(EntitySkeletonGun.class);
		builder.id(new ResourceLocation("foundry", "gun_skeleton"), 0).name("foundry.gunSkeleton");
		builder.factory(EntitySkeletonGun::new).tracker(40, 1, true);
		builder.egg(0xd3d3d3, 0x808080);
		e.getRegistry().register(builder.build());
		EntityRegistry.addSpawn(EntitySkeletonGun.class, 8, 1, 2, EnumCreatureType.MONSTER, BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS).toArray(new Biome[0]));
		LootTableList.register(new ResourceLocation("foundry", "gun_skeleton"));
	}
}
