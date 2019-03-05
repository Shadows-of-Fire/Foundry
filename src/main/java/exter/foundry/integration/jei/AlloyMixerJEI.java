package exter.foundry.integration.jei;

import java.util.Collections;
import java.util.List;

import exter.foundry.Foundry;
import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.gui.GuiAlloyMixer;
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
import shadows.placebo.util.PlaceboUtil;

public class AlloyMixerJEI {

	static public class Category implements IRecipeCategory<Wrapper> {

		protected final ResourceLocation backgroundLocation;

		private final IDrawable background;

		private final String localizedName;

		private final IDrawable tank_overlay;

		public Category(IJeiHelpers helpers) {
			IGuiHelper guiHelper = helpers.getGuiHelper();
			backgroundLocation = new ResourceLocation("foundry", "textures/gui/alloymixer.png");

			ResourceLocation location = new ResourceLocation("foundry", "textures/gui/alloymixer.png");
			background = guiHelper.createDrawable(location, 18, 44, 132, 37);
			tank_overlay = guiHelper.createDrawable(location, 176, 0, 16, 35);
			localizedName = Translator.translateToLocal("gui.jei.alloymixer");
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
			return FoundryJEIConstants.AM_UID;
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, Wrapper recipeWrapper, IIngredients ingredients) {
			IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

			List<List<FluidStack>> in = ingredients.getInputs(VanillaTypes.FLUID);
			FluidStack out = ingredients.getOutputs(VanillaTypes.FLUID).get(0).get(0);
			int out_amount = out.amount;
			for (int i = 0; i < in.size(); i++) {
				int amount = in.get(i).get(0).amount;
				if (amount > out_amount) {
					out_amount = amount;
				}
			}

			guiFluidStacks.init(5, false, 115, 1, 16, GuiAlloyMixer.TANK_HEIGHT, out_amount, false, tank_overlay);
			for (int i = 0; i < in.size(); i++) {
				guiFluidStacks.init(i, true, 8 + 21 * i, 1, 16, GuiAlloyMixer.TANK_HEIGHT, out_amount, false, tank_overlay);
				guiFluidStacks.set(i, in.get(i));
			}
			guiFluidStacks.set(5, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
		}
	}

	static public class Wrapper implements IRecipeWrapper {
		private final IAlloyMixerRecipe recipe;

		public Wrapper(IAlloyMixerRecipe recipe) {
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
			ingredients.setInputs(VanillaTypes.FLUID, PlaceboUtil.asList(recipe.getInputs()));
			ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutput());
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
