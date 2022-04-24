package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.utility.registration.ModContentRegistry;
import com.infinityraider.infinitylib.utility.registration.RegistryInitializer;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.item.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class ItemRegistry extends ModContentRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    public static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(Reference.MOD_ID.toLowerCase() + ".creative_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.getInstance().getShurikenItem());
        }
    };

    private final RegistryInitializer<ItemNinjaArmor> ninaArmorHead;
    private final RegistryInitializer<ItemNinjaArmor> ninjaArmorChest;
    private final RegistryInitializer<ItemNinjaArmor> ninjaArmorLegs;
    private final RegistryInitializer<ItemNinjaArmor> ninjaArmorFeet;
    private final RegistryInitializer<ItemKatana> katana;
    private final RegistryInitializer<ItemSai> sai;
    private final RegistryInitializer<ItemShuriken> shuriken;
    private final RegistryInitializer<ItemSmokeBomb> smokeBomb;
    private final RegistryInitializer<ItemRopeCoil> ropeCoil;
    private final RegistryInitializer<ItemRope> rope;

    private ItemRegistry() {
        this.ninaArmorHead = this.item(() -> new ItemNinjaArmor("ninja_gear_head",EquipmentSlot.HEAD));
        this.ninjaArmorChest = this.item(() -> new ItemNinjaArmor("ninja_gear_chest", EquipmentSlot.CHEST));
        this.ninjaArmorLegs = this.item(() -> new ItemNinjaArmor("ninja_gear_legs", EquipmentSlot.LEGS));
        this.ninjaArmorFeet = this.item(() -> new ItemNinjaArmor("ninja_gear_feet", EquipmentSlot.FEET));
        this.katana = this.item(ItemKatana::new);
        this.sai = this.item(ItemSai::new);
        this.shuriken = this.item(ItemShuriken::new);
        this.smokeBomb = this.item(ItemSmokeBomb::new);
        this.ropeCoil = this.item(ItemRopeCoil::new);
        this.rope = this.item(ItemRope::new);
    }

    public ItemNinjaArmor getNinjaArmorHeadItem() {
        return this.ninaArmorHead.get();
    }

    public ItemNinjaArmor getNinjaArmorChestItem() {
        return this.ninjaArmorChest.get();
    }

    public ItemNinjaArmor getNinjaArmorLegsItem() {
        return this.ninjaArmorLegs.get();
    }

    public ItemNinjaArmor getNinjaArmorFeetItem() {
        return this.ninjaArmorFeet.get();
    }

    public ItemKatana getKatanaItem() {
        return this.katana.get();
    }

    public ItemSai getSaiItem() {
        return this.sai.get();
    }

    public ItemShuriken getShurikenItem() {
        return this.shuriken.get();
    }

    public ItemSmokeBomb getSmokeBombItem() {
        return this.smokeBomb.get();
    }

    public ItemRopeCoil getRopeCoilItem() {
        return this.ropeCoil.get();
    }

    public ItemRope getRopeItem() {
        return this.rope.get();
    }
}
