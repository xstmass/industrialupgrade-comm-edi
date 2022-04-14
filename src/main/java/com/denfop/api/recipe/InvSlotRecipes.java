package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.upgrade.ItemUpgradeModule;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class InvSlotRecipes extends InvSlot {

    private final IBaseRecipe recipe;
    private final IUpdateTick tile;
    private FluidTank tank;

    public InvSlotRecipes(final TileEntityInventory base, IBaseRecipe baseRecipe, IUpdateTick tile) {
        super(base, "input", Access.I, baseRecipe.getSize());
        this.recipe = baseRecipe;
        this.tile = tile;
        this.tank = null;
    }

    public InvSlotRecipes(final TileEntityInventory base, String baseRecipe, IUpdateTick tile) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile);

    }

    public InvSlotRecipes(final TileEntityInventory base, String baseRecipe, IUpdateTick tile, FluidTank tank) {
        this(base, Recipes.recipes.getRecipe(baseRecipe), tile);
        this.tank = tank;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        this.tile.setRecipeOutput(this.process());
        this.tile.onUpdate();
    }

    @Override
    public boolean accepts(final ItemStack itemStack) {
        return !itemStack.isEmpty() && !(itemStack.getItem() instanceof ItemUpgradeModule);
    }

    public void consume(int number, int amount) {
        this.consume(number, amount, false, false);
    }

    public void consume(int number, int amount, boolean simulate, boolean consumeContainers) {


        ItemStack stack = this.get(number);
        if (!stack.isEmpty() && stack.getCount() >= 1 && this.accepts(stack) && (stack.getCount() >= 1 || consumeContainers || !stack
                .getItem()
                .hasContainerItem(stack))) {
            int currentAmount = Math.min(amount, stack.getCount());
            if (!simulate) {
                if (stack.getCount() == currentAmount) {
                    if (!consumeContainers && stack.getItem().hasContainerItem(stack)) {
                        this.put(number, stack.getItem().getContainerItem(stack));
                    } else {
                        this.put(number, null);
                    }
                } else {
                    stack.setCount(stack.getCount() - currentAmount);
                }
            }


        }


    }

    @Override
    public ItemStack get(final int index) {
        return super.get(index);
    }

    public RecipeOutput process() {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).isEmpty()) {
                return null;
            }
        }
        final RecipeOutput output = getOutputFor();
        if (this.tile instanceof TileEntityConverterSolidMatter) {
            TileEntityConverterSolidMatter mechanism = (TileEntityConverterSolidMatter) this.tile;
            mechanism.getrequiredmatter(output);
        }

        return output;
    }

    public RecipeOutput consume() {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).isEmpty()) {
                return null;
            }
        }
        List<ItemStack> list = new ArrayList<>();
        this.forEach(list::add);
        if (this.tank == null) {
            return Recipes.recipes.getRecipeOutput(this.recipe.getName(), this.recipe.consume(), list);
        } else {
            return Recipes.recipes.getRecipeOutputFluid(this.recipe.getName(), this.recipe.consume(), list, this.tank);
        }

    }

    private RecipeOutput getOutputFor() {
        List<ItemStack> list = new ArrayList<>();
        this.forEach(list::add);
        if (this.tank == null) {
            return Recipes.recipes.getRecipeOutput(this.recipe.getName(), false, list);
        } else {
            return Recipes.recipes.getRecipeOutputFluid(this.recipe.getName(), false, list, this.tank);
        }

    }

}
