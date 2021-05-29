package com.example.examplemod.utility;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;

import java.util.OptionalLong;

public class MockDimensionType extends DimensionType {

    public static DimensionType createMock(){
        return OVERWORLD_TYPE;
        //return new MockDimensionType(OptionalLong.of(1), false, false, false, false, 1.0D, false, false, false, false, 200, new ResourceLocation(""), new ResourceLocation(""), 10.0f);
    }

    protected MockDimensionType(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, ResourceLocation infiniburn, ResourceLocation effects, float ambientLight) {
        super(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, infiniburn, effects, ambientLight);
    }
}
