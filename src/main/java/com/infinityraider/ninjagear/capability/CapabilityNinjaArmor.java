package com.infinityraider.ninjagear.capability;

import com.infinityraider.infinitylib.capability.IInfSerializableCapabilityImplementation;
import com.infinityraider.ninjagear.capability.CapabilityNinjaArmor.Impl;
import com.infinityraider.ninjagear.item.ItemNinjaArmor;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.reference.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class CapabilityNinjaArmor implements IInfSerializableCapabilityImplementation<ItemStack, Impl> {
    private static final CapabilityNinjaArmor INSTANCE = new CapabilityNinjaArmor();

    public static CapabilityNinjaArmor getInstance() {
        return INSTANCE;
    }

    public static ResourceLocation KEY = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "ninja_gear");

    public static boolean isNinjaArmor(ItemStack stack) {
        return stack.getCapability(CapabilityNinjaArmor.CAPABILITY).map(CapabilityNinjaArmor.Impl::isNinjaArmor).orElse(false);
    }

    public static Capability<Impl> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    private CapabilityNinjaArmor() {}

    @Override
    public Capability<Impl> getCapability() {
        return CAPABILITY;
    }

    @Override
    public boolean shouldApplyCapability(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem;
    }

    @Override
    public Impl createNewValue(ItemStack stack) {
        return new Impl(stack);
    }

    @Override
    public ResourceLocation getCapabilityKey() {
        return KEY;
    }

    @Override
    public Class<ItemStack> getCarrierClass() {
        return ItemStack.class;
    }

    @Override
    public Class<Impl> getCapabilityClass() {
        return Impl.class;
    }

    public static class Impl implements Serializable<Impl> {
        private boolean isNinjaArmor;

        private Impl(ItemStack stack) {
            this.isNinjaArmor = stack.getItem() instanceof ItemNinjaArmor;
        }

        public boolean isNinjaArmor() {
            return this.isNinjaArmor;
        }

        public void setNinjaArmor(boolean value) {
            this.isNinjaArmor = value;
        }

        @Override
        public void copyDataFrom(Impl from) {
            this.isNinjaArmor = from.isNinjaArmor();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putBoolean(Names.NBT.NINJA_GEAR, this.isNinjaArmor);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            this.isNinjaArmor = tag.contains(Names.NBT.NINJA_GEAR) && tag.getBoolean(Names.NBT.NINJA_GEAR);
        }
    }
}
