package com.example.examplemod.particle;

import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SparkParticle extends SpriteTexturedParticle {

    private static ResourceLocation SPARK_SPRITE = new ResourceLocation("callofthevoidmod", "particle/spark");

    protected SparkParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SparkParticle particle = new SparkParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.maxAge = 10;
            particle.selectSpriteWithAge(spriteSet);
            return particle;
        }
    }
}
