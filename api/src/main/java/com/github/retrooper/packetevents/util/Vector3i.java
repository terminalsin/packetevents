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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;

/**
 * 3D int Vector.
 * This vector can represent coordinates, angles, or anything you want.
 * You can use this to represent an array if you really want.
 * PacketEvents usually uses this for block positions as they don't need any decimals.
 *
 * @author retrooper
 * @since 1.7
 */
public class Vector3i {
    /**
     * X (coordinate/angle/whatever you wish)
     */
    public int x;
    /**
     * Y (coordinate/angle/whatever you wish)
     */
    public int y;
    /**
     * Z (coordinate/angle/whatever you wish)
     */
    public int z;

    /**
     * Default constructor setting all coordinates/angles/values to their default values (=0).
     */
    public Vector3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3i(long val) {
        this(val, PacketEvents.getAPI().getServerManager().getVersion());
    }

    public Vector3i(long val, ServerVersion serverVersion) {
        int x = (int) (val >> 38);
        int y;
        int z;

        // 1.14 method for this is storing X Z Y
        // 1.17 added support for negative values
        // 1.15+ might all be the same but let's be safe with not producing negative values on 1.14-1.16...
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            y = (int) (val << 52 >> 52);
            z = (int) (val << 26 >> 38);
        } else if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            y = (int) (val << 52 >> 52);
            z = (int) (val << 26 >> 38);
        } else {
            // 1.13 and below store X Y Z
            y = (int) ((val >> 26) & 0xFFF);
            z = (int) (val << 38 >> 38);
        }

        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * Constructor allowing you to set the values.
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor allowing you to specify an array.
     * X will be set to the first index of an array(if it exists, otherwise 0).
     * Y will be set to the second index of an array(if it exists, otherwise 0).
     * Z will be set to the third index of an array(if it exists, otherwise 0).
     *
     * @param array Array.
     */
    public Vector3i(int[] array) {
        if (array.length > 0) {
            x = array[0];
        } else {
            x = 0;
            y = 0;
            z = 0;
            return;
        }
        if (array.length > 1) {
            y = array[1];
        } else {
            y = 0;
            z = 0;
            return;
        }
        if (array.length > 2) {
            z = array[2];
        } else {
            z = 0;
        }
    }

    public long getSerializedPosition(ServerVersion serverVersion) {
        // 1.17 adds support for negative values
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            long x = getX() & 0x3FFFFFF;
            long y = getY() & 0xFFF;
            long z = getZ() & 0x3FFFFFF;

            return x << 38 | z << 12 | y;
        }
        // 1.14 method for this is storing X Z Y
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            return ((long) (getX() & 0x3FFFFFF) << 38) | ((long) (getZ() & 0x3FFFFFF) << 12) | (getY() & 0xFFF);
        }
        // 1.13 and below store X Y Z
        return ((long) (getX() & 0x3FFFFFF) << 38) | ((long) (getY() & 0xFFF) << 26) | (getZ() & 0x3FFFFFF);
    }

    public long getSerializedPosition() {
        return getSerializedPosition(PacketEvents.getAPI().getServerManager().getVersion());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    /**
     * Is the object we are comparing to equal to us?
     * It must be of type Vector3d or Vector3i and all values must be equal to the values in this class.
     *
     * @param obj Compared object.
     * @return Are they equal?
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3i) {
            Vector3i vec = (Vector3i) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof Vector3d) {
            Vector3d vec = (Vector3d) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof Vector3f) {
            Vector3f vec = (Vector3f) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        }
        return false;
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    /**
     * Simply clone an instance of this class.
     *
     * @return Clone.
     */
    @Override
    public Vector3i clone() {
        return new Vector3i(getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }

    public static Vector3i zero() {
        return new Vector3i();
    }
}
