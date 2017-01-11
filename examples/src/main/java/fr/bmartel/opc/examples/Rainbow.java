package fr.bmartel.opc.examples;

import fr.bmartel.opc.OpcClient;
import fr.bmartel.opc.OpcDevice;
import fr.bmartel.opc.PixelStrip;

/**
 * Pixel strip test.
 */
public class Rainbow {

    public static void main(String[] arg) throws Exception {
        
        String FC_SERVER_HOST = System.getProperty("fadecandy.server", "raspberrypi.local");
        int FC_SERVER_PORT = Integer.parseInt(System.getProperty("fadecandy.port", "7890"));
        int STRIP1_COUNT = Integer.parseInt(System.getProperty("fadecandy.strip1.count", "64"));

        OpcClient server = new OpcClient(FC_SERVER_HOST, FC_SERVER_PORT);
        OpcDevice fadeCandy = server.addDevice();
        PixelStrip strip = fadeCandy.addPixelStrip(0, STRIP1_COUNT);
        System.out.println(server.getConfig());
        int wait = 50;

        // Color wipe: in red, green, and blue
        for (int color : new int[]{0xFF0000, 0x00FF00, 0x0000FF}) {
            for (int i = 0; i < strip.getPixelCount(); i++) {
                strip.setPixelColor(i, color);
                server.show();
                Thread.sleep(wait);
            }
            server.clear();
            server.show();
        }

        // Rainbow
        for (int j = 0; j < 256; j++) {
            for (int i = 0; i < strip.getPixelCount(); i++) {
                strip.setPixelColor(i, colorWheel(i + j));
            }
            server.show();
            Thread.sleep(wait);
        }

        // Rainbow cycle
        for (int j = 0; j < 256 * 5; j++) {
            for (int i = 0; i < strip.getPixelCount(); i++) {
                int c = (int) Math.round(i * 256.0 / strip.getPixelCount());
                strip.setPixelColor(i, colorWheel(c + j));
            }
            server.show();
            Thread.sleep(wait);
        }

        server.clear();
        server.show();
        server.close();
    }

    /**
     * Input a value 0 to 255 to get a color value.
     * The colors are a transition r - g - b - back to r.
     */
    private static int colorWheel(int c) {
        byte n = (byte) c;
        if (n < 85) {
            return OpcClient.makeColor(n * 3, 255 - n * 3, 0);
        } else if (n < 170) {
            return OpcClient.makeColor(255 - n * 3, 0, n * 3);
        } else {
            return OpcClient.makeColor(0, n * 3, 255 - n * 3);
        }
    }

}
