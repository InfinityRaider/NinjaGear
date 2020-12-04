package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.reference.Objects;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.item.ItemBase;
import com.google.common.collect.Multimap;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemKatana extends ItemBase {
    private Ingredient repairItem;

    public ItemKatana() {
        super(Objects.KATANA, new Properties()
                .maxDamage(1000)
                .maxStackSize(1)
                .group(ItemRegistry.CREATIVE_TAB)
        );
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.isIn(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANTS
                    && material != Material.TALL_PLANTS
                    && material != Material.CORAL
                    && !state.isIn(BlockTags.LEAVES)
                    && material != Material.GOURD
                    ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
            if (state.getBlockHardness(world, pos) != 0.0F) {
                stack.damageItem(2, entityLiving, (entity) -> {
                    entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
            }
            return true;
        }

    @Override
    public boolean canHarvestBlock(BlockState blockIn) {
        return blockIn.getBlock() == Blocks.COBWEB;
    }

    @Override
    public int getItemEnchantability() {
        return 15;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return ItemRegistry.REPAIR_ITEM_STEEL.test(repair);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if(player.getItemStackFromSlot(EquipmentSlotType.OFFHAND) != null) {
            player.getAttributeManager().reapplyModifiers(getAttributeModifiers(EquipmentSlotType.MAINHAND, stack, false));
        } else {
            player.getAttributeManager().reapplyModifiers(getAttributeModifiers(EquipmentSlotType.MAINHAND, stack, true));
        }
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack, boolean effective) {
        Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlotType.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier",
                            3.0 + (effective ? NinjaGear.instance.getConfig().getKatanaDamage() : 0),
                            AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier",
                            (effective ? 1 : -2),
                            AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

    @Override
    @ParametersAreNonnullByDefault
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return getAttributeModifiers(slot, stack, true);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L2"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L3"));
    }
}
