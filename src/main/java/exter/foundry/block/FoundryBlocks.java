package exter.foundry.block;

import exter.foundry.FoundryRegistry;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.ItemBlockMulti;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class FoundryBlocks {
	static public BlockComponent block_component;
	static public BlockRefractoryGlass block_refractory_glass;
	static public BlockFoundryMachine block_machine;
	static public BlockCastingTable block_casting_table;

	static public BlockAlloyFurnace block_alloy_furnace;
	static public BlockMoldStation block_mold_station;
	static public BlockBurnerHeater block_burner_heater;
	static public BlockCokeOven block_coke_oven;

	static public BlockRefractoryHopper block_refractory_hopper;
	static public BlockRefractorySpout block_refractory_spout;
	static public BlockRefractoryTankBasic block_refractory_tank_basic;
	static public BlockRefractoryTankStandard block_refractory_tank_standard;
	static public BlockRefractoryTankAdvanced block_refractory_tank_advanced;

	static public BlockCauldronBronze block_cauldron_bronze;

	static public void register(Block block) {
		FoundryRegistry.BLOCKS.add(block);
		FoundryRegistry.ITEMS.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}

	static public void registerBlocks() {
		block_component = new BlockComponent();
		block_refractory_glass = new BlockRefractoryGlass();
		block_machine = new BlockFoundryMachine();
		block_casting_table = new BlockCastingTable();

		block_alloy_furnace = new BlockAlloyFurnace();
		block_mold_station = new BlockMoldStation();
		block_refractory_hopper = new BlockRefractoryHopper();
		block_burner_heater = new BlockBurnerHeater();
		block_refractory_spout = new BlockRefractorySpout();
		block_refractory_tank_basic = new BlockRefractoryTankBasic();
		block_refractory_tank_standard = new BlockRefractoryTankStandard();
		block_refractory_tank_advanced = new BlockRefractoryTankAdvanced();
		block_cauldron_bronze = new BlockCauldronBronze();

		registerMulti(block_component);
		registerMulti(block_machine);
		registerMulti(block_casting_table);
		register(block_refractory_glass);
		register(block_alloy_furnace);
		register(block_mold_station);
		register(block_refractory_hopper);
		register(block_burner_heater);
		register(block_refractory_spout);
		register(block_refractory_tank_basic);
		register(block_refractory_tank_standard);
		register(block_refractory_tank_advanced);
		register(block_cauldron_bronze);

		if (FoundryConfig.block_cokeoven) {
			block_coke_oven = new BlockCokeOven();
			register(block_coke_oven);
		}
	}

	static private <T extends Block & IBlockVariants> void registerMulti(T block) {
		FoundryRegistry.BLOCKS.add(block);
		FoundryRegistry.ITEMS.add(new ItemBlockMulti(block).setRegistryName(block.getRegistryName()));
	}
}
