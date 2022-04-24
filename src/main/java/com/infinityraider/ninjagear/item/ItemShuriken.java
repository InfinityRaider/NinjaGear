package com.infinityraider.ninjagear.item;

import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.entity.EntityShuriken;
import com.infinityraider.ninjagear.handler.NinjaAuraHandler;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemShuriken extends ItemBase implements IHiddenItem {
    public ItemShuriken() {
        super("shuriken", new Properties().tab(ItemRegistry.CREATIVE_TAB));
    }

    @Override
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if(!world.isClientSide()) {
            boolean crit = player.hasEffect(EffectRegistry.getInstance().getNinjaHiddenEffect());
            EntityShuriken shuriken = new EntityShuriken(player, crit);
            world.addFreshEntity(shuriken);
            if (!player.isCreative()) {
                player.getInventory().removeItem(player.getInventory().selected, 1);
            }
            NinjaAuraHandler.getInstance().revealEntity(player, NinjaGear.instance.getConfig().getHidingCooldown(), true);
        }
        return new InteractionResultHolder<>(InteractionResult.CONSUME, player.getItemInHand(hand));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(Player entity, ItemStack stack) {
        return false;
    }
}
