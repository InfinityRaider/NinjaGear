package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.item.IInfinityItem;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.IItemWithRecipe;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.registry.PotionRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemNinjaArmor extends ItemArmor implements IInfinityItem, IItemWithModel, IItemWithRecipe {
    private static ArmorMaterial ninjaCloth;

    private final String internalName;

    public ItemNinjaArmor(String name, int renderIndex, EntityEquipmentSlot equipmentSlot) {
        super(getMaterial(), renderIndex, equipmentSlot);
        this.internalName = name;
        this.setCreativeTab(ItemRegistry.CREATIVE_TAB);
    }

    public String getInternalName() {
        return internalName;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public void registerItemRenderer() {}

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int layer = slot == EntityEquipmentSlot.LEGS ? 2 : 1;
        return Reference.MOD_ID +":textures/models/armor/ninjaGear_layer_" + layer +".png";
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if(this.armorType != EntityEquipmentSlot.CHEST) {
            return;
        }
        if(!world.isRemote) {
            if (!player.isPotionActive(PotionRegistry.getInstance().potionNinjaAura)) {
                ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (helmet == null) {
                    return;
                }
                ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                if (leggings == null) {
                    return;
                }
                ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
                if (boots == null) {
                    return;
                }
                if ((helmet.getItem() instanceof ItemNinjaArmor) && (leggings.getItem() instanceof ItemNinjaArmor) && (boots.getItem() instanceof ItemNinjaArmor)) {

                    PotionEffect effect = new PotionEffect(PotionRegistry.getInstance().potionNinjaAura, Integer.MAX_VALUE, 0, false, false);
                    player.addPotionEffect(effect);

                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:ninjaGear_L1"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + internalName, "inventory")));
        return list;
    }

    @Override
    public List<IRecipe> getRecipes() {
        List<IRecipe> list = new ArrayList<>();
        switch(this.armorType) {
            case HEAD:
                list.add(new ShapedOreRecipe(this, "dhd", "sls",
                        'd', "dyeBlack",
                        'h', Items.LEATHER_HELMET,
                        's', "string",
                        'l', "leather"));
                break;
            case CHEST:
                list.add(new ShapedOreRecipe(this, "dsd", "lcl", "lsl",
                        'd', "dyeBlack",
                        'c', Items.LEATHER_CHESTPLATE,
                        's', "string",
                        'l', "leather"));
                break;
            case LEGS:
                list.add(new ShapedOreRecipe(this, "dpd", "lsl", "lsl",
                        'd', "dyeBlack",
                        'p', Items.LEATHER_LEGGINGS,
                        's', "string",
                        'l', "leather"));
                break;
            case FEET:
                list.add(new ShapedOreRecipe(this, "s s", "dbd", "lll",
                        'd', "dyeBlack",
                        'b', Items.LEATHER_BOOTS,
                        's', "string",
                        'l', "leather"));
                break;
        }
        return list;
    }

    public static ArmorMaterial getMaterial() {
        if(ninjaCloth == null) {
            ninjaCloth = EnumHelper.addArmorMaterial("ninjaCloth", "ninja_cloth", 15, new int[]{2, 3, 4, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F);
            ninjaCloth.customCraftingMaterial = Item.getItemFromBlock(Blocks.WOOL);
        }
        return ninjaCloth;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
