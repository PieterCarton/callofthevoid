package com.example.examplemod.item;

import com.example.examplemod.capability.climbing.ClimbingHandler;
import com.example.examplemod.network.CClimbingActionPacket;
import com.example.examplemod.network.ModPacketHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class ClimbingPickItem extends PickaxeItem {

    @CapabilityInject(ClimbingHandler.class)
    private static Capability<ClimbingHandler> CLIMBING_HANDLER_CAPABILITY = null;

    private static final AttributeModifier SLIDE_FALL_SPEED_REDUCTION = new AttributeModifier("Slide fall-speed reduction", -0.06, AttributeModifier.Operation.ADDITION);
    final double CLING_DISTANCE = 0.1;
    final int USAGE_COOLDOWN = 5;
    final int MAX_LEAPS = 5;
    int currentCooldown = 0;

    public ClimbingPickItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!playerIn.world.isRemote) {
            ServerWorld world = (ServerWorld) playerIn.world;
            System.out.println("particles spawned");
            Vector3d particlePos = playerIn.getPositionVec();
            world.spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState()), particlePos.getX(), particlePos.getY(), particlePos.getZ(), 10, 0.0, 0.0, 0.0, 0.3);
        }
        if (worldIn.isRemote() && currentCooldown < 0 && canClimbOnWall(playerIn)) {
            currentCooldown = USAGE_COOLDOWN;
            if (playerIn.hasNoGravity() == true) {
                onRelease(playerIn);
            } else {
                playerIn.swingArm(handIn);
                worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                onAttach(playerIn);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    // TODO needs refactor
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!(entityIn instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) entityIn;
        LazyOptional<ClimbingHandler> climbingCapability = player.getCapability(CLIMBING_HANDLER_CAPABILITY);
        ClimbingHandler cap = climbingCapability.orElse(new ClimbingHandler());

        // check if attached blocks still valid
        if (cap.isClimbing()) {
            Direction attachDirection = cap.getAttachDirection();
            BlockPos upperLookBlockPos = player.getPosition().add(attachDirection.getDirectionVec());
            BlockPos lowerLookBlockPos = upperLookBlockPos.add(Direction.DOWN.getDirectionVec());

            if (!worldIn.getBlockState(upperLookBlockPos).getMaterial().blocksMovement() && !worldIn.getBlockState(lowerLookBlockPos).getMaterial().blocksMovement()) {
                onRelease(player);
            }
        }

        if (cap.isClimbing() && player.isOnGround()) {
            onLand(player);
        }

        if (cap.isSliding()) {
            player.fallDistance = 0.0f;
            if (!player.isCrouching() && cap.getStableHeight() >= player.getPosY() && worldIn.isRemote) {
                // attach once again when stable height has been reached
                onAttach(player);
            }
            if(player.ticksExisted % 10 == 0) {
                worldIn.playSound(player, player.getPosition(), SoundEvents.BLOCK_SOUL_SAND_STEP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                if (!player.world.isRemote) {
                    Vector3d particlePos = player.getPositionVec();
                    ((ServerWorld) worldIn).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState()), particlePos.getX(), particlePos.getY(), particlePos.getZ(), 10, 0.0, 0.0, 0.0, 0.3);
                }
            }
        }

        if (worldIn.isRemote()) {
            if (entityIn.getMotion().getX() != 0.0 || entityIn.getMotion().getZ() != 0.0) {
                onRelease(player);
            } else if (player.hasNoGravity()) {
                if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
                    System.out.println("leap");
                    onLeap(player);
                } else if(player.isCrouching() && !cap.isSliding()) {
                    onStartSliding(player);
                }
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

        if (player.world.getBlockState(upperLookBlockPos).getMaterial().blocksMovement() || player.world.getBlockState(lowerLookBlockPos).getMaterial().blocksMovement()) {
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

    //TODO: code seems somewhat buggy: write tests
    //TODO: decide whether code below belong to capability

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

    /**
     * Executed when player tries to attach to wall
     * @param player
     */
    public void onAttach(PlayerEntity player) {
        if (!player.world.isRemote) {
            ServerWorld world = (ServerWorld) player.world;
            Vector3d particlePos = player.getLookVec().add(player.getPosX(), player.getEyeHeight(), player.getPosZ());
            world.spawnParticle(ParticleTypes.CRIT, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 10, 0.0, 0.0, 0.0, 0.5);
        }
        // check if player is still allowed to attach to wall
        LazyOptional<ClimbingHandler> climbingCapability = player.getCapability(CLIMBING_HANDLER_CAPABILITY);
        ClimbingHandler cap = climbingCapability.orElse(new ClimbingHandler());

        // if player is no longer allowed to attach, start sliding
        if (cap.getJumps() >= MAX_LEAPS && cap.getStableHeight() < player.getPosY()) {
            onStartSliding(player);
            return;
        }

        // stop sliding
        removeSlidingModifier(player);
        cap.setSliding(false);
        cap.setClimbing(true);

        // switch gravity off and update capability
        player.setNoGravity(true);
        player.fallDistance = 0.0f;

        // update capability
        cap.setStableHeight(player.getPosY());

        if (player.world.isRemote) {
            // if on client, handle movement and inform server of action
            player.setMotion(Vector3d.ZERO);
            ModPacketHandler.INSTANCE.sendToServer(new CClimbingActionPacket(CClimbingActionPacket.ClimbingAction.ATTACH, player.getPosY()));
        }
    }

    public void onRelease(PlayerEntity player) {
        LazyOptional<ClimbingHandler> climbingCapability = player.getCapability(CLIMBING_HANDLER_CAPABILITY);
        climbingCapability.ifPresent(cap -> cap.setSliding(false));
        player.setNoGravity(false);
        removeSlidingModifier(player);

        if (player.world.isRemote) {
            // if on client, inform server of action
            ModPacketHandler.INSTANCE.sendToServer(new CClimbingActionPacket(CClimbingActionPacket.ClimbingAction.RELEASE, player.getPosY()));
        }
    }

    public void onLand(PlayerEntity player) {
        // reset number of jumps, stop sliding
        System.out.println("Side: " + player.world.isRemote);
        System.out.println("landed");
        LazyOptional<ClimbingHandler> climbingCapability = player.getCapability(CLIMBING_HANDLER_CAPABILITY);
        climbingCapability.ifPresent(cap -> cap.resetJumps());
        climbingCapability.ifPresent(cap -> cap.setSliding(false));
        climbingCapability.ifPresent(cap -> cap.setClimbing(false));
        player.setNoGravity(false);
        removeSlidingModifier(player);
    }

    public void onStartSliding(PlayerEntity player) {

        LazyOptional<ClimbingHandler> climbingCapability = player.getCapability(CLIMBING_HANDLER_CAPABILITY);
        ClimbingHandler cap = climbingCapability.orElse(new ClimbingHandler());
        if (cap.isSliding()) {
            return;
        }

        cap.setSliding(true);
        addSlidingModifier(player);
        player.setNoGravity(false);

        // if on client, handle movement and inform server of action
        if (player.world.isRemote) {
            player.setMotion(Vector3d.ZERO);
            ModPacketHandler.INSTANCE.sendToServer(new CClimbingActionPacket(CClimbingActionPacket.ClimbingAction.SLIDE, player.getPosY()));
        }

    }

    public void addSlidingModifier(PlayerEntity player) {
        // apply sliding attribute modifier, if not already present
        ModifiableAttributeInstance attributeInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        if (!attributeInstance.hasModifier(SLIDE_FALL_SPEED_REDUCTION)) {
            attributeInstance.applyPersistentModifier(SLIDE_FALL_SPEED_REDUCTION);
        }
    }

    public void removeSlidingModifier(PlayerEntity player) {
        // remove sliding attribute modifier, if present
        ModifiableAttributeInstance attributeInstance = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
        if (attributeInstance.hasModifier(SLIDE_FALL_SPEED_REDUCTION)) {
            attributeInstance.removeModifier(SLIDE_FALL_SPEED_REDUCTION);
        }
    }
}
