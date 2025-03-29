package eu.internetpolice.minimap.packet;

import net.minecraft.network.FriendlyByteBuf;

public abstract class AbstractPacket {
    protected abstract byte getPacketId();
    public abstract byte[] encode();

    protected byte[] encodeBuffer(FriendlyByteBuf buffer) {
        return encodeBuffer(buffer, false);
    }

    protected byte[] encodeBuffer(FriendlyByteBuf buffer, boolean strip) {
        if (strip) {
            buffer = buffer.readerIndex(3);
        }

        byte[] packetData = new byte[buffer.readableBytes()];
        buffer.readBytes(packetData);

        return packetData;
    }
}
