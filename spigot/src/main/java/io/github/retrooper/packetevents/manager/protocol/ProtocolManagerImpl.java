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

package io.github.retrooper.packetevents.manager.protocol;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.CustomPipelineUtil;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ProtocolManagerImpl implements ProtocolManager {
    private static Method WRITE_MESSAGE_METHOD;
    private ProtocolVersion platformVersion;

    //TODO Implement
    private ProtocolVersion resolveVersionNoCache() {
       /* for (final ServerVersion val : ServerVersion.reversedValues()) {
            //For example "V_1_18" -> "1.18"
            if (Bukkit.getBukkitVersion().contains(val.getReleaseName())) {
                return val;
            }
        }

        ServerVersion fallbackVersion = PacketEvents.getAPI().getSettings().getFallbackServerVersion();
        if (fallbackVersion != null) {
            if (fallbackVersion == ServerVersion.V_1_7_10) {
                try {
                    Class.forName("net.minecraft.util.io.netty.buffer.ByteBuf");
                } catch (Exception ex) {
                    //We will assume its 1.8.8
                    fallbackVersion = ServerVersion.V_1_8_8;
                }
            }
            Plugin plugin = (Plugin) PacketEvents.getAPI().getPlugin();
            plugin.getLogger().warning("[packetevents] Your server software is preventing us from checking the server version. This is what we found: " + Bukkit.getBukkitVersion() + ". We will assume the server version is " + fallbackVersion.name() + "...");
            return fallbackVersion;
        }
        return ServerVersion.ERROR;*/
        return ProtocolVersion.UNKNOWN;
    }

    @Override
    public ProtocolVersion getPlatformVersion() {
        if (platformVersion == null) {
            platformVersion = resolveVersionNoCache();
        }
        return platformVersion;
    }

    @Override
    public void sendPacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            ChannelHelper.writeAndFlush(channel, byteBuf);
        }
    }

    @Override
    public void sendPacketSilently(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //Only call the encoders after ours in the pipeline.
            //TODO Downside is Via won't process the packet either(yet)
            ChannelHelper.writeAndFlushInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
        }
    }

    @Override
    public void writePacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //TODO Why can we not listen to our own written packets when protocollib is present
            Object encoder = ChannelHelper.getPipelineHandler(channel, PacketEvents.ENCODER_NAME);
            Object ctx = ChannelHelper.getPipelineContext(channel, PacketEvents.ENCODER_NAME);
            if (WRITE_MESSAGE_METHOD == null) {
                try {
                    WRITE_MESSAGE_METHOD = encoder.getClass().getMethod("writeMessage", Object.class, Object.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            try {
                //Call writeMessage in our encoder, which redirects to write in netty encoders.
                //This should be passed on to the next encoders.
                WRITE_MESSAGE_METHOD.invoke(encoder, ctx, byteBuf);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void writePacketSilently(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //Only call the encoders after ours in the pipeline
            ChannelHelper.writeInContext(channel, PacketEvents.ENCODER_NAME, byteBuf);
        }
    }

    @Override
    public void receivePacket(Object channel, Object byteBuf) {
        if (ChannelHelper.isOpen(channel)) {
            //TODO Have we given ViaVersion a thought?
            List<String> handlerNames = ChannelHelper.pipelineHandlerNames(channel);
            if (handlerNames.contains("ps_decoder_transformer")) {
                //We want to skip ProtocolSupport's translation handlers,
                //because the buffer is fit for the current server-version
                ChannelHelper.fireChannelReadInContext(channel, "ps_decoder_transformer", byteBuf);
            } else if (handlerNames.contains("decompress")) {
                //We will have to just skip through the minecraft server's decompression handler
                ChannelHelper.fireChannelReadInContext(channel, "decompress", byteBuf);
            } else {
                if (handlerNames.contains("decrypt")) {
                    //We will have to just skip through the minecraft server's decryption handler
                    //We don't have to deal with decompressing, as that handler isn't currently in the pipeline
                    ChannelHelper.fireChannelReadInContext(channel, "decrypt", byteBuf);
                } else {
                    //No decompressing nor decrypting handlers are present
                    //You cannot fill this buffer up with chunks of packets,
                    //since we skip the packet-splitter handler.
                    ChannelHelper.fireChannelReadInContext(channel, "splitter", byteBuf);
                }
            }
        }
    }

    @Override
    public void receivePacketSilently(Object channel, Object byteBuf) {
        //Receive the packet for all handlers after our decoder
        ChannelHelper.fireChannelReadInContext(channel, PacketEvents.DECODER_NAME, byteBuf);
    }

    @Override
    public ClientVersion getClientVersion(Object channel) {
        User user = getUser(channel);
        if (user.getClientVersion() == null) {
            return ClientVersion.UNKNOWN;
        }
        return user.getClientVersion();
    }
}
