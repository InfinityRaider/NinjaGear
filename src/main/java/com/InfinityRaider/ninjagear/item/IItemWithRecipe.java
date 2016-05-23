package com.InfinityRaider.ninjagear.item;

import net.minecraft.item.crafting.IRecipe;

import java.util.List;

/**
 * Interface used to ease recipe registering
 */
public interface IItemWithRecipe {
    List<IRecipe> getRecipes();
}
