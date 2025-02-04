package com.denfop.tiles.mechanism;

import com.denfop.api.Recipes;
import com.denfop.container.ContainerBaseWitherMaker;
import com.denfop.gui.GUIWitherMaker;
import com.denfop.invslot.InvSlotProcessableWitherMaker;
import com.denfop.tiles.base.TileEntityBaseWitherMaker;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityWitherMaker extends TileEntityBaseWitherMaker {

    public TileEntityWitherMaker() {
        super(1, 1500, 1);
        this.inputSlotA = new InvSlotProcessableWitherMaker(this, "inputA", 7);

    }

    public static void init() {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        GenerationMicrochip(
                input.forStack(new ItemStack(Items.SKULL, 1, 1), 1),
                input.forStack(new ItemStack(Blocks.SOUL_SAND), 1),
                new ItemStack(Items.NETHER_STAR, 1)
        );

    }

    public static void GenerationMicrochip(
            IRecipeInput container,
            IRecipeInput fill2, ItemStack output
    ) {
        Recipes.withermaker.addRecipe(container, container, fill2, fill2, fill2, container, fill2, output);

    }

    public String getInventoryName() {

        return "wither maker";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIWitherMaker(new ContainerBaseWitherMaker(entityPlayer, this));
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
        );
    }

}
