package com.InfinityRaider.ninjagear.render.player;

import com.InfinityRaider.ninjagear.NinjaGear;
import com.InfinityRaider.ninjagear.handler.RenderPlayerHandler;
import com.InfinityRaider.ninjagear.registry.ItemRegistry;
import com.InfinityRaider.ninjagear.render.RenderUtilBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class RenderNinjaGadget extends RenderUtilBase {
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

    public void updateRenderMask(EntityPlayer player, boolean[] renderMask) {
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

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        if(player == null) {
            return;
        }
        if(RenderPlayerHandler.getInstance().isInvisible(player)) {
            return;
        }
        if(!renderMap.containsKey(player.getUniqueID())) {
            return;
        }

        boolean[] renderMask = renderMap.get(player.getUniqueID());
        float f = event.getPartialRenderTick();

        GlStateManager.pushMatrix();

        if(player != NinjaGear.proxy.getClientPlayer()) {
            EntityPlayer local = NinjaGear.proxy.getClientPlayer();
            double dx = player.prevPosX + (player.posX - player.prevPosX)*f - (local.prevPosX + (local.posX - local.prevPosX)*f);
            double dy = player.prevPosY + (player.posY - player.prevPosY)*f - (local.prevPosY + (local.posY - local.prevPosY)*f);
            double dz = player.prevPosZ + (player.posX - player.prevPosX)*f - (local.prevPosZ + (local.posZ - local.prevPosZ)*f);
            GlStateManager.translate(dx, dy, dz);
        }

        float yaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset)*f;
        GlStateManager.rotate(-yaw, 0, 1, 0);

        if(player.isSneaking()) {
            GlStateManager.translate(0, - 0.375, 0);
            GlStateManager.rotate(30, 1, 0, 0);
            GlStateManager.translate(0, 0, -0.8);
        }

        for(int i = 0; i < renderMask.length; i++) {
            if(renderMask[i]) {
                switch(i) {
                    case ID_KATANA: this.renderKatana(i); break;
                    case ID_SAI_RIGHT: this.renderSaiRight(i); break;
                    case ID_SAI_LEFT: this.renderSaiLeft(i); break;
                    case ID_SHURIKEN: this.renderShuriken(i); break;
                    case ID_SMOKE_BOMB: this.renderSmokeBomb(i); break;
                    case ID_ROPE_COIL: this.renderRopeCoil(i); break;
                }
            }
        }

        GlStateManager.popMatrix();
    }

    private void renderKatana(int id) {
        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.translate(0, 0, -0.2);
        GlStateManager.translate(0, 1.125, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.scale(0.8, 0.8, 1);

        renderer.renderItem(this.itemsToRender[id], ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }

    private void renderSaiLeft(int id) {
        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.translate(0, 0, -0.1);
        GlStateManager.translate(0, 0.625, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0.275, 0, 0);
        GlStateManager.rotate(-90, 0, 1, 0);
        GlStateManager.scale(0.5, 0.5, 1);

        renderer.renderItem(this.itemsToRender[id], ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }

    private void renderSaiRight(int id) {
        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.translate(0, 0, -0.1);
        GlStateManager.translate(0, 0.625, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(-0.275, 0, 0);
        GlStateManager.rotate(-90, 0, 1, 0);
        GlStateManager.scale(0.5, 0.5, 1);

        renderer.renderItem(this.itemsToRender[id], ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }

    private void renderShuriken(int id) {
        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.translate(0, 0, 0.2);
        GlStateManager.translate(0, 0.75, 0);
        GlStateManager.translate(-0.15, 0, 0);
        GlStateManager.scale(0.2, 0.2, 0.5);

        renderer.renderItem(this.itemsToRender[id], ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }

    private void renderSmokeBomb(int id) {
        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.translate(0, 0, -0.2);
        GlStateManager.translate(0, 0.75, 0);
        GlStateManager.rotate(180, 0, 1, 0);
        GlStateManager.translate(0.125, 0, 0);
        GlStateManager.scale(0.3, 0.3, 1);

        renderer.renderItem(this.itemsToRender[id], ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }

    private void renderRopeCoil(int id) {
        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.translate(0, 0, -0.15);
        GlStateManager.translate(0, 1.25, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0.3, 0, 0);
        GlStateManager.rotate(-90, 0, 1, 0);
        GlStateManager.rotate(-90, 0, 0, 1);
        GlStateManager.scale(0.8, 0.8, 1);

        renderer.renderItem(this.itemsToRender[id], ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }
}
