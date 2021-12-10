package com.example.examplemod.setup;

import com.example.examplemod.CallOfTheVoidMod;
import com.example.examplemod.capability.ModCapabilities;
import com.example.examplemod.capability.climbing.CapabilityClimbing;
import com.example.examplemod.capability.climbing.ClimbingHandler;
import com.example.examplemod.network.ModPacketHandler;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftGame;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Registration {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CallOfTheVoidMod.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CallOfTheVoidMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CallOfTheVoidMod.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CallOfTheVoidMod.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CallOfTheVoidMod.MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CallOfTheVoidMod.MOD_ID);


    public static void register(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SOUNDS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CONTAINERS.register(modEventBus);
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLE_TYPES.register(modEventBus);
        ModPacketHandler.registerPackets();

        ModSounds.init();
        ModItems.register();
        ModBlocks.register();
        ModContainerTypes.register();
    }

    @SubscribeEvent
    public static void registerCapabilities(FMLCommonSetupEvent evt) {
        ModCapabilities.register();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> evt){
        ModTileEntityTypes.register();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerScreens(RegistryEvent.Register<ContainerType<?>> evt){
        //ModTileEntityTypes.register();
        ModScreens.init();
    }

    /*
    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> evt) {
        System.out.println("attachin capabilities");
        if (evt.getObject() instanceof PlayerEntity) {
            ClimbingHandler instance = new ClimbingHandler();
            evt.addCapability(CapabilityClimbing.CLIMBING_HANDLER_CAPABILITY_NAME,
                    new ICapabilityProvider() {
                        @Nonnull
                        @Override
                        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                            if (cap == CapabilityClimbing.CLIMBING_HANDLER_CAPABILITY)
                                return LazyOptional.of(() -> instance).cast();
                            return LazyOptional.empty();
                        }
                    });
        }
    }
    */
}
