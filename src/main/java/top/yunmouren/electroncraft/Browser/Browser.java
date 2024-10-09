package top.yunmouren.electroncraft.Browser;

import net.minecraft.client.Minecraft;
import top.yunmouren.electroncraft.Browser.Api.Api;
import top.yunmouren.electroncraft.ElectronCraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Map;

public class Browser {
    private Process process;
    public int BrowserPort = RandomPort();
    public Api api = new Api();
    public Browser() {
        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(getBroswerPath());
                Map<String, String> environment = processBuilder.environment();
                environment.put("LANG", "en_US.UTF-8");
                environment.put("BrowserPort", String.valueOf(BrowserPort));
                process = processBuilder.start();
                handleOutput(process);
            } catch (IOException | NullPointerException e) {
                ElectronCraft.logger.error(e.getMessage());
            }
        }).start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process != null) process.destroyForcibly();
        }));
    }

    public String getBroswerPath() {
        //Judgment System
        return switch (System.getProperty("os.name").toLowerCase()) {
            case "linux", "mac" ->
                    Minecraft.getInstance().gameDirectory.getAbsolutePath() + "\\minecraft_of_electron\\minecraft_of_electron";
            case "windows" ->
                    Minecraft.getInstance().gameDirectory.getAbsolutePath() + "\\minecraft_of_electron\\minecraft_of_electron.exe";
            default -> null;
        };
    }



    private void handleOutput(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ElectronCraft.logger.debug(line);
                }
            } catch (IOException e) {
                ElectronCraft.logger.error("Error reading process output: " + e.getMessage());
            }
        }).start();
    }


    private int RandomPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            ElectronCraft.logger.error(e.getMessage());
        }
        return RandomPort();
    }
}
