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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.util.Vector3d;

public class Location {
    private Vector3d position;
    private float yaw;
    private float pitch;

    public Location(Vector3d position, float yaw, float pitch) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(double x, double y, double z, float yaw, float pitch) {
        this(new Vector3d(x, y, z), yaw, pitch);
    }

    public Vector3d getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public void setX(double x) {
        position.setX(x);
    }

    public double getY() {
        return position.getY();
    }

    public void setY(double y) {
        position.setY(y);
    }

    public double getZ() {
        return position.getZ();
    }

    public void setZ(double z) {
        position.setZ(z);
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

    @Override
    public Location clone() {
        return new Location(position.clone(), yaw, pitch);
    }

    @Override
    public String toString() {
        return "Location {[" + position.toString() + "]," + " yaw: " + yaw + ", pitch: " + pitch + "}";
    }
}
