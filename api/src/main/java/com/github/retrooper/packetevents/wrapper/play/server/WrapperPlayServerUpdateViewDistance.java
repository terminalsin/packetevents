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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateViewDistance extends PacketWrapper<WrapperPlayServerUpdateViewDistance> {
    private int viewDistance;
    public WrapperPlayServerUpdateViewDistance(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateViewDistance(int viewDistance) {
        super(PacketType.Play.Server.UPDATE_VIEW_DISTANCE);
        this.viewDistance = viewDistance;
    }

    @Override
    public void readData() {
        viewDistance = readVarInt();
    }

    @Override
    public void readData(WrapperPlayServerUpdateViewDistance wrapper) {
        viewDistance = wrapper.viewDistance;
    }

    @Override
    public void writeData() {
        writeVarInt(viewDistance);
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }
}
