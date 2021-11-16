package com.example.examplemod.item;

import com.example.examplemod.capability.climbing.ClimbingHandler;
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
        //maybe should be capability?
        //if (!worldIn.isRemote) {
        // add toggle timer
        if (worldIn.isRemote() && currentCooldown < 0 && canClimbOnWall(playerIn)) {
            playerIn.setNoGravity(!playerIn.hasNoGravity());
            playerIn.setMotion(Vector3d.ZERO);
            currentCooldown = USAGE_COOLDOWN;
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
            if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
                if (climbingHandlerLazyOptional == null) {
                    climbingHandlerLazyOptional = entityIn.getCapability(CLIMBING_HANDLER_CAPABILITY);
                }

                climbingHandlerLazyOptional.ifPresent(cap -> cap.incJumps());
                //climbingHandlerLazyOptional.ifPresent(cap -> System.out.println(cap.getJumps()));

                entityIn.setNoGravity(false);
                PlayerEntity player = (PlayerEntity) entityIn;
                System.out.println("instance of ClientPlayerEntity??");
                if (player instanceof ClientPlayerEntity) {
                    System.out.println("yessssss");
                    ((ClientPlayerEntity) player).movementInput.jump = true;
                }
                player.jump();
            } else if (entityIn.getMotion().getX() != 0.0 && entityIn.getMotion().getZ() != 0.0) {
                entityIn.setNoGravity(false);
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

    public void onLeap() {
        // if on client, handle movement

        // if on server, switch gravity on and update capability
    }

    public void onAttach() {
        // if on client, handle movement

        // if on server, switch gravity off and update capability
    }

    public void onRelease() {
        // switch gravity on and update capability
    }
}
