package com.github.retrooper.packetevents.protocol.world.chunk.reader.impl;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.reader.ChunkReader;

import java.util.BitSet;

public class ChunkReader_v1_18 implements ChunkReader {
    @Override
    public BaseChunk[] read(BitSet set, BitSet sevenExtendedMask, boolean fullChunk, boolean hasSkyLight, boolean checkForSky, int chunkSize, byte[] data, NetStreamInput dataIn) {
        BaseChunk[] chunks = new BaseChunk[chunkSize];

        for (int index = 0; index < chunkSize; ++index) {
            chunks[index] = Chunk_v1_18.read(dataIn);
        }

        return chunks;
    }
}
