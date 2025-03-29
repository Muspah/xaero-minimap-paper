package eu.internetpolice.minimap.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class ClientboundRulesPacket extends AbstractPacket {
    @Override
    protected byte getPacketId() {
        return 4;
    }

    @Override
    public byte[] encode() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("cm", true);
        nbt.putBoolean("ncm", true);
        nbt.putBoolean("r", true);

        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeByte(getPacketId());
        buffer.writeNbt(nbt);

        return encodeBuffer(buffer);
    }
}
