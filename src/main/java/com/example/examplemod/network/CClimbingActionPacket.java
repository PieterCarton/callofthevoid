package com.example.examplemod.network;

import com.example.examplemod.item.ClimbingPickItem;
import com.example.examplemod.setup.ModItems;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CClimbingActionPacket extends ClientToServerPacket {
    enum ClimbingAction{
        ATTACH,
        RELEASE,
        LEAP
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
        switch (action){
            case LEAP:
                climbingPick.onLeap();
                break;
            case ATTACH:
                climbingPick.onAttach();
                break;
            case RELEASE:
                climbingPick.onRelease();
        }
    }

    @Override
    public void serialize(PacketBuffer buffer) {
        buffer.writeEnumValue(action);
        buffer.writeDouble(playerHeight);
    }
}
