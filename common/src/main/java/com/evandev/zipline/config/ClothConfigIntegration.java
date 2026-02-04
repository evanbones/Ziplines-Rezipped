package com.evandev.zipline.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigIntegration {

    public static Screen createScreen(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.zipline.title"));

        builder.setSavingRunnable(ModConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.zipline.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.zipline.option.snap_radius"), config.snapRadius)
                .setDefaultValue(2.0)
                .setTooltip(Component.translatable("config.zipline.option.snap_radius.tooltip"))
                .setSaveConsumer(newValue -> config.snapRadius = newValue)
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.zipline.option.click_reach"), config.clickReach)
                .setDefaultValue(3.0)
                .setTooltip(Component.translatable("config.zipline.option.click_reach.tooltip"))
                .setSaveConsumer(newValue -> config.clickReach = newValue)
                .build());


        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.zipline.option.use_anywhere"), config.useAnywhere)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.zipline.option.use_anywhere.tooltip"))
                .setSaveConsumer(newValue -> config.useAnywhere = newValue)
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.zipline.option.max_turn_angle"), config.maxTurnAngle)
                .setDefaultValue(0.707)
                .setTooltip(Component.translatable("config.zipline.option.max_turn_angle.tooltip"))
                .setSaveConsumer(newValue -> config.maxTurnAngle = newValue)
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.zipline.option.hang_offset"), config.hangOffset)
                .setDefaultValue(2.1)
                .setTooltip(Component.translatable("config.zipline.option.hang_offset.tooltip"))
                .setSaveConsumer(newValue -> config.hangOffset = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.zipline.option.realistic_physics"), config.realisticPhysics)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("config.zipline.option.realistic_physics.tooltip"))
                .setSaveConsumer(newValue -> config.realisticPhysics = newValue)
                .build());

        general.addEntry(entryBuilder.startDoubleField(Component.translatable("config.zipline.option.exit_jump_multiplier"), config.exitJumpMultiplier)
                .setDefaultValue(1.4)
                .setTooltip(Component.translatable("config.zipline.option.exit_jump_multiplier.tooltip"))
                .setSaveConsumer(newValue -> config.exitJumpMultiplier = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.zipline.option.consume_durability"), config.consumeDurability)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.zipline.option.consume_durability.tooltip"))
                .setSaveConsumer(newValue -> config.consumeDurability = newValue)
                .build());

        return builder.build();
    }
}