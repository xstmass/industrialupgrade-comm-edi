package com.denfop.api.recipe;

import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Objects;

public class BaseMachineRecipe {

    public final IInput input;
    public final RecipeOutput output;

    public BaseMachineRecipe(IInput input, RecipeOutput output) {
        this.input = input;
        this.output = output;
    }

    public boolean matches(List<ItemStack> stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            if (this.input.getInputs().get(i).matches(stacks.get(i))) {
                return true;
            }
        }
        return false;
    }

    public RecipeOutput getOutput() {
        return this.output;
    }



}
