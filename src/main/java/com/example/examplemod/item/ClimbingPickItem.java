package com.example.examplemod.item;

import com.example.examplemod.capability.climbing.ClimbingHandler;
import net.minecraft.client.GameSettings;
import net.minecraft.client.KeyboardListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CInputPacket;
import net.minecraft.network.play.server.SEntityMetadataPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClimbingPickItem extends PickaxeItem {

    @CapabilityInject(ClimbingHandler.class)
    private static Capability<ClimbingHandler> CLIMBING_HANDLER_CAPABILITY = null;

    final double CLING_DISTANCE = 0.1;
    final int USAGE_COOLDOWN = 5;
    int currentCooldown = 0;

    private static DataParameter<Boolean> HANGING_FROM_WALL = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.BOOLEAN);

    public ClimbingPickItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //maybe should be capability?
        //if (!worldIn.isRemote) {
        // add toggle timer
        LazyOptional<ClimbingHandler> capability = playerIn.getCapability(CLIMBING_HANDLER_CAPABILITY, null);
        System.out.println(capability.isPresent());
        capability.ifPresent(info -> System.out.println(info.getJumps()));
        capability.ifPresent(info -> info.setJumps(info.getJumps() + 1));

        System.out.println(CLIMBING_HANDLER_CAPABILITY == null);
        if (worldIn.isRemote() && currentCooldown < 0 && canClimbOnWall(playerIn)) {
            playerIn.setNoGravity(!playerIn.hasNoGravity());
            playerIn.setMotion(Vector3d.ZERO);
            currentCooldown = USAGE_COOLDOWN;
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn.isOnGround()) {
            // reset jumps
        }

        if (entityIn.hasNoGravity() && worldIn.isRemote() && entityIn instanceof PlayerEntity) {
            if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
                entityIn.setNoGravity(false);
                PlayerEntity player = (PlayerEntity) entityIn;
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
}
