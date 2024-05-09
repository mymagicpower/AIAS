package me.aias.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePlotter {

    public static void plotDensity(float[][] density) {
        int width = density[0].length;
        int height = density.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = density[y][x];
                int rgb = getColor(value);
                image.setRGB(x, y, rgb);
            }
        }

        File output = new File("density_plot.jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getColor(double value) {
        // Jet colormap implementation
        double[][] jetColors = {{0, 0, 0.5}, {0, 0, 1}, {0, 0.5, 1}, {0, 1, 1}, {0.5, 1, 0.5}, {1, 1, 0}, {1, 0.5, 0}, {1, 0, 0}, {0.5, 0, 0}};

        int numColors = jetColors.length;
        double scaledValue = value * (numColors - 1);
        int colorIndex = (int) scaledValue;
        double colorFraction = scaledValue - colorIndex;

        int[] color1 = getColorComponents(jetColors[colorIndex]);
        int[] color2 = getColorComponents(jetColors[colorIndex + 1]);

        int red = (int) (color1[0] + colorFraction * (color2[0] - color1[0]));
        int green = (int) (color1[1] + colorFraction * (color2[1] - color1[1]));
        int blue = (int) (color1[2] + colorFraction * (color2[2] - color1[2]));

        return new Color(red, green, blue).getRGB();
    }

    private static int[] getColorComponents(double[] color) {
        int[] components = new int[3];
        for (int i = 0; i < 3; i++) {
            components[i] = (int) (color[i] * 255);
        }
        return components;
    }

    public static void main(String[] args) {
        float[][] density = {
                {0.1f, 0.2f, 0.3f},
                {0.4f, 0.5f, 0.6f},
                {0.7f, 0.8f, 0.9f}
        };

        plotDensity(density);
    }
}