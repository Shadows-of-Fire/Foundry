package exter.foundry.integration.jei;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import exter.foundry.Foundry;
import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.gui.GuiAlloyingCrucible;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class AlloyingCrucibleJEI {

	static public class Category implements IRecipeCategory<Wrapper> {

		protected final ResourceLocation backgroundLocation;

		private final IDrawable background;

		private final String localizedName;

		private final IDrawable tank_overlay;

		public Category(IJeiHelpers helpers) {
			IGuiHelper guiHelper = helpers.getGuiHelper();
			backgroundLocation = new ResourceLocation("foundry", "textures/gui/alloyingcrucible.png");

			ResourceLocation location = new ResourceLocation("foundry", "textures/gui/alloyingcrucible.png");
			background = guiHelper.createDrawable(location, 33, 43, 110, 39);
			tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 35);
			localizedName = Translator.translateToLocal("gui.jei.alloyingcrucible");
		}

		@Override
		public void drawExtras(Minecraft minecraft) {

		}

		@Override

		public IDrawable getBackground() {
			return background;
		}

		@Override
		public IDrawable getIcon() {
			return null;
		}

		@Override
		public String getModName() {
			return Foundry.MODID;
		}

		@Override
		public String getTitle() {
			return localizedName;
		}

		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			return Collections.emptyList();
		}

		@Override
		public String getUid() {
			return FoundryJEIConstants.AC_UID;
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
			IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

			FluidStack out = ingredients.getOutputs(VanillaTypes.FLUID).get(0).get(0);
			List<FluidStack> in_a = ingredients.getInputs(VanillaTypes.FLUID).get(0);
			List<FluidStack> in_b = ingredients.getInputs(VanillaTypes.FLUID).get(1);

			int amount = Integer.max(out.amount, Integer.max(in_a.get(0).amount, in_b.get(0).amount));

			guiFluidStacks.init(0, true, 35 - 33, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, amount, false, tank_overlay);
			guiFluidStacks.init(1, true, 92, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, amount, false, tank_overlay);
			guiFluidStacks.init(2, false, 47, 2, 16, GuiAlloyingCrucible.TANK_HEIGHT, amount, false, tank_overlay);

			guiFluidStacks.set(0, in_a);
			guiFluidStacks.set(1, in_b);
			guiFluidStacks.set(2, out);
		}
	}

	static public class Wrapper implements IRecipeWrapper {
		private final IAlloyingCrucibleRecipe recipe;

		public Wrapper(IAlloyingCrucibleRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

		}

		@Override
		public boolean equals(Object other) {
			return recipe == other;
		}

		@Override
		public void getIngredients(IIngredients ingredients) {
			ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput());
			ingredients.setInputs(VanillaTypes.FLUID, ImmutableList.of(recipe.getInputA(), recipe.getInputB()));
		}

		@Override
		public List<String> getTooltipStrings(int mouseX, int mouseY) {
			return null;
		}

		@Override
		public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
			return false;
		}
	}
}
