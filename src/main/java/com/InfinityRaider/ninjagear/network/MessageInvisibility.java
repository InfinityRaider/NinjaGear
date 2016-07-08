package com.infinityraider.ninjagear.network;

import com.infinityraider.ninjagear.handler.RenderPlayerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageInvisibility extends MessageBase<IMessage> {
    EntityPlayer player;
    boolean invisible;

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
        if(ctx.side == Side.CLIENT && this.player != null) {
            RenderPlayerHandler.getInstance().setPlayerInvisibilityStatus(this.player, this.invisible);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = this.readPlayerFromByteBuf(buf);
        this.invisible = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.writePlayerToByteBuf(buf, this.player);
        buf.writeBoolean(this.invisible);
    }
}
