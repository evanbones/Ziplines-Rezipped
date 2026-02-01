package com.evandev.zipline;

import com.evandev.zipline.client.ClientConfigSetup;
import com.evandev.zipline.config.ReloadListener;
import com.evandev.zipline.config.RuleManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(ReliableRemoverMod.MOD_ID)
public class ReliableRemoverMod {
    public ReliableRemoverMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(this::addReloadListener);

        if (FMLEnvironment.dist.isClient()) {
            ClientConfigSetup.register(modContainer);
        }
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ReliableRemoverCommands.register(event.getDispatcher());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        RuleManager.load();
    }

    private void addReloadListener(final AddReloadListenerEvent event) {
        event.addListener(new ReloadListener());
    }
}