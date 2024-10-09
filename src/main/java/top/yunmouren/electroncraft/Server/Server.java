package top.yunmouren.electroncraft.Server;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import top.yunmouren.electroncraft.Server.Command.Command;
import top.yunmouren.electroncraft.Server.Network.SimpleNetwork;

import java.util.Optional;

import static top.yunmouren.electroncraft.ElectronCraft.MOD_ID;

public class Server {
    private static int id = 0;
    private static final String PROTOCOL_VERSION = "1.0";

    public Server() {
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "minecraft_of_electron"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public void setup(final FMLCommonSetupEvent event) {
        CHANNEL.registerMessage(id++, SimpleNetwork.class, SimpleNetwork::encode, SimpleNetwork::decode, SimpleNetwork::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        Command.register(dispatcher);
    }
}
