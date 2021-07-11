package com.example.examplemod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import org.junit.Test;

import static io.netty.buffer.Unpooled.buffer;
import static org.junit.Assert.*;

public class EssenceSyncPacketTest {
    @Test
    public void decodeEmptyTag(){
        EssenceSyncPacket pkt = new EssenceSyncPacket(new CompoundNBT(), 12);
        PacketBuffer buffer = new PacketBuffer(buffer());
        EssenceSyncPacket.encode(pkt, buffer);

        EssenceSyncPacket fromBuffer = EssenceSyncPacket.decode(buffer);
        assertEquals(pkt, fromBuffer);
    }

    @Test
    public void decodeFilledTag(){
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("testInt", 460);
        nbt.putString("testString", "Test String!");
        ListNBT list = new ListNBT();
        list.add(new CompoundNBT());
        list.add(new CompoundNBT());
        nbt.put("testList", list);
        EssenceSyncPacket pkt = new EssenceSyncPacket(nbt, 12);
        PacketBuffer buffer = new PacketBuffer(buffer());
        EssenceSyncPacket.encode(pkt, buffer);

        EssenceSyncPacket fromBuffer = EssenceSyncPacket.decode(buffer);
        assertEquals(pkt, fromBuffer);
    }
}