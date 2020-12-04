package com.infinityraider.ninjagear.network;

import com.infinityraider.infinitylib.network.MessageBase;
import com.infinityraider.ninjagear.render.player.RenderNinjaGadget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageUpdateGadgetRenderMaskClient extends MessageBase {
    private boolean[] mask;
    private PlayerEntity player;

    public MessageUpdateGadgetRenderMaskClient() {
        super();
    }

    public MessageUpdateGadgetRenderMaskClient(PlayerEntity player, boolean[] mask) {
        this();
        this.mask = mask;
        this.player = player;
    }

    @Override
    public NetworkDirection getMessageDirection() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }

    @Override
    protected void processMessage(NetworkEvent.Context ctx) {
        if(this.player != null) {
            RenderNinjaGadget.getInstance().updateRenderMask(this.player, this.mask);
        }
    }
}
