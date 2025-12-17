package com.balburg.easypathforge;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(EasyPathForge.MOD_ID)
public class EasyPathForge {
    public static final String MOD_ID = "easypathforge";

    // Deferred Register for Items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    
    // Deferred Register for Creative Mode Tabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    // Register the Path Shovel item
    public static final RegistryObject<Item> PATH_SHOVEL = ITEMS.register("path_shovel", 
        () -> new PathShovelItem(new Item.Properties().stacksTo(1).durability(500)));
    
    // Register the Forest Clearer item
    public static final RegistryObject<Item> FOREST_CLEARER = ITEMS.register("forest_clearer", 
        () -> new ForestClearerItem(new Item.Properties().stacksTo(1).durability(500)));
    
    // Register the Grass Cleaner item
    public static final RegistryObject<Item> GRASS_CLEANER = ITEMS.register("grass_cleaner", 
        () -> new GrassCleanerItem(new Item.Properties().stacksTo(1).durability(500)));

    // Register custom creative mode tab
    public static final RegistryObject<CreativeModeTab> PATH_TOOLS_TAB = CREATIVE_MODE_TABS.register("path_tools_tab",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.easypathforge.path_tools"))
            .icon(() -> new ItemStack(PATH_SHOVEL.get()))
            .displayItems((parameters, output) -> {
                output.accept(PATH_SHOVEL.get());
                output.accept(FOREST_CLEARER.get());
                output.accept(GRASS_CLEANER.get());
            })
            .build());

    public EasyPathForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register items
        ITEMS.register(modEventBus);
        
        // Register creative mode tabs
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
