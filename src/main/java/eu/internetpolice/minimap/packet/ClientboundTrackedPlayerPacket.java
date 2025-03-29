package eu.internetpolice.minimap.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ClientboundTrackedPlayerPacket extends AbstractPacket {
    protected final boolean remove;
    protected final Player player;
    protected final Location location;

    public ClientboundTrackedPlayerPacket(boolean remove, Player player, Location location) {
        this.remove = remove;
        this.player = player;
        this.location = location;
    }

    public boolean isRemove() {
        return remove;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    protected byte getPacketId() {
        return 2;
    }

    @Override
    public byte[] encode() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("r", this.remove);
        nbt.putUUID("i", this.player.getUniqueId());
        if (!this.remove) {
            nbt.putDouble("x", this.location.getX());
            nbt.putDouble("y", this.location.getY());
            nbt.putDouble("z", this.location.getZ());

            switch (this.location.getWorld().getEnvironment()) {
                case World.Environment.NORMAL -> nbt.putString("d", "minecraft:overworld");
                case World.Environment.NETHER -> nbt.putString("d", "minecraft:nether");
                case World.Environment.THE_END -> nbt.putString("d", "minecraft:the_end");
                default -> nbt.putString("d", "minecraft:custom");
            }
        }

        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeByte(getPacketId());
        buffer.writeNbt(nbt);

        return encodeBuffer(buffer);
    }
}
