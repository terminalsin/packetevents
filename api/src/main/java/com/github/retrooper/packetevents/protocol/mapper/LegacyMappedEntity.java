package com.github.retrooper.packetevents.protocol.mapper;

import com.github.retrooper.packetevents.resources.ResourceLocation;

public interface LegacyMappedEntity {

    ResourceLocation getName();

    int getLegacyId();

}
