package com.example.examplemod.network;

import com.example.examplemod.item.ClimbingPickItem;
import com.example.examplemod.setup.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CClimbingActionPacket extends ClientToServerPacket {
    public enum ClimbingAction{
        ATTACH,
        RELEASE,
        LEAP,
        SLIDE
    }

    private ClimbingAction action;
    private double playerHeight;

    public CClimbingActionPacket(ClimbingAction action, double playerHeight) {
        this.action = action;
        this.playerHeight = playerHeight;
    }

    public CClimbingActionPacket(PacketBuffer buffer) {
        this.action = buffer.readEnumValue(ClimbingAction.class);
        this.playerHeight = buffer.readDouble();
    }

    @Override
    public void handleServerside(NetworkEvent.Context ctx) {
        ClimbingPickItem climbingPick = (ClimbingPickItem) ModItems.CLIMBING_PICK_ITEM.get();
        PlayerEntity player = ctx.getSender();
        switch (action){
            case LEAP:
                climbingPick.onLeap(player);
                break;
            case ATTACH:
                climbingPick.onAttach(player);
                break;
            case RELEASE:
                climbingPick.onRelease(player);
                break;
            case SLIDE:
                climbingPick.onStartSliding(player);
        }
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeEnumValue(action);
        buffer.writeDouble(playerHeight);
    }
}
