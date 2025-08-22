package com.example.limitedmace;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class LimitedMaceMod implements ModInitializer {
    public static final String MODID = "limited_mace";

    @Override
    public void onInitialize() {
        System.out.println("LimitedMace initializing");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("limitedmace")
                    .requires(src -> src.hasPermissionLevel(2))
                    .then(literal("reset").executes(ctx -> {
                        ServerCommandSource src = ctx.getSource();
                        ServerWorld world = src.getWorld();
                        LimitedMaceState state = LimitedMaceState.get(world);
                        state.crafted = false;
                        state.markDirty();
                        src.sendFeedback(() -> Text.literal("LimitedMace flag reset"), false);
                        return 1;
                    }))
                    .then(literal("state").executes(ctx -> {
                        ServerCommandSource src = ctx.getSource();
                        LimitedMaceState s = LimitedMaceState.get(src.getWorld());
                        src.sendFeedback(() -> Text.literal("LimitedMace crafted=" + s.crafted), false);
                        return 1;
                    }))
            );
        });
    }
}
