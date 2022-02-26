/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
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

package io.github.retrooper.packetevents.handlers.modern;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

@ChannelHandler.Sharable
public class PacketEncoderModern extends MessageToByteEncoder<ByteBuf> {
    public User user;
    public volatile Player player;

    public PacketEncoderModern(User user) {
        this.user = user;
    }

    public void writeMessage(Object ctx, Object msg) throws Exception {
        write((ChannelHandlerContext) ctx, msg, ((ChannelHandlerContext) ctx).newPromise());
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        int preProcessIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player, buffer);
        int processIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> {
            buffer.readerIndex(processIndex);
        });
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().writeData();
            }
            buffer.readerIndex(preProcessIndex);
        } else {
            //Make the buffer unreadable for the next handlers
            buffer.clear();
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        if (in.readableBytes() == 0) return;
        out.writeBytes(in);
        read(ctx, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //Check if the minecraft server will already print our exception for us.
        if (ExceptionUtil.isException(cause, PacketProcessException.class)
                && !SpigotReflectionUtil.isMinecraftServerInstanceDebugging()
                && (user == null || user.getConnectionState() != ConnectionState.HANDSHAKING)) {
            cause.printStackTrace();
        }
    }
}
