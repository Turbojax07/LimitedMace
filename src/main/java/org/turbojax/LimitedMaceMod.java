package org.turbojax;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.simpleyaml.configuration.file.YamlFile;

public class LimitedMaceMod implements ModInitializer {
    public static final String MOD_ID = "limited_mace";
    public static final YamlFile config = new YamlFile("config/limited_mace_config.yml");

    @Override
    public void onInitialize() {
        System.out.println("LimitedMace initializing");

        // Load the YAML file if exists or create new one if it doesn't
        config.addDefault("max-maces", 1);
        loadConfig();

        // Creating the command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    CommandManager.literal("limitedmace")
                            .requires(src -> src.hasPermissionLevel(2))
                            .then(CommandManager.literal("reset").executes(ctx -> {
                                ServerCommandSource src = ctx.getSource();
                                LimitedMaceState state = LimitedMaceState.get(src.getServer());
                                state.setCrafted(0);
                                state.markDirty();
                                src.sendFeedback(() -> Text.literal("LimitedMace flag reset"), false);
                                return 1;
                            })).then(CommandManager.literal("state").executes(ctx -> {
                                ServerCommandSource src = ctx.getSource();
                                LimitedMaceState s = LimitedMaceState.get(src.getServer());
                                src.sendFeedback(() -> Text.literal(s.getCrafted() + " Maces have been crafted."), false);
                                return 1;
                            })).then(CommandManager.literal("reload").executes(ctx -> {
                                loadConfig();
                                return 1;
                            })));
        });
    }

    public static int getMaxMaces() {
        return config.getInt("max-maces");
    }

    public static boolean loadConfig() {
        try {
            if (!config.exists()) {
                config.createNewFile();

                config.save();

                System.out.println("New file has been created: " + config.getFilePath() + "\n");
            } else {
                System.out.println(config.getFilePath() + " already exists, loading configurations...\n");
            }
            config.load();
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}