package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.network.MessageUpdateGadgetRenderMaskServer;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.item.ItemNinjaArmor;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.function.Supplier;

/**
 * Every client checks for changes in items equipped in the inventory.
 * If a change is detected, a message is sent to the server which sends the change to all other clients.
 * This is necessary because a client is unaware of the inventory of another client.
 */
@OnlyIn(Dist.CLIENT)
public class NinjaGadgetHandler {
    private static final NinjaGadgetHandler INSTANCE = new NinjaGadgetHandler();

    public static NinjaGadgetHandler getInstance() {
        return INSTANCE;
    }

    private boolean hasSent = false;

    private NinjaGadgetHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTick(TickEvent event) {
        if(event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END) {
            Player player = NinjaGear.instance.getClientPlayer();
            if(player == null) {
                return;
            }
            //count relevant items in player's inventory
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if(chest.getItem() instanceof ItemNinjaArmor) {
                for(int i = 0; i < player.getInventory().items.size(); i++) {
                    if(i == player.getInventory().selected) {
                        continue;
                    }
                    ItemStack stack = player.getInventory().getItem(i);
                    if(stack.isEmpty()) {
                        continue;
                    }
                    Gadgets gadget = Gadgets.getGadgetFromItem(stack.getItem());
                    if(gadget != null) {
                        gadget.increment();
                    }
                }
            }
            //if one gadget has changed count or it is the first tick, send the change to the server which then sends it to all clients
            boolean flag = false;
            for(Gadgets gadget : Gadgets.values()) {
                if(gadget.updateAndCheckForChanges()) {
                    flag = true;
                }
            }
            if(flag || !hasSent) {
                new MessageUpdateGadgetRenderMaskServer(Gadgets.getRenderMask()).sendToServer();
                hasSent = true;
            }
        }
    }

    public enum Gadgets {
        KATANA(ItemRegistry.getInstance()::getKatanaItem),
        SAI(ItemRegistry.getInstance()::getSaiItem),
        SHURIKEN(ItemRegistry.getInstance()::getShurikenItem),
        SMOKE_BOMB(ItemRegistry.getInstance()::getSmokeBombItem),
        ROPE_COIL(ItemRegistry.getInstance()::getRopeCoilItem);

        private final Supplier<Item> item;

        private int counter;
        private int prevCount;

        Gadgets(Supplier<Item> item) {
            this.item = item;
        }

        public Item getItem() {
            return this.item.get();
        }

        public void increment() {
            this.counter++;
        }

        public boolean updateAndCheckForChanges() {
            if(counter != prevCount) {
                prevCount = counter;
                counter = 0;
                return true;
            } else {
                prevCount = counter;
                counter = 0;
                return false;
            }
        }

        public static boolean[] getRenderMask() {
            return new boolean[]{
                    KATANA.prevCount > 0,
                    shouldRenderSai(Minecraft.getInstance().player, SAI.prevCount, false),
                    shouldRenderSai(Minecraft.getInstance().player, SAI.prevCount, true),
                    SHURIKEN.prevCount > 0,
                    SMOKE_BOMB.prevCount > 0,
                    ROPE_COIL.prevCount > 0
            };
        }

        private static boolean shouldRenderSai(Player player, int itemCount, boolean left) {
            if(itemCount <= 0) {
                return false;
            }
            if(itemCount >= 2) {
                return true;
            }
            ItemStack main = player.getItemBySlot(EquipmentSlot.MAINHAND);
            ItemStack off = player.getItemBySlot(EquipmentSlot.OFFHAND);
            boolean hasRight = main.getItem() == SAI.getItem();
            boolean hasLeft = off.getItem() == SAI.getItem();
            if(hasLeft) {
                return !left;
            } else if(hasRight){
                return  left;
            }
            return !left;
        }

        public static Gadgets getGadgetFromItem(Item item) {
            if(item == null) {
                return null;
            }
            for(Gadgets gadget : values()) {
                if(gadget.getItem() == item) {
                    return gadget;
                }
            }
            return null;
        }
    }
}
