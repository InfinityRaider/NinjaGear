package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.ninjagear.handler.RenderPlayerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageInvisibility extends MessageBase<IMessage> {
    private EntityPlayer player;
    private boolean invisible;

    @SuppressWarnings("unused")
    public MessageInvisibility() {}

    public MessageInvisibility(EntityPlayer player, boolean invisible) {
        this.player = player;
        this.invisible = invisible;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        if(this.player != null) {
            RenderPlayerHandler.getInstance().setPlayerInvisibilityStatus(this.player, this.invisible);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
