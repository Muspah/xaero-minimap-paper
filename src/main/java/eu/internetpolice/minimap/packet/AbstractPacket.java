package eu.internetpolice.minimap.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractPacket {
    protected abstract byte getPacketId();
    public abstract byte[] encode();

    protected FriendlyByteBuf getPacket() {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeByte(getPacketId());
        return buffer;
    }

    protected byte[] encodeBuffer(FriendlyByteBuf buffer) {
        byte[] packetData = new byte[buffer.readableBytes()];
        buffer.readBytes(packetData);

        return packetData;
    }
}
