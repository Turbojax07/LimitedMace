package org.turbojax.mixin;

import org.turbojax.LimitedMaceMod;
import org.turbojax.LimitedMaceState;
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
    @Inject(method = "onTakeItem(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",at = @At("HEAD"),cancellable = true)
    private void limitedMace$onTakeItem(PlayerEntity player, ItemStack stack, CallbackInfo ci) {

        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }

        boolean isMace = stack.isOf(Items.MACE);
        //System.out.println("[LimitedMace] isMace? " + isMace);

        if (!isMace) {
            return;
        }

        LimitedMaceState state = LimitedMaceState.get(serverPlayer.getServer());

        if (state.getCrafted() == LimitedMaceMod.getMaxMaces()) {
            String insert = LimitedMaceMod.getMaxMaces() + "mace" + (LimitedMaceMod.getMaxMaces() == 1 ? "" : "s");
            serverPlayer.sendMessage(Text.literal("Only " + insert + " can ever be crafted."), false);
            ci.cancel();
            return;
        }

        state.setCrafted(state.getCrafted() + 1);
        state.markDirty();

        org.turbojax.ClickGuard.markThisTick(serverPlayer, serverPlayer.getServer().getOverworld());

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
