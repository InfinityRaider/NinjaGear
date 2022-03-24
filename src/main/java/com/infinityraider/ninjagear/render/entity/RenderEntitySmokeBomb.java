package com.infinityraider.ninjagear.render.entity;

import com.infinityraider.infinitylib.render.entity.RenderEntityAsItem;
import com.infinityraider.ninjagear.entity.EntitySmokeBomb;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntitySmokeBomb extends RenderEntityAsItem<EntitySmokeBomb> {
    public RenderEntitySmokeBomb(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ItemStack(ItemRegistry.itemSmokeBomb));
    }

    @Override
    public void applyTransformations(EntitySmokeBomb entity, float yaw, float partialTicks, PoseStack transforms) {
        //NOOP
    }
}
