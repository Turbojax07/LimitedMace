package org.turbojax;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * One-tick guard so a single shift-click can't pull multiple maces.
 */
public final class ClickGuard {
    private static final ConcurrentHashMap<UUID, Long> LAST_MACE_TICK = new ConcurrentHashMap<>();

    private ClickGuard() {}

    public static void markThisTick(ServerPlayerEntity player, ServerWorld world) {
        LAST_MACE_TICK.put(player.getUuid(), world.getTime());
    }

    public static boolean isThisTick(ServerPlayerEntity player, ServerWorld world) {
        Long t = LAST_MACE_TICK.get(player.getUuid());
        return t != null && t == world.getTime();
    }
}