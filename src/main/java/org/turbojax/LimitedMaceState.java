package org.turbojax;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import net.minecraft.world.World;

public class LimitedMaceState extends PersistentState {
    private int crafted = 0;

    private LimitedMaceState() {}

    private LimitedMaceState(int crafted) {
        this.crafted = crafted;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("macesCrafted", crafted);
        return nbt;
    }

    public static LimitedMaceState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        return new LimitedMaceState(nbt.getInt("macesCrafted"));
    }
 
    public int getCrafted() {
        return crafted;
    }

    public void setCrafted(int crafted) {
        this.crafted = crafted;
    }

// FOR NEWER VERSIONS
//import net.minecraft.world.PersistentStateType;
//    private static final Codec<LimitedMaceState> CODEC = Codec.INT.fieldOf("macesCrafted").codec().xmap(
//            LimitedMaceState::new, // create a new 'LimitedMaceState' from the stored number
//            LimitedMaceState::getCrafted // return the number from the 'LimitedMaceState' to be saved
//    );
//
//    private static final PersistentStateType<LimitedMaceState> type = new Type<>(
//            LimitedMaceMod.MOD_ID,
//            LimitedMaceState::new, // If there's no 'LimitedMaceState' yet create one and refresh variables
//            CODEC, // If there is a 'LimitedMaceState' NBT, parse it with 'CODEC'
//            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
//    );

    public static LimitedMaceState get(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        assert serverWorld != null;

        Type<LimitedMaceState> type = new Type<>(
                LimitedMaceState::new,
                LimitedMaceState::fromNbt,
                null
        );
        // The first time the following 'getOrCreate' function is called, it creates a brand new 'StateSaverAndLoader' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'StateSaverAndLoader' NBT on disk to the codec in our type, using the codec to decode the nbt into our state
        LimitedMaceState state = serverWorld.getPersistentStateManager().getOrCreate(type, LimitedMaceMod.MOD_ID);
 
        // If state is not marked dirty, nothing will be saved when Minecraft closes.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();
 
        return state;
    }
}
