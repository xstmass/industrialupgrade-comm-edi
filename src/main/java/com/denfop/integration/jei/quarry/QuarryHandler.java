package com.denfop.integration.jei.quarry;


import com.denfop.IUCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuarryHandler {

    private static final List<QuarryHandler> recipes = new ArrayList<>();


    public static List<QuarryHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    private final ItemStack output;


    public QuarryHandler(ItemStack output) {
        this.output = output;
    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }


    public static QuarryHandler addRecipe(ItemStack output) {
        QuarryHandler recipe = new QuarryHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static QuarryHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (QuarryHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == output.getItem();
    }

    public static void initRecipes() {
        for (ItemStack container : IUCore.list) {
            addRecipe(container);

        }
    }

    private static ItemStack is(Item item) { // Побочный метод.
        return new ItemStack(item);
    }

    private static ItemStack is(Block block) { // Побочный метод.
        return new ItemStack(block);
    }

}
