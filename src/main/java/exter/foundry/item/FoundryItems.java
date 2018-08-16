package exter.foundry.item;

import exter.foundry.FoundryRegistry;
import exter.foundry.item.ammo.ItemRoundAP;
import exter.foundry.item.ammo.ItemRoundFire;
import exter.foundry.item.ammo.ItemRoundHollow;
import exter.foundry.item.ammo.ItemRoundJacketed;
import exter.foundry.item.ammo.ItemRoundLumium;
import exter.foundry.item.ammo.ItemRoundNormal;
import exter.foundry.item.ammo.ItemRoundPoison;
import exter.foundry.item.ammo.ItemRoundSnow;
import exter.foundry.item.ammo.ItemShellAP;
import exter.foundry.item.ammo.ItemShellLumium;
import exter.foundry.item.ammo.ItemShellNormal;
import exter.foundry.item.firearm.ItemRevolver;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.item.ItemStack;

public class FoundryItems {
	static public ItemComponent item_component;
	static public ItemMold item_mold;
	static public ItemRevolver item_revolver;
	static public ItemShotgun item_shotgun;
	static public ItemRoundNormal item_round;
	static public ItemRoundJacketed item_round_jacketed;
	static public ItemRoundHollow item_round_hollow;
	static public ItemRoundFire item_round_fire;
	static public ItemRoundPoison item_round_poison;
	static public ItemRoundAP item_round_ap;
	static public ItemRoundLumium item_round_lumium;
	static public ItemRoundSnow item_round_snow;
	static public ItemShellNormal item_shell;
	static public ItemShellAP item_shell_ap;
	static public ItemShellLumium item_shell_lumium;

	static public ItemRefractoryFluidContainer item_container;

	static public ItemStack component(ItemComponent.SubItem sub) {
		return component(sub, 1);
	}

	static public ItemStack component(ItemComponent.SubItem sub, int amount) {
		return new ItemStack(item_component, amount, sub.id);
	}

	static public ItemStack mold(ItemMold.SubItem sub) {
		return mold(sub, 1);
	}

	static public ItemStack mold(ItemMold.SubItem sub, int amount) {
		return new ItemStack(item_mold, amount, sub.id);
	}

	static public void registerItems() {
		item_component = new ItemComponent();
		item_mold = new ItemMold();
		item_revolver = new ItemRevolver();
		item_shotgun = new ItemShotgun();
		item_round = new ItemRoundNormal();
		item_round_jacketed = new ItemRoundJacketed();
		item_round_hollow = new ItemRoundHollow();
		item_round_fire = new ItemRoundFire();
		item_round_poison = new ItemRoundPoison();
		item_round_ap = new ItemRoundAP();
		item_round_lumium = new ItemRoundLumium();
		item_round_snow = new ItemRoundSnow();
		item_shell = new ItemShellNormal();
		item_shell_ap = new ItemShellAP();
		item_shell_lumium = new ItemShellLumium();
		item_container = new ItemRefractoryFluidContainer();

		FoundryRegistry.ITEMS.add(item_component);
		FoundryRegistry.ITEMS.add(item_mold);
		FoundryRegistry.ITEMS.add(item_revolver);
		FoundryRegistry.ITEMS.add(item_shotgun);
		FoundryRegistry.ITEMS.add(item_round);
		FoundryRegistry.ITEMS.add(item_round_jacketed);
		FoundryRegistry.ITEMS.add(item_round_hollow);
		FoundryRegistry.ITEMS.add(item_round_fire);
		FoundryRegistry.ITEMS.add(item_round_poison);
		FoundryRegistry.ITEMS.add(item_round_ap);
		FoundryRegistry.ITEMS.add(item_round_lumium);
		FoundryRegistry.ITEMS.add(item_round_snow);
		FoundryRegistry.ITEMS.add(item_shell);
		FoundryRegistry.ITEMS.add(item_shell_ap);
		FoundryRegistry.ITEMS.add(item_shell_lumium);
		FoundryRegistry.ITEMS.add(item_container);
	}
}
