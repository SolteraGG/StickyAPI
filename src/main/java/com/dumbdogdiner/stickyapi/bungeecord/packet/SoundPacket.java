/*
 * Copyright (c) 2021 DumbDogDiner <dumbdogdiner.com>. All rights reserved.
 * Licensed under the MIT license, see LICENSE for more information...
 */
package com.dumbdogdiner.stickyapi.bungeecord.packet;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

public class SoundPacket extends DefinedPacket {

    public int sound;
    public int category;
    private int x;
    private int y;
    private int z;
    private float volume;
    private float pitch;

    public SoundPacket() {
    }

    /**
     * Create a new sound packet to play to a player
     * 
     * @param sound    to play
     * @param category of the sound
     * @param x        coordinate of where this sound should be played
     * @param y        coordinate of where this sound should be played
     * @param z        coordinate of where this sound should be played
     * @param volume   to play the sound at
     * @param pitch    to play the sound at
     * @deprecated it is not recommended to play sounds over BungeeCord as it is
     *             very prone to issues
     */
    @Deprecated
    public SoundPacket(int sound, int category, double x, double y, double z, float volume, float pitch) {
        this.sound = sound;
        this.category = category;
        setX(x);
        setY(y);
        setZ(z);
        this.volume = volume;
        this.pitch = pitch;
    }

    public void setX(double x) {
        this.x = ((int) (x * 8.0D));
    }

    public void setY(double y) {
        this.y = ((int) (y * 8.0D));
    }

    public void setZ(double z) {
        this.z = ((int) (z * 8.0D));
    }

    @Override
    public void handle(AbstractPacketHandler abstractPacketHandler) throws Exception {

    }

    @Override
    public void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        this.sound = readVarInt(buf);
        this.category = readVarInt(buf);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public void write(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
        writeVarInt(sound, buf);
        writeVarInt(category, buf);

        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);

        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
