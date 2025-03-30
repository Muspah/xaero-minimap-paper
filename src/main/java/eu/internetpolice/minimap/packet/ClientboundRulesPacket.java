package eu.internetpolice.minimap.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class ClientboundRulesPacket extends AbstractPacket {
    protected final boolean allowCaveMode;
    protected final boolean allowNetherCaveMode;
    protected final boolean allowRadar;

    public ClientboundRulesPacket(boolean allowCaveMode, boolean allowNetherCaveMode, boolean allowRadar) {
        this.allowCaveMode = allowCaveMode;
        this.allowNetherCaveMode = allowNetherCaveMode;
        this.allowRadar = allowRadar;
    }

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

        FriendlyByteBuf buffer = getPacket();
        buffer.writeNbt(nbt);

        return encodeBuffer(buffer);
    }
}
