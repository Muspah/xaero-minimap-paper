package eu.internetpolice.minimap.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public class ClientboundPlayerTrackerResetPacket extends AbstractPacket {
    @Override
    protected byte getPacketId() {
        return 3;
    }

    @Override
    public byte[] encode() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeByte(getPacketId());
        return encodeBuffer(buffer);
    }
}
