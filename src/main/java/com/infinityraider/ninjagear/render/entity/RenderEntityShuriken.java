package com.infinityraider.ninjagear.render.entity;

import com.infinityraider.infinitylib.render.entity.RenderEntityAsItem;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntityShuriken extends RenderEntityAsItem<EntityShuriken> {

    public RenderEntityShuriken(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ItemStack(ItemRegistry.getInstance().getShurikenItem()));
    }

    @Override
    public void applyTransformations(EntityShuriken entity, float yaw, float partialTicks, PoseStack transforms) {
        //translate and rotate according to throwing heading
        Vec3 direction = entity.direction();
        if(direction != null) {
            double vX = entity.direction().x();
            double vY = entity.direction().y();
            double vZ = entity.direction().z();
            double alpha = 180 * Math.atan2(vZ, vX) / Math.PI;
            double beta = 180 * Math.atan2(vY, Math.sqrt(vX * vX + vZ * vZ)) / Math.PI;
            transforms.mulPose(Vector3f.YP.rotationDegrees((float) -alpha));
            transforms.mulPose(Vector3f.ZP.rotationDegrees((float) beta));

            //rotate around z-axis
            int period = 600;
            int time = (int) (System.currentTimeMillis() % period);
            float angle = -360 * ((float) time) / ((float) period);
            float dy = -0.125F;
            transforms.mulPose(Vector3f.ZP.rotationDegrees(angle));
            transforms.translate(0, dy, 0);
        }
    }
}
