package com.denfop.integration.jei.plasticcratorplate;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import ic2.core.init.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PlasticCreatorPlateCategory extends Gui implements IRecipeCategory<PlasticCreatorPlateWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public PlasticCreatorPlateCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPlasticPlate" +
                        ".png"), 5, 5, 140,
                75
        );
    }

    @Override
    public String getUid() {
        return BlockBaseMachine2.plastic_plate_creator.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine1, 1, 13).getUnlocalizedName());
    }

    @Override
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void drawExtras(final Minecraft mc) {
        progress++;
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);
        int xScale = 24 * progress / 100;

        if (xScale > 24) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(+51 + 1, +31 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(+74, +29, 176, 14, xScale + 1, 16);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final PlasticCreatorPlateWrapper recipes,
            final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 50, 11);

        isg.set(0, recipes.getInput());

        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(1, true, 5, 4, 12, 47, 10000, true, null);
        fff.set(1, recipes.getInput2());

        isg.init(2, false, 110, 28);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPlasticPlate.png");
    }


}
