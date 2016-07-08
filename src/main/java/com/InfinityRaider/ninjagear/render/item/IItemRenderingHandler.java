package com.infinityraider.ninjagear.render.item;

import com.infinityraider.ninjagear.render.tessellation.ITessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IItemRenderingHandler<I extends Item> {
    /**
     * @return The item this handler should handle rendering for
     */
    I getItem();

    /**
     * Returns a list containing a ResourceLocation for every texture used to render this Item.
     * Passed textures are stitched to the Minecraft texture map and icons can be retrieved from them.
     * @return a list of ResourceLocations
     */
    List<ResourceLocation> getAllTextures();

    /**
     * Called to render the item
     * startDrawing() has already been called on the tessellator object.
     *
     * @param tessellator tessellator object to draw quads
     * @param world the world object
     * @param stack stack containing this block as an item
     * @param entity entity holding the stack
     * @param type camera transform type
     */
    void renderItem(ITessellator tessellator, World world, I item,
                    ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type);

}
