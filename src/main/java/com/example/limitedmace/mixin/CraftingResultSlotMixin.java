package com.example.limitedmace.mixin;

import com.example.limitedmace.LimitedMaceState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public abstract class CraftingResultSlotMixin {

    // EXACT Yarn signature for 1.21.8:
    @Inject(
            method = "onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void limitedMace$onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {

        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }

        boolean isMace = stack.isOf(Items.MACE);
        //System.out.println("[LimitedMace] isMace? " + isMace);

        if (!isMace) {
            return;
        }

        ServerWorld world = serverPlayer.getWorld();
        LimitedMaceState state = LimitedMaceState.get(world);

        if (state.crafted) {
            serverPlayer.sendMessage(Text.literal("Only one mace can ever be crafted on this world."), false);
            ci.cancel();
            return;
        }

        state.crafted = true;
        state.markDirty();

        com.example.limitedmace.ClickGuard.markThisTick(serverPlayer, world);

        //System.out.println("[LimitedMace] First mace crafted -> flag set true (allowing take).");
    }

    private static String describe(ItemStack stack) {
        try {
            Item item = stack.getItem();
            String key = Item.getRawId(item) + "";
            String name = String.valueOf(item);
            return "item=" + name + " rawId=" + key + " count=" + stack.getCount();
        } catch (Throwable t) {
            return "<?> (" + t.getClass().getSimpleName() + ")";
        }
    }
}
