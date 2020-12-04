package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.handler.NinjaAuraHandler;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import com.google.common.collect.Multimap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemSai extends ItemBase implements IHiddenItem, IItemWithModel {

    public ItemSai() {
        super("sai", new Properties()
                .maxDamage(1000)
                .maxStackSize(1)
                .group(ItemRegistry.CREATIVE_TAB));
    }
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
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
        if(attacker instanceof PlayerEntity) {
            NinjaAuraHandler.getInstance().revealEntity((PlayerEntity) attacker, NinjaGear.instance.getConfig().getHidingCooldown());
        }
        stack.damageItem(1, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        ItemStack offhand = attacker.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
        if(offhand != null && offhand.getItem() instanceof ItemSai) {
            offhand.damageItem(1, attacker, (entity) -> {
                entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entity) {
        if ((double)state.getBlockHardness(world, pos) != 0.0D) {
            stack.damageItem(1, entity, (e) -> {
                e.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
            ItemStack offhand = entity.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
            if(offhand != null && offhand.getItem() instanceof ItemSai) {
                offhand.damageItem(1, entity, (e) -> {
                    e.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
            }
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
        ItemStack offHand = player.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
        boolean crit = player.isPotionActive(EffectRegistry.getInstance().potionNinjaHidden);
        if(offHand != null && offHand.getItem() == this) {
            player.getAttributeManager().reapplyModifiers(getAttributeModifiers(EquipmentSlotType.MAINHAND, stack, true, crit));
        } else {
            player.getAttributeManager().reapplyModifiers(getAttributeModifiers(EquipmentSlotType.MAINHAND, stack, false, false));
        }
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack, boolean effective, boolean crit) {
        Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlotType.MAINHAND) {
            float multiplier = crit ? NinjaGear.instance.getConfig().getCitMultiplier() : 1;
            float damage = (effective ? NinjaGear.instance.getConfig().getSaiDamage() : 0)*multiplier;
            multimap.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier",
                            3.0 +  damage,
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
        return  getAttributeModifiers(slot, stack, true, false);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L4"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(PlayerEntity entity, ItemStack stack) {
        return false;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
        return list;
    }
}
