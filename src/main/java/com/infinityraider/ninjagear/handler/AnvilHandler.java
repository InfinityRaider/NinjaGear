package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.capability.CapabilityNinjaArmor;
import com.infinityraider.ninjagear.item.ItemNinjaArmor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AnvilHandler {
    private static final AnvilHandler INSTANCE = new AnvilHandler();

    public static AnvilHandler getInstance() {
        return INSTANCE;
    }

    private AnvilHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onAnvilUse(AnvilUpdateEvent event) {
        // Fetch input stacks
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        // Verify imbuing conditions
        if(left == null || right == null || left.isEmpty() || right.isEmpty()) {
            // Do nothing if either slot is empty
            return;
        }
        if(!(left.getItem() instanceof ArmorItem)) {
            // Do nothing if the left slot does not contain an armor piece
            return;
        }
        if(!(right.getItem() instanceof ArmorItem)) {
            // Do nothing if the right slot does not contain an armor piece
            return;
        }
        if((left.getItem() instanceof ItemNinjaArmor) && (right.getItem() instanceof ItemNinjaArmor)) {
            // Do nothing if both slots contain ninja armor
            return;
        }
        if(!(left.getItem() instanceof ItemNinjaArmor) && !(right.getItem() instanceof ItemNinjaArmor)) {
            // Do nothing if neither slot contains ninja armor
            return;
        }
        if(((ArmorItem) left.getItem()).getEquipmentSlot(left) != ((ArmorItem) right.getItem()).getEquipmentSlot(right)) {
            // Do nothing if the slots contain armor pieces for different body parts
            return;
        }
        // Fetch the input ItemStack
        ItemStack input = (left.getItem() instanceof ItemNinjaArmor) ? right : left;
        // Check if the input stack is imbued already
        if(CapabilityNinjaArmor.isNinjaArmor(input)) {
            // Do nothing if the input stack is already imbued
            return;
        }
        // Copy the input ItemStack
        ItemStack output = input.copy();
        // Activate the ninja armor status on the capability
        output.getCapability(CapabilityNinjaArmor.CAPABILITY).ifPresent(cap -> cap.setNinjaArmor(true));
        // Set the name
        String inputName = event.getName();
        if(inputName == null || inputName.isEmpty()) {
            output.resetHoverName();
        } else {
            output.setHoverName(new TextComponent(inputName));
        }
        // Set the output
        event.setOutput(output);
        // Set the cost
        event.setCost(NinjaGear.instance.getConfig().getImbueCost());
    }
}
