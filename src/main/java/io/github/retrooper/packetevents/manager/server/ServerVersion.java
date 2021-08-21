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

package io.github.retrooper.packetevents.manager.server;

import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;

/**
 * Server Version.
 * This is a nice wrapper over minecraft's protocol versions.
 * You won't have to memorize the protocol version, just memorize the server version you see in the launcher.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol_version_numbers">https://wiki.vg/Protocol_version_numbers</a>
 * @since 1.6.9
 */
public enum ServerVersion {
    v_1_7_10((short) 5),
    v_1_8((short) 47), v_1_8_3((short) 47), v_1_8_8((short) 47),
    v_1_9((short) 107), v_1_9_2((short) 109), v_1_9_4((short) 110),
    //1.10 and 1.10.1 are redundant
    v_1_10((short) 210), v_1_10_1((short) 210), v_1_10_2((short) 210),
    v_1_11((short) 315), v_1_11_2((short) 316),
    v_1_12((short) 335), v_1_12_1((short) 338), v_1_12_2((short) 340),
    v_1_13((short) 393), v_1_13_1((short) 401), v_1_13_2((short) 404),
    v_1_14((short) 477), v_1_14_1((short) 480), v_1_14_2((short) 485), v_1_14_3((short) 490), v_1_14_4((short) 498),
    v_1_15((short) 573), v_1_15_1((short) 575), v_1_15_2((short) 578),
    v_1_16((short) 735), v_1_16_1((short) 736), v_1_16_2((short) 751), v_1_16_3((short) 753), v_1_16_4((short) 754), v_1_16_5((short) 754),
    v_1_17((short) 755), v_1_17_1((short) 756),
    ERROR((short) -1);

    private static final ServerVersion[] VALUES = values();
    public static ServerVersion[] reversedValues = new ServerVersion[VALUES.length];
    private final short protocolVersion;

    ServerVersion(short protocolId) {
        this.protocolVersion = protocolId;
    }

    public static ServerVersion resolve() {
        if (reversedValues[0] == null) {
            reversedValues = ServerVersion.reverse();
        }

        for (final ServerVersion val : reversedValues) {
            String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            }
        }

        ServerVersion fallbackVersion = PacketEvents.get().getSettings().getFallbackServerVersion();
        if (fallbackVersion != null) {
            if (fallbackVersion == ServerVersion.v_1_7_10) {
                try {
                    Class.forName("net.minecraft.util.io.netty.buffer.ByteBuf");
                }
                catch (Exception ex) {
                    //We will assume its 1.8.8
                    fallbackVersion = ServerVersion.v_1_8_8;
                }
            }
            Bukkit.getLogger().warning("[packetevents] Your server software is preventing us from checking the server version. We will assume the server version is " + fallbackVersion.name() + "...");
            return fallbackVersion;
        }
        return ERROR;
    }
    /**
     * The values in this enum in reverse.
     *
     * @return Reversed server version enum values.
     */
    private static ServerVersion[] reverse() {
        ServerVersion[] array = values();
        int i = 0;
        int j = array.length - 1;
        ServerVersion tmp;
        while (j > i) {
            tmp = array[j];
            array[j--] = array[i];
            array[i++] = tmp;
        }
        return array;
    }

    public static ServerVersion getLatest() {
        return reversedValues[0];
    }

    public static ServerVersion getOldest() {
        return values()[0];
    }

    /**
     * Get this server version's protocol version.
     *
     * @return Protocol version.
     */
    public short getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Is this server version newer than the compared server version?
     * This method simply checks if this server version's protocol version is greater than
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version newer than the compared server version.
     */
    public boolean isNewerThan(ServerVersion target) {
        /*
         * Some server versions have the same protocol version in the minecraft protocol.
         * We still need this method to work in such cases.
         * We first check if this is the case, if the protocol versions aren't the same, we can just use the protocol versions
         * to compare the server versions.
         */
        if (target.protocolVersion != protocolVersion || this == target) {
            return protocolVersion > target.protocolVersion;
        }

        /*
         * The server versions unfortunately have the same protocol version.
         * We need to look at this "reversedValues" variable.
         * The reversed values variable is an array containing all enum constants in this enum but in a reversed order.
         * I already made this variable a while ago for a different usage, you can check that out.
         * The first one we find in the array is the newer version.
         */
        for (ServerVersion version : reversedValues) {
            if (version == target) {
                return false;
            }
            if (version == this) return true;
        }

        return false;
    }

    /**
     * Is this server version older than the compared server version?
     * This method simply checks if this server version's protocol version is less than
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version older than the compared server version.
     */
    public boolean isOlderThan(ServerVersion target) {
        /*
         * Some server versions have the same protocol version in the minecraft protocol.
         * We still need this method to work in such cases.
         * We first check if this is the case, if the protocol versions aren't the same, we can just use the protocol versions
         * to compare the server versions.
         */
        if (target.protocolVersion != protocolVersion || this == target) {
            return protocolVersion < target.protocolVersion;
        }
        /*
         * The server versions unfortunately have the same protocol version.
         * We look at all enum constants in the ServerVersion enum in the order they have been defined in.
         * The first one we find in the array is the newer version.
         */
        for (ServerVersion version : VALUES) {
            if (version == this) {
                return true;
            } else if (version == target) {
                return false;
            }
        }
        return false;
    }

    /**
     * Is this server version newer than or equal to the compared server version?
     * This method simply checks if this server version's protocol version is greater than or equal to
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version newer than or equal to the compared server version.
     */
    public boolean isNewerThanOrEquals(ServerVersion target) {
        return this == target || isNewerThan(target);
    }

    /**
     * Is this server version older than or equal to the compared server version?
     * This method simply checks if this server version's protocol version is older than or equal to
     * the compared server version's protocol version.
     *
     * @param target Compared server version.
     * @return Is this server version older than or equal to the compared server version.
     */
    public boolean isOlderThanOrEquals(ServerVersion target) {
        return this == target || isOlderThan(target);
    }
}