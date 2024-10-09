package top.yunmouren.electroncraft.Browser.Handler.entry;

import com.google.gson.JsonObject;
import top.yunmouren.electroncraft.Browser.Handler.inherit.IHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;

public class Render extends IHandler {
    public static final ArrayList<Frame> ToBeRendered = new ArrayList<>();
    private static final ArrayList<String> ToBeDecoded = new ArrayList<>();
    private static Boolean isRender = false;

    public static void setIsRender(Boolean b) {
        ToBeRendered.clear();
        ToBeDecoded.clear();
        isRender = b;
    }

    @Override
    public void Handler(JsonObject receiveMessage) {
        if (isRender) {
            ToBeDecoded.add(receiveMessage.get("Data").getAsString());
            try {
                ToBeRendered.add(new Frame(ToBeDecoded.get(0)));
                ToBeDecoded.remove(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static class Frame {
        public int width = 0;
        public int height = 0;
        public ByteBuffer imageBuffer = null;

        private ByteBuffer convertToByteBuffer(BufferedImage image) {
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4); // RGBA = 4 bytes per pixel
            for (int pixel : pixels) {
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                buffer.put((byte) (pixel & 0xFF));         // Blue
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }
            buffer.flip();
            return buffer;
        }

        public Frame(String base64Image) throws IOException {
            if (base64Image.startsWith("data:image/png;base64,")) {
                base64Image = base64Image.substring("data:image/png;base64,".length());
            }
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (bufferedImage != null) {
                ByteBuffer imageBuffer = convertToByteBuffer(bufferedImage);
                this.width = bufferedImage.getWidth();
                this.height = bufferedImage.getHeight();
                this.imageBuffer = imageBuffer;
            }
        }
    }
}
