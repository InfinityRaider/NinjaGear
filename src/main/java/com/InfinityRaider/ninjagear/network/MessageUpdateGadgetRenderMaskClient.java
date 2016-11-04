package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.ninjagear.render.player.RenderNinjaGadget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdateGadgetRenderMaskClient extends MessageBase<IMessage> {
    private boolean[] mask;
    private EntityPlayer player;

    @SuppressWarnings("unused")
    public MessageUpdateGadgetRenderMaskClient() {}

    public MessageUpdateGadgetRenderMaskClient(EntityPlayer player, boolean[] mask) {
        this.mask = mask;
        this.player = player;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.player != null) {
            RenderNinjaGadget.getInstance().updateRenderMask(this.player, this.mask);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
