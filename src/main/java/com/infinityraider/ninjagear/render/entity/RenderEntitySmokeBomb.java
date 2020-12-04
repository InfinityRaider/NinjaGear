package com.infinityraider.ninjagear.render.entity;

import com.infinityraider.infinitylib.render.entity.RenderEntityAsItem;
import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntitySmokeBomb extends RenderEntityAsItem<EntitySmokeBomb> {

    public RenderEntitySmokeBomb(EntityRendererManager renderManager) {
        super(renderManager, new ItemStack(ItemRegistry.getInstance().itemSmokeBomb));
    }

    @Override
    public void applyTransformations(EntitySmokeBomb entity, float yaw, float partialTicks, MatrixStack transforms) {
        //NOOP
    }
}
