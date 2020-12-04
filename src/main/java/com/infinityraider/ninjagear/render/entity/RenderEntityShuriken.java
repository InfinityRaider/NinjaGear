package com.infinityraider.ninjagear.render.entity;

import com.infinityraider.infinitylib.render.entity.RenderEntityAsItem;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
public class RenderEntityShuriken extends RenderEntityAsItem<EntityShuriken> {

    public RenderEntityShuriken(EntityRendererManager renderManager) {
        super(renderManager, new ItemStack(ItemRegistry.getInstance().itemShuriken));
    }

    @Override
    public void applyTransformations(EntityShuriken entity, float yaw, float partialTicks, MatrixStack transforms) {
        //translate and rotate according to throwing heading
        double vX = entity.getDirection().getX();
        double vY = entity.getDirection().getY();
        double vZ = entity.getDirection().getZ();
        double alpha = 180*Math.atan2(vZ, vX)/Math.PI;
        double beta = 180*Math.atan2(vY, Math.sqrt(vX*vX + vZ*vZ))/Math.PI;
        transforms.rotate(Vector3f.YP.rotationDegrees((float) -alpha));
        transforms.rotate(Vector3f.ZP.rotationDegrees((float) beta));

        //rotate around z-axis
        int period = 600;
        int time = (int) (System.currentTimeMillis() % period);
        float angle = -360 * ((float) time) / ((float) period);
        float dy = -0.125F;
        transforms.rotate(Vector3f.ZP.rotationDegrees(angle));
        transforms.translate(0, dy, 0);
    }
}
