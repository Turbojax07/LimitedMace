package com.example.limitedmace;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;

public class LimitedMaceState extends PersistentState {
    public static final String ID = "limited_mace_state";
    public boolean crafted = false;

    public static final PersistentStateType<LimitedMaceState> TYPE = new PersistentStateType<>(
            ID,
            ctx -> new LimitedMaceState(),
            ctx -> RecordCodecBuilder.create(inst -> inst
                    .group(Codec.BOOL.fieldOf("crafted").forGetter(s -> s.crafted))
                    .apply(inst, crafted -> {
                        LimitedMaceState s = new LimitedMaceState();
                        s.crafted = crafted;
                        return s;
                    })
            ),
            DataFixTypes.LEVEL
    );

    public static LimitedMaceState get(ServerWorld world) {
        PersistentStateManager psm = world.getPersistentStateManager();
        return psm.getOrCreate(TYPE);
    }
}
