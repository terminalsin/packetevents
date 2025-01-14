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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This message is sent from the client to the server when the "Done" button is pushed after placing a sign.
 */
public class WrapperPlayClientUpdateSign extends PacketWrapper<WrapperPlayClientUpdateSign> {
    private String[] textLines;
    private Vector3i blockPosition;

    public WrapperPlayClientUpdateSign(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUpdateSign(Vector3i blockPosition, String[] textLines) {
        super(PacketType.Play.Client.UPDATE_SIGN);
        this.blockPosition = blockPosition;
        this.textLines = textLines;
    }

    @Override
    public void readData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.blockPosition = new Vector3i(readLong());
        } else {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            this.blockPosition = new Vector3i(x, y, z);
        }
        textLines = new String[4];
        for (int i = 0; i < 4; i++) {
            this.textLines[i] = readString(384);
        }
    }

    @Override
    public void readData(WrapperPlayClientUpdateSign wrapper) {
        this.blockPosition = wrapper.blockPosition;
        this.textLines = wrapper.textLines;
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            long positionVector = blockPosition.getSerializedPosition();
            writeLong(positionVector);
        } else {
            writeInt(blockPosition.x);
            writeShort(blockPosition.y);
            writeInt(blockPosition.z);
        }
        for (int i = 0; i < 4; i++) {
            writeString(textLines[i], 384);
        }
    }

    /**
     * Block location of the sign.
     *
     * @return Sign position
     */
    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    /**
     * Modify the block location of the sign.
     *
     * @param blockPosition Sign position
     */
    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    /**
     * The text lines in the sign.
     *
     * @return Sign content
     */
    public String[] getTextLines() {
        return textLines;
    }

    /**
     * Modify the text lines in the sign.
     *
     * @param textLines Sign content
     */
    public void setTextLines(String[] textLines) {
        this.textLines = textLines;
    }
}
