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

package com.github.retrooper.packetevents.event;

import org.jetbrains.annotations.NotNull;

/**
 * The {@code PlayerEjectEvent} event is fired whenever a player is ejected.
 * This class implements {@link CancellableEvent} and {@link PlayerEvent}.
 *
 * @author retrooper
 * @since 1.6.9
 */
public final class PlayerEjectEvent extends PacketEvent implements CancellableEvent, PlayerEvent<Object> {
    private final Object player;
    private boolean cancelled;

    public PlayerEjectEvent(Object player) {
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        cancelled = value;
    }

    /**
     * This method returns the bukkit player object of the player being ejected.
     * The player object is guaranteed to NOT be null.
     *
     * @return Ejected player.
     */
    @NotNull
    @Override
    public Object getPlayer() {
        return player;
    }

    @Override
    public void call(PacketListenerCommon listener) {
        listener.onPlayerEject(this);
    }

    @Override
    public boolean isInbuilt() {
        return true;
    }
}
