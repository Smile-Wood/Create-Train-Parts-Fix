package com.tiestoettoet.create_train_parts;

import com.tiestoettoet.create_train_parts.item.ModItems;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.neoforge.mods.toml file
@Mod(CreateTrainParts.MOD_ID)
public class CreateTrainParts
{

    public static final String MOD_ID = "create_train_parts";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static CreateRegistrate registrate;

    public CreateTrainParts(IEventBus modEventBus, ModContainer modContainer)
    {
//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        registrate = CreateRegistrate.create(MOD_ID).registerEventListeners(modEventBus);

        AllBlocks.register();

        AllBlockEntityTypes.register();

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
    }

    public static CreateRegistrate registrate()
    {
        return registrate;
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("create_train_parts is starting up on the server side!");
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("create_enchantment_industry_plus is starting up on the client side!");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
