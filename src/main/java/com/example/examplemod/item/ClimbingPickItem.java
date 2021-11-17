package com.example.examplemod.item;

import com.example.examplemod.capability.climbing.ClimbingHandler;
import com.example.examplemod.network.CClimbingActionPacket;
import com.example.examplemod.network.ModPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class ClimbingPickItem extends PickaxeItem {

    @CapabilityInject(ClimbingHandler.class)
    private static Capability<ClimbingHandler> CLIMBING_HANDLER_CAPABILITY = null;
    private LazyOptional<ClimbingHandler> climbingHandlerLazyOptional;

    final double CLING_DISTANCE = 0.1;
    final int USAGE_COOLDOWN = 5;
    int currentCooldown = 0;

    public ClimbingPickItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        if (worldIn.isRemote() && currentCooldown < 0 && canClimbOnWall(playerIn)) {
            currentCooldown = USAGE_COOLDOWN;
            if (playerIn.hasNoGravity() == true) {
                onRelease(playerIn);
            } else {
                onAttach(playerIn);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    // should ultimately be handled server side, both for security and to ensure correct function when rejoining


    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn.isOnGround()) {
            // reset jumps
        }

        if (entityIn.hasNoGravity() && worldIn.isRemote() && entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityIn;
            if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
                onLeap(player);
            } else if (entityIn.getMotion().getX() != 0.0 && entityIn.getMotion().getZ() != 0.0) {
                onRelease(player);
            }
        }
        currentCooldown--;
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
    }

    private boolean canClimbOnWall(PlayerEntity player) {
        Direction lookDirection = getLookDirection(player.getLookVec());
        Vector3i lookDirectionVec = lookDirection.getDirectionVec();

        // track block in some way
        BlockPos upperLookBlockPos = player.getPosition().add(lookDirection.getDirectionVec());
        BlockPos lowerLookBlockPos = upperLookBlockPos.add(Direction.DOWN.getDirectionVec());

        if (player.world.getBlockState(upperLookBlockPos).isSolid() || player.world.getBlockState(lowerLookBlockPos).isSolid()) {
            // position of player within blockpos
            Vector3d relativePosition = player.getPositionVec().subtract(player.getPosition().getX() + 0.5,0 ,player.getPosition().getZ() + 0.5);
            double distanceFromCentre = relativePosition.dotProduct(new Vector3d(lookDirectionVec.getX(), 0, lookDirectionVec.getZ()));
            if (distanceFromCentre > CLING_DISTANCE) {
                return true;
            }

        }

        return false;
    }

    private Direction getLookDirection(Vector3d lookVec) {
        Vector3d absLookVec = new Vector3d(Math.abs(lookVec.x), Math.abs(lookVec.y), Math.abs(lookVec.z));

        if (absLookVec.x >= absLookVec.z) {
            if (lookVec.x > 0) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }
        } else {
            if (lookVec.z > 0) {
                return Direction.SOUTH;
            } else {
                return Direction.NORTH;
            }
        }
    }

    public void onLeap(PlayerEntity player) {
        // switch gravity on
        player.setNoGravity(false);

        //update capability
        LazyOptional<ClimbingHandler> climbingCapability = player.getCapability(CLIMBING_HANDLER_CAPABILITY);
        climbingCapability.ifPresent(cap -> cap.incJumps());

        if (player.world.isRemote) {
            // if on client, handle movement and inform server of action
            player.jump();
            ModPacketHandler.INSTANCE.sendToServer(new CClimbingActionPacket(CClimbingActionPacket.ClimbingAction.LEAP, player.getPosY()));
        }
    }

    public void onAttach(PlayerEntity player) {
        // switch gravity off and update capability
        player.setNoGravity(true);

        if (player.world.isRemote) {
            // if on client, handle movement and inform server of action
            player.setMotion(Vector3d.ZERO);
            ModPacketHandler.INSTANCE.sendToServer(new CClimbingActionPacket(CClimbingActionPacket.ClimbingAction.ATTACH, player.getPosY()));
        }


    }

    public void onRelease(PlayerEntity player) {
        // switch gravity on
        player.setNoGravity(false);

        if (player.world.isRemote) {
            // if on client, inform server of action
            ModPacketHandler.INSTANCE.sendToServer(new CClimbingActionPacket(CClimbingActionPacket.ClimbingAction.RELEASE, player.getPosY()));
        }
    }
}
