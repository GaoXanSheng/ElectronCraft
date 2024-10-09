package top.yunmouren.electroncraft.Browser.IPC;


import com.google.gson.JsonObject;
import top.yunmouren.electroncraft.Browser.Handler.Handler;
import top.yunmouren.electroncraft.ElectronCraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class TcpClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public void start(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            new Thread(this::listenForMessages).start();
        } catch (Exception e) {
            ElectronCraft.logger.error(e.getMessage());
        }
    }

    /**
     *<p>
     *     interface iHandler {
     * 	    to?: 'Minecraft',
     * 	    from: 'Minecraft' | 'Browser' | 'Node'
     * 	    type: string
     * 	    data: any
     *     }
     *</p>
     *   If 'to' exists, it will be sent to Minecraft
     *
     */
    public void sendMessage(JsonObject message) {
        if (out == null)return;
        this.out.println(encodeToBase64UrlSafe(message.toString()));
    }
    /**
     * ConvertStringsToURLSafeBase64Encoding
     *
     * @param input TheStringToBeEncoded
     * @return Base64EncodedStrings
     */
    public static String encodeToBase64UrlSafe(String input) {
        Base64.Encoder encoder = Base64.getUrlEncoder();
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] encodedBytes = encoder.encode(inputBytes);
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    private String receiveMessage() {
        try {
            return in.readLine(); // 确保服务器发送的消息包含换行符
        } catch (Exception e) {
            ElectronCraft.logger.error(e.getMessage());
            return null;
        }
    }

    private void listenForMessages() {
        try {
            String msg;
            while ((msg = receiveMessage()) != null) {
                new Handler(msg);
            }
        } catch (Exception e) {
            ElectronCraft.logger.error(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                ElectronCraft.logger.error(e.getMessage());
            }
        }
    }
}
