package com.infinityraider.ninjagear.render.player;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.handler.RenderPlayerHandler;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
                new ItemStack(ItemRegistry.itemKatana),
                new ItemStack(ItemRegistry.itemSai),
                new ItemStack(ItemRegistry.itemSai),
                new ItemStack(ItemRegistry.itemShuriken),
                new ItemStack(ItemRegistry.itemSmokeBomb),
                new ItemStack(ItemRegistry.itemRopeCoil)
        };
    }

    public void updateRenderMask(Player player, boolean[] renderMask) {
        if(player == null) {
            return;
        }
        if(renderMask == null || renderMask.length != itemsToRender.length || isMaskEmpty(renderMask)) {
            renderMap.remove(player.getUUID());
        }
        renderMap.put(player.getUUID(), renderMask);
    }

    private boolean isMaskEmpty(boolean[] mask) {
        for(boolean b : mask) {
            if(b) {
                return false;
            }
        }
        return true;
    }

    private static final Quaternion SNEAK_ROTATION = new Quaternion(new Quaternion(new Vector3f(0.8F, 0, 0), 35, true));

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event) {
        if (!NinjaGear.instance.getConfig().renderGadgets()) {
            return;
        }
        PoseStack transforms = event.getPoseStack();
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        if (RenderPlayerHandler.getInstance().isInvisible(player)) {
            return;
        }
        if (!renderMap.containsKey(player.getUUID())) {
            return;
        }

        boolean[] renderMask = renderMap.get(player.getUUID());
        float f = event.getPartialTick();

        transforms.pushPose();

        float yaw = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO) * f;
        transforms.mulPose(new Quaternion(new Vector3f(0, 1, 0), -yaw, true));

        if (player.isDiscrete()) {
            transforms.translate(0, -0.375, 0.5);
            transforms.mulPose(SNEAK_ROTATION);
            transforms.translate(0, 0, -1.250);
        }

        for (int i = 0; i < renderMask.length; i++) {
            if (renderMask[i]) {
                switch (i) {
                    case ID_KATANA:
                        this.renderKatana(this.itemsToRender[i], event.getPackedLight(), transforms, event.getMultiBufferSource());
                        break;
                    case ID_SAI_RIGHT:
                        this.renderSaiRight(this.itemsToRender[i], event.getPackedLight(), transforms, event.getMultiBufferSource());
                        break;
                    case ID_SAI_LEFT:
                        this.renderSaiLeft(this.itemsToRender[i], event.getPackedLight(), transforms, event.getMultiBufferSource());
                        break;
                    case ID_SHURIKEN:
                        this.renderShuriken(this.itemsToRender[i], event.getPackedLight(), transforms, event.getMultiBufferSource());
                        break;
                    case ID_SMOKE_BOMB:
                        this.renderSmokeBomb(this.itemsToRender[i], event.getPackedLight(), transforms, event.getMultiBufferSource());
                        break;
                    case ID_ROPE_COIL:
                        this.renderRopeCoil(this.itemsToRender[i], event.getPackedLight(), transforms, event.getMultiBufferSource());
                        break;
                }
            }
        }
        transforms.popPose();
    }


    private static final Quaternion KATANA_ROTATION = new Quaternion(new Vector3f(1, 0, 0), 180, true);

    private void renderKatana(ItemStack stack, int light, PoseStack transforms, MultiBufferSource buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.pushPose();

        transforms.translate(0, 1, -0.2);
        transforms.mulPose(KATANA_ROTATION);
        transforms.scale(0.8F, 0.8F, 1);

        renderer.renderStatic(stack, ItemTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer, 0);

        transforms.popPose();
    }

    private static final Quaternion SAI_ROTATION_1 = new Quaternion(new Vector3f(1, 0, 0), 180, true);
    private static final Quaternion SAI_ROTATION_2 = new Quaternion(new Vector3f(0, 1, 0), -90, true);

    private void renderSaiLeft(ItemStack stack, int light, PoseStack transforms, MultiBufferSource buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.pushPose();

        transforms.translate(0, 0.625, -0.1);
        transforms.mulPose(SAI_ROTATION_1);
        transforms.translate(0.275, 0, 0);
        transforms.mulPose(SAI_ROTATION_2);
        transforms.scale(0.5F, 0.5F, 1);

        renderer.renderStatic(stack, ItemTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer, 0);

        transforms.popPose();
    }

    private void renderSaiRight(ItemStack stack, int light, PoseStack transforms, MultiBufferSource buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.pushPose();

        transforms.translate(0, 0.625, -0.1);
        transforms.mulPose(SAI_ROTATION_1);
        transforms.translate(-0.275, 0, 0);
        transforms.mulPose(SAI_ROTATION_2);
        transforms.scale(0.5F, 0.5F, 1);

        renderer.renderStatic(stack, ItemTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer, 0);

        transforms.popPose();
    }

    private void renderShuriken(ItemStack stack, int light, PoseStack transforms, MultiBufferSource buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.pushPose();

        transforms.translate(-0.15, 0.75, 0.2);
        transforms.scale(0.2F, 0.2F, 0.5F);

        renderer.renderStatic(stack, ItemTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer, 0);

        transforms.popPose();
    }

    private static final Quaternion SMOKE_BOMB_ROTATION = new Quaternion(new Vector3f(0, 1, 0), 180, true);

    private void renderSmokeBomb(ItemStack stack, int light, PoseStack transforms, MultiBufferSource buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.pushPose();

        transforms.translate(0, 0.75, -0.2);
        transforms.mulPose(SMOKE_BOMB_ROTATION);
        transforms.translate(0.125, 0, 0);
        transforms.scale(0.3F, 0.3F, 1);

        renderer.renderStatic(stack, ItemTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer, 0);

        transforms.popPose();
    }

    private static final Quaternion ROPE_COIL_ROTATION_1 = new Quaternion(new Vector3f(1, 0, 0), 180, true);
    private static final Quaternion ROPE_COIL_ROTATION_2 = new Quaternion(new Vector3f(0, 1, 0), -90, true);
    private static final Quaternion ROPE_COIL_ROTATION_3 = new Quaternion(new Vector3f(0, 0, 1), -90, true);

    private void renderRopeCoil(ItemStack stack, int light, PoseStack transforms, MultiBufferSource buffer) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

        transforms.pushPose();

        transforms.translate(0, 1.25, -0.15);
        transforms.mulPose(ROPE_COIL_ROTATION_1);
        transforms.translate(0.3, 0, 0);
        transforms.mulPose(ROPE_COIL_ROTATION_2);
        transforms.mulPose(ROPE_COIL_ROTATION_3);
        transforms.scale(0.8F, 0.8F, 1);

        renderer.renderStatic(stack, ItemTransforms.TransformType.NONE, light, NO_OVERLAY, transforms, buffer, 0);

        transforms.popPose();
    }
}
