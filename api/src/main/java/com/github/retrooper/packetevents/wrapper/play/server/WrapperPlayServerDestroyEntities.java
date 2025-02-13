/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class WrapperPlayServerDestroyEntities extends PacketWrapper<WrapperPlayServerDestroyEntities> {
    private int[] entityIDs;

    public WrapperPlayServerDestroyEntities(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDestroyEntities(int... entityIDs) {
        super(PacketType.Play.Server.DESTROY_ENTITIES);
        this.entityIDs = entityIDs;
    }

    public WrapperPlayServerDestroyEntities(int entityID) {
        super(PacketType.Play.Server.DESTROY_ENTITIES);
        this.entityIDs = new int[] {entityID};
    }

    @Override
    public void readData() {
        if (serverVersion == ServerVersion.V_1_17) {
            entityIDs = new int[] {readVarInt()};
        }
        else {
            if (serverVersion == ServerVersion.V_1_7_10) {
                int entityIDCount = readUnsignedByte();
                entityIDs = new int[entityIDCount];
                for (int i = 0; i < entityIDCount; i++) {
                    entityIDs[i] = readInt();
                }
            }
            else {
                int entityIDCount = readVarInt();
                entityIDs = new int[entityIDCount];
                for (int i = 0; i < entityIDCount; i++) {
                    entityIDs[i] = readVarInt();
                }
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerDestroyEntities wrapper) {
        entityIDs = wrapper.entityIDs;
    }

    @Override
    public void writeData() {
        if (serverVersion == ServerVersion.V_1_17) {
            writeVarInt(entityIDs[0]);
        }
        else {
            if (serverVersion == ServerVersion.V_1_7_10) {
                writeByte(entityIDs.length);
                for (int entityID : entityIDs) {
                    writeInt(entityID);
                }
            }
            else {
                writeVarInt(entityIDs.length);
                for (int entityID : entityIDs) {
                    writeVarInt(entityID);
                }
            }
        }
    }

    public int[] getEntityIds() {
        return entityIDs;
    }

    public void setEntityIds(int[] entityIDs) {
        this.entityIDs = entityIDs;
    }
}
