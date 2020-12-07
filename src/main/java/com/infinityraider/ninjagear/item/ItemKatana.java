package com.infinityraider.ninjagear.item;

import com.google.common.collect.ImmutableMultimap;
import com.infinityraider.infinitylib.crafting.FallbackIngredient;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.reference.Names;
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
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
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
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final Multimap<Attribute, AttributeModifier> attributeModifiersIneffective;

    public ItemKatana() {
        super(Names.Items.KATANA, new Properties()
                .maxDamage(1000)
                .group(ItemRegistry.CREATIVE_TAB)
        );
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.attributeModifiers = builder
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Katana Attack Damage Modifier",
                        3.0 + NinjaGear.instance.getConfig().getKatanaDamage(), AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Katana Attack Speed Modifier",
                        1, AttributeModifier.Operation.ADDITION)).build();
        builder = ImmutableMultimap.builder();
        this.attributeModifiersIneffective = builder
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Katana Attack Damage modifier",
                        3.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Katana Attack Speed Modifier",
                        -2, AttributeModifier.Operation.ADDITION))
                .build();
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
        return getRepairItem().test(repair);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        ItemStack offhand = player.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
        if(offhand != null && !offhand.isEmpty()) {
            player.getAttributeManager().reapplyModifiers(this.attributeModifiers);
        } else {
            player.getAttributeManager().reapplyModifiers(this.attributeModifiersIneffective);
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        if(slot == EquipmentSlotType.MAINHAND) {
            return this.attributeModifiers;
        } else if(slot == EquipmentSlotType.OFFHAND) {
            return this.attributeModifiersIneffective;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L1"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L2"));
        tooltip.add(new TranslationTextComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L3"));
    }

    private static Ingredient repairItem;

    public static Ingredient getRepairItem() {
        if(repairItem == null) {
            repairItem = new FallbackIngredient(
                    ItemTags.getCollection().getTagByID(new ResourceLocation("forge", "ingots/steel")),
                    Ingredient.fromTag(ItemTags.getCollection().getTagByID(new ResourceLocation("forge", "ingots/iron"))));
        }
        return repairItem;
    }
}
