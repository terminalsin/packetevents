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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientVehicleMove extends PacketWrapper<WrapperPlayClientVehicleMove> {
    private Vector3d position;
    private float yaw;
    private float pitch;

    public WrapperPlayClientVehicleMove(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientVehicleMove(Vector3d position, float yaw, float pitch) {
        super(PacketType.Play.Client.VEHICLE_MOVE);
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void readData() {
        double x = readDouble();
        double y = readDouble();
        double z = readDouble();
        this.position = new Vector3d(x, y, z);
        this.yaw = readFloat();
        this.pitch = readFloat();
    }

    @Override
    public void readData(WrapperPlayClientVehicleMove wrapper) {
        this.position = wrapper.position;
        this.yaw = wrapper.yaw;
        this.pitch = wrapper.pitch;
    }

    @Override
    public void writeData() {
        writeDouble(position.x);
        writeDouble(position.y);
        writeDouble(position.z);
        writeFloat(yaw);
        writeFloat(pitch);
    }

    public Vector3d getPosition() {
        return position;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
