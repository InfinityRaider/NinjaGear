package com.infinityraider.ninjagear.capability;

import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import com.infinityraider.infinitylib.utility.ISerializable;
import com.infinityraider.ninjagear.item.ItemNinjaArmor;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.reference.Reference;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityNinjaArmor implements ICapabilityImplementation<ItemStack, CapabilityNinjaArmor.Impl> {
    private static final CapabilityNinjaArmor INSTANCE = new CapabilityNinjaArmor();

    public static CapabilityNinjaArmor getInstance() {
        return INSTANCE;
    }

    public static ResourceLocation KEY = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "ninja_gear");

    public static boolean isNinjaArmor(ItemStack stack) {
        return stack.getCapability(CapabilityNinjaArmor.CAPABILITY).map(CapabilityNinjaArmor.Impl::isNinjaArmor).orElse(false);
    }

    @CapabilityInject(value = Impl.class)
    public static Capability<Impl> CAPABILITY = null;

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

    public static class Impl implements ISerializable {
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
        public void readFromNBT(CompoundNBT tag) {
            this.isNinjaArmor = tag.contains(Names.NBT.NINJA_GEAR) && tag.getBoolean(Names.NBT.NINJA_GEAR);
        }

        @Override
        public CompoundNBT writeToNBT() {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean(Names.NBT.NINJA_GEAR, this.isNinjaArmor);
            return tag;
        }
    }
}
