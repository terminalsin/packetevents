/*
 * This file is part of ProtocolLib - https://github.com/dmulloy2/ProtocolLib
 * Copyright (C) ProtocolLib
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.handlers.modern.early;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.handlers.modern.ServerConnectionInitializerModern;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class PreChannelInitializerModern_v1_8 extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                ServerConnectionInitializerModern.initChannel(channel, ConnectionState.HANDSHAKING);
            }
        });
    }
}
