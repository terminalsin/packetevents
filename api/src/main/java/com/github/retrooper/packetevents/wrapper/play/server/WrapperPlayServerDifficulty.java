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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDifficulty extends PacketWrapper<WrapperPlayServerDifficulty> {
    private Difficulty difficulty;
    private boolean locked;

    public WrapperPlayServerDifficulty(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDifficulty(Difficulty difficulty, boolean locked) {
        super(PacketType.Play.Server.SERVER_DIFFICULTY);
        this.difficulty = difficulty;
        this.locked = locked;

    }

    @Override
    public void readData() {
        difficulty = Difficulty.getById(readByte());
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            locked = readBoolean();
        }
        //TODO On 1.8 locked theoretically is true? Confirm
    }

    @Override
    public void readData(WrapperPlayServerDifficulty wrapper) {
        difficulty = wrapper.difficulty;
        locked = wrapper.locked;
    }

    @Override
    public void writeData() {
        writeByte(difficulty.getId());
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            writeBoolean(locked);
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
