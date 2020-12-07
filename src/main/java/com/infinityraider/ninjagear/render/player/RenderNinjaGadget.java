package com.infinityraider.ninjagear.render.player;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.handler.RenderPlayerHandler;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

@OnlyIn(Dist.CLIENT)
public class RenderNinjaGadget {
    private static final RenderNinjaGadget INSTANCE = new RenderNinjaGadget();

    public static RenderNinjaGadget getInstance() {
        return INSTANCE;
    }

    private final HashMap<UUID, boolean[]> renderMap;

    private final ItemStack[] itemsToRender;

    private static final int ID_KATANA = 0;
    private static final int ID_SAI_RIGHT = 1;
    private static final int ID_SAI_LEFT = 2;
    private static final int ID_SHURIKEN = 3;
    private static final int ID_SMOKE_BOMB = 4;
    private static final int ID_ROPE_COIL = 5;

    private RenderNinjaGadget() {
        this.renderMap = new HashMap<>();
        this.itemsToRender = new ItemStack[] {
                new ItemStack(ItemRegistry.getInstance().itemKatana),
                new ItemStack(ItemRegistry.getInstance().itemSai),
                new ItemStack(ItemRegistry.getInstance().itemSai),
                new ItemStack(ItemRegistry.getInstance().itemShuriken),
                new ItemStack(ItemRegistry.getInstance().itemSmokeBomb),
                new ItemStack(ItemRegistry.getInstance().itemRopeCoil)
        };
    }

    public void updateRenderMask(PlayerEntity player, boolean[] renderMask) {
        if(player == null) {
            return;
        }
        if(renderMask == null || renderMask.length != itemsToRender.length || isMaskEmpty(renderMask)) {
            renderMap.remove(player.getUniqueID());
        }
        renderMap.put(player.getUniqueID(), renderMask);
    }

    private boolean isMaskEmpty(boolean[] mask) {
        for(boolean b : mask) {
            if(b) {
                return false;
            }
        }
        return true;
    }

    private static final Quaternion SNEAK_ROTATION = new Quaternion(new Vector3f(0, 0, -0.8F), 30, true);

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event) {
        if (!NinjaGear.instance.getConfig().renderGadgets()) {
            return;
        }
        MatrixStack transforms = event.getMatrixStack();
        PlayerEntity player = event.getPlayer();
        if (player == null) {
            return;
        }
        if (RenderPlayerHandler.getInstance().isInvisible(player)) {
            return;
        }
        if (!renderMap.containsKey(player.getUniqueID())) {
            return;
        }

        boolean[] renderMask = renderMap.get(player.getUniqueID());
        float f = event.getPartialRenderTick();

        transforms.push();

        if (player != NinjaGear.instance.getClientPlayer()) {
            PlayerEntity local = NinjaGear.instance.getClientPlayer();
            double dx = player.prevPosX + (player.getPosX() - player.prevPosX) * f - (local.prevPosX + (local.getPosX() - local.prevPosX) * f);
            double dy = player.prevPosY + (player.getPosY() - player.prevPosY) * f - (local.prevPosY + (local.getPosY() - local.prevPosY) * f);
            double dz = player.prevPosZ + (player.getPosZ() - player.prevPosZ) * f - (local.prevPosZ + (local.getPosZ() - local.prevPosZ) * f);
            transforms.translate(dx, dy, dz);
        }

        float yaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * f;
        transforms.rotate(new Quaternion(new Vector3f(0, 1, 0), -yaw, true));

        if (player.isSneaking()) {
            transforms.translate(0, -0.375, 0);
            transforms.rotate(SNEAK_ROTATION);
            transforms.translate(0, 0, -0.8);
        }

        for (int i = 0; i < renderMask.length; i++) {
            if (renderMask[i]) {
                switch (i) {
                    case ID_KATANA:
                        this.renderKatana(this.itemsToRender[i], event.getLight(), transforms, event.getBuffers());
                        break;
                    case ID_SAI_RIGHT:
                        this.renderSaiRight(this.itemsToRender[i], event.getLight(), transforms, event.getBuffers());
                        break;
                    case ID_SAI_LEFT:
                        this.renderSaiLeft(this.itemsToRender[i], event.getLight(), transforms, event.getBuffers());
                        break;
                    case ID_SHURIKEN:
                        this.renderShuriken(this.itemsToRender[i], event.getLight(), transforms, event.getBuffers());
                        break;
                    case ID_SMOKE_BOMB:
                        this.renderSmokeBomb(this.itemsToRender[i], event.getLight(), transforms, event.getBuffers());
                        break;
                    case ID_ROPE_COIL:
                        this.renderRopeCoil(this.itemsToRender[i], event.getLight(), transforms, event.getBuffers());
                        break;
                }
            }
        }
        transforms.pop();
    }


    private static final Quaternion KATANA_ROTATION = new Quaternion(new Vector3f(1, 0, 0), 180, true);

    private void renderKatana(ItemStack stack, int light, MatrixStack transforms, IRenderTypeBuffer buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.push();

        transforms.translate(0, 1, -0.2);
        transforms.rotate(KATANA_ROTATION);
        transforms.scale(0.8F, 0.8F, 1);

        renderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer);

        transforms.pop();
    }

    private static final Quaternion SAI_ROTATION_1 = new Quaternion(new Vector3f(1, 0, 0), 180, true);
    private static final Quaternion SAI_ROTATION_2 = new Quaternion(new Vector3f(0, 1, 0), -90, true);

    private void renderSaiLeft(ItemStack stack, int light, MatrixStack transforms, IRenderTypeBuffer buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.push();

        transforms.translate(0, 0.625, -0.1);
        transforms.rotate(SAI_ROTATION_1);
        transforms.translate(0.275, 0, 0);
        transforms.rotate(SAI_ROTATION_2);
        transforms.scale(0.5F, 0.5F, 1);

        renderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer);

        transforms.pop();
    }

    private void renderSaiRight(ItemStack stack, int light, MatrixStack transforms, IRenderTypeBuffer buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.push();

        transforms.translate(0, 0.625, -0.1);
        transforms.rotate(SAI_ROTATION_1);
        transforms.translate(-0.275, 0, 0);
        transforms.rotate(SAI_ROTATION_2);
        transforms.scale(0.5F, 0.5F, 1);

        renderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer);

        transforms.pop();
    }

    private void renderShuriken(ItemStack stack, int light, MatrixStack transforms, IRenderTypeBuffer buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.push();

        transforms.translate(-0.15, 0.75, 0.2);
        transforms.scale(0.2F, 0.2F, 0.5F);

        renderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer);

        transforms.pop();
    }

    private static final Quaternion SMOKE_BOMB_ROTATION = new Quaternion(new Vector3f(0, 1, 0), 180, true);

    private void renderSmokeBomb(ItemStack stack, int light, MatrixStack transforms, IRenderTypeBuffer buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.push();

        transforms.translate(0, 0.75, -0.2);
        transforms.rotate(SMOKE_BOMB_ROTATION);
        transforms.translate(0.125, 0, 0);
        transforms.scale(0.3F, 0.3F, 1);

        renderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer);

        transforms.pop();
    }

    private static final Quaternion ROPE_COIL_ROTATION_1 = new Quaternion(new Vector3f(1, 0, 0), 180, true);
    private static final Quaternion ROPE_COIL_ROTATION_2 = new Quaternion(new Vector3f(0, 1, 0), -90, true);
    private static final Quaternion ROPE_COIL_ROTATION_3 = new Quaternion(new Vector3f(0, 0, 1), -90, true);

    private void renderRopeCoil(ItemStack stack, int light, MatrixStack transforms, IRenderTypeBuffer buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.push();

        transforms.translate(0, 1.25, -0.15);
        transforms.rotate(ROPE_COIL_ROTATION_1);
        transforms.translate(0.3, 0, 0);
        transforms.rotate(ROPE_COIL_ROTATION_2);
        transforms.rotate(ROPE_COIL_ROTATION_3);
        transforms.scale(0.8F, 0.8F, 1);

        renderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer);

        transforms.pop();
    }
}
