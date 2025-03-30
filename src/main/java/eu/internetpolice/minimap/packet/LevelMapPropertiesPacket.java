package eu.internetpolice.minimap.packet;

import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class LevelMapPropertiesPacket extends AbstractPacket {
    protected final int id;

    public LevelMapPropertiesPacket(World world) {
        id = readMapProperties(world.getWorldFolder());
    }

    @Override
    protected byte getPacketId() {
        return 0;
    }

    @Override
    public byte[] encode() {
        FriendlyByteBuf buffer = getPacket();
        buffer.writeInt(id);
        return encodeBuffer(buffer);
    }

    protected int readMapProperties(File worldDirectory) {
        int rand = new Random().nextInt(Integer.MAX_VALUE);
        try {
            Path propertiesFile = worldDirectory.toPath().resolve("xaeromap.txt");
            if (Files.exists(propertiesFile)) {
                for (String line : Files.readAllLines(propertiesFile)) {
                    if (line.startsWith("id:")) {
                        String id = line.split(":")[1];
                        try {
                            return Integer.parseInt(id);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            Files.writeString(propertiesFile, "id:" + rand);
        } catch (IOException ignored) {}

        return rand;
    }
}
