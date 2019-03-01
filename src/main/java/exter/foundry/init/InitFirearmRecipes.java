package exter.foundry.init;

import exter.foundry.api.FoundryAPI;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class InitFirearmRecipes {

	static public void init() {

		ItemStack mold_bullet = FoundryItems.mold(ItemMold.SubItem.BULLET);
		ItemStack mold_bullet_hollow = FoundryItems.mold(ItemMold.SubItem.BULLET_HOLLOW);
		ItemStack mold_round_casing = FoundryItems.mold(ItemMold.SubItem.ROUND_CASING);
		ItemStack mold_pellet = FoundryItems.mold(ItemMold.SubItem.PELLET);
		ItemStack mold_shell_casing = FoundryItems.mold(ItemMold.SubItem.SHELL_CASING);
		ItemStack mold_gun_barrel = FoundryItems.mold(ItemMold.SubItem.GUN_BARREL);
		ItemStack mold_revolver_drum = FoundryItems.mold(ItemMold.SubItem.REVOLVER_DRUM);
		ItemStack mold_revolver_frame = FoundryItems.mold(ItemMold.SubItem.REVOLVER_FRAME);
		ItemStack mold_shotgun_pump = FoundryItems.mold(ItemMold.SubItem.SHOTGUN_PUMP);
		ItemStack mold_shotgun_frame = FoundryItems.mold(ItemMold.SubItem.SHOTGUN_FRAME);

		MoldRecipeManager.INSTANCE.addRecipe(mold_bullet, 3, 3, new int[] { 0, 1, 0, 1, 2, 1, 0, 1, 0, });

		MoldRecipeManager.INSTANCE.addRecipe(mold_bullet_hollow, 3, 3, new int[] { 0, 2, 0, 2, 1, 2, 0, 2, 0, });

		MoldRecipeManager.INSTANCE.addRecipe(mold_round_casing, 4, 4, new int[] { 0, 2, 2, 0, 2, 1, 1, 2, 2, 1, 1, 2, 0, 2, 2, 0 });

		MoldRecipeManager.INSTANCE.addRecipe(mold_pellet, 1, 1, new int[] { 1 });

		MoldRecipeManager.INSTANCE.addRecipe(mold_shell_casing, 5, 5, new int[] { 0, 0, 3, 0, 0, 0, 3, 1, 3, 0, 3, 1, 1, 1, 3, 0, 3, 1, 3, 0, 0, 0, 3, 0, 0 });

		MoldRecipeManager.INSTANCE.addRecipe(mold_gun_barrel, 3, 3, new int[] { 0, 4, 0, 4, 0, 4, 0, 4, 0, });

		MoldRecipeManager.INSTANCE.addRecipe(mold_revolver_drum, 5, 5, new int[] { 0, 3, 3, 3, 0, 3, 0, 3, 0, 3, 3, 3, 3, 3, 3, 3, 0, 3, 0, 3, 0, 3, 3, 3, 0 });

		MoldRecipeManager.INSTANCE.addRecipe(mold_revolver_frame, 5, 6, new int[] { 0, 2, 2, 0, 0, 0, 2, 2, 2, 0, 2, 2, 2, 2, 0, 0, 2, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2 });

		MoldRecipeManager.INSTANCE.addRecipe(mold_shotgun_pump, 3, 3, new int[] { 4, 0, 4, 4, 0, 4, 4, 4, 4, });

		MoldRecipeManager.INSTANCE.addRecipe(mold_shotgun_frame, 6, 5, new int[] { 0, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, });

		ItemStack bullet = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET);
		ItemStack bullet_hollow = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_HOLLOW);
		ItemStack bullet_jacketed = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_JACKETED);
		ItemStack bullet_casing = FoundryItems.component(ItemComponent.SubItem.AMMO_CASING);
		ItemStack pellet = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET);
		ItemStack shell_casing = FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL);
		ItemStack gun_barrel = FoundryItems.component(ItemComponent.SubItem.GUN_BARREL);
		ItemStack revolver_drum = FoundryItems.component(ItemComponent.SubItem.REVOLVER_DRUM);
		ItemStack revolver_frame = FoundryItems.component(ItemComponent.SubItem.REVOLVER_FRAME);
		ItemStack shotgun_pump = FoundryItems.component(ItemComponent.SubItem.SHOTGUN_PUMP);
		ItemStack shotgun_frame = FoundryItems.component(ItemComponent.SubItem.SHOTGUN_FRAME);
		ItemStack bullet_steel = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_STEEL);
		ItemStack pellet_steel = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_STEEL);
		ItemStack bullet_lumium = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_LUMIUM);
		ItemStack pellet_lumium = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_LUMIUM);

		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet), new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.getAmountNugget() * 3));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_hollow), new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.getAmountNugget() * 3));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_jacketed), new FluidStack(FoundryFluids.liquid_copper, FoundryAPI.getAmountNugget()));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_casing), new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.getAmountNugget()));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(pellet), new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.getAmountNugget()));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(shell_casing), new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.getAmountNugget() * 2));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_steel), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.getAmountNugget() * 3));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(pellet_steel), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.getAmountNugget()));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_lumium), new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.getAmountNugget() * 3));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(pellet_lumium), new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.getAmountNugget()));

		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(gun_barrel), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(revolver_drum), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(revolver_frame), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(shotgun_pump), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
		MeltingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(shotgun_frame), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));

		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet), new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.getAmountNugget() * 3), mold_bullet, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_hollow), new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.getAmountNugget() * 3), mold_bullet_hollow, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_jacketed), new FluidStack(FoundryFluids.liquid_copper, FoundryAPI.getAmountNugget()), mold_bullet, new ItemStackMatcher(bullet));
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_casing), new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.getAmountNugget()), mold_round_casing, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(pellet), new FluidStack(FoundryFluids.liquid_lead, FoundryAPI.getAmountNugget()), mold_pellet, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(shell_casing), new FluidStack(FoundryFluids.liquid_brass, FoundryAPI.getAmountNugget() * 2), mold_shell_casing, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_steel), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.getAmountNugget() * 3), mold_bullet, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(pellet_steel), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.getAmountNugget()), mold_pellet, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(bullet_lumium), new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.getAmountNugget() * 3), mold_bullet, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(pellet_lumium), new FluidStack(FoundryFluids.liquid_lumium, FoundryAPI.getAmountNugget()), mold_pellet, null);

		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(gun_barrel), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_gun_barrel, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(revolver_drum), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4), mold_revolver_drum, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(revolver_frame), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_revolver_frame, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(shotgun_pump), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_shotgun_pump, null);
		CastingRecipeManager.INSTANCE.addRecipe(new ItemStackMatcher(shotgun_frame), new FluidStack(FoundryFluids.liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_shotgun_frame, null);

	}
}
