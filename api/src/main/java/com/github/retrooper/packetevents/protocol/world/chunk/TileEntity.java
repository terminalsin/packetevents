package com.github.retrooper.packetevents.protocol.world.chunk;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;

public class TileEntity {
    // 1.18+
    byte packedByte;
    // 1.18+
    short y;
    // 1.18+
    int type;
    // Exists on all versions
    NBTCompound data;

    // 1.18 format: ((blockX & 15) << 4) | (blockZ & 15)
    // Versions below this store height in the NBTCompound
    public TileEntity(final NBTCompound data) {
        this.data = data;
    }

    public TileEntity(final byte packedByte, final short y, final int type, final NBTCompound data) {
        this.packedByte = packedByte;
        this.y = y;
        this.type = type;
        this.data = data;
    }

    public int getX() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
            return (this.packedByte & 0xF0) >> 4;
        }
        return data.getTagOfTypeOrNull("x", NBTInt.class).getAsInt();
    }

    public int getZ() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
            return this.packedByte & 0xF;
        }
        return data.getTagOfTypeOrNull("z", NBTInt.class).getAsInt();
    }

    public int getY() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
            return this.y;
        }
        return data.getTagOfTypeOrNull("y", NBTInt.class).getAsInt();
    }

    public void setX(final int x) {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.packedByte = (byte) ((this.packedByte & 0xF) | ((x & 0xF) << 4));
        } else {
            data.setTag("x", new NBTInt(x));
        }
    }

    public void setY(final int y) {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.y = (short) y;
        } else {
            data.setTag("y", new NBTInt(y));
        }
    }

    public void setZ(final int z) {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
            this.packedByte = (byte) ((this.packedByte & 0xF0) | (z & 0xF));
        } else {
            data.setTag("z", new NBTInt(z));
        }
    }

    // How do we get the type? Does anyone need this?
    public int getType() {
        return this.type;
    }

    public byte getPackedByte() {
        return this.packedByte;
    }

    public short getYShort() {
        return this.y;
    }

    public NBTCompound getNBT() {
        return this.data;
    }
}
