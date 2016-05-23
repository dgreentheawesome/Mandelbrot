package graphics.base;

import graphics.colors.ColorScheme;
import java.awt.Color;
import math.DoubleWindow;
import math.DoubleMandelbrotCalculator;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.TreeMap;

/**
 *
 * @author David
 */
public class GraphicsController {

    public static final int WINDOW_ZOOM_UPDATE = 0;
    public static final int WINDOW_PAN_UP_UPDATE = 1;
    public static final int WINDOW_PAN_DOWN_UPDATE = 2;
    public static final int WINDOW_PAN_LEFT_UPDATE = 3;
    public static final int WINDOW_PAN_RIGHT_UPDATE = 4;
    public static final int WINDOW_COLOR_UPDATE = 5;
    public static final int ANYTHING = 6;
    public static final int MAX_SHIFT_DISTANCE = 10;
    private final ColorScheme[] schemes;
    private int colorScheme;
    private static final int THREAD_COUNT = 4;
    private DoubleWindow window;
    final private int[][] data;
    private TreeMap<Integer, Integer> colors;
    final private BufferedImage img;

    public GraphicsController(int width, int height, Insets insets) {
        width = width - insets.right - insets.left;
        height = height - insets.top - insets.bottom;
        System.out.println(width + ", " + height);
        data = new int[width][height];
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        DoubleMandelbrotCalculator.initialize(THREAD_COUNT, width, height, data);
        schemes = ColorScheme.values();
    }

    /**
     * Executes the render instructions
     *
     * @param g the graphics context
     * @param width the width of the canvas to draw on
     * @param height the height of the canvas to draw on
     */
    private int pixelShiftDistance = 1;

    private int lastCommand;
    
    void render(Graphics2D g, boolean[] mustRender) {
        if (mustRender[ANYTHING]) {
            if (mustRender[WINDOW_ZOOM_UPDATE]) {
                DoubleMandelbrotCalculator.getHistogram().reset();
                DoubleMandelbrotCalculator.draw(window);
                lastCommand = WINDOW_ZOOM_UPDATE;
            }
            if (mustRender[WINDOW_PAN_RIGHT_UPDATE]) {
                if (lastCommand == WINDOW_PAN_RIGHT_UPDATE) {
                    pixelShiftDistance = Math.min(++pixelShiftDistance, MAX_SHIFT_DISTANCE);
                } else {
                    pixelShiftDistance = 1;
                }
                lastCommand = WINDOW_PAN_RIGHT_UPDATE;
                DoubleMandelbrotCalculator.panRight(pixelShiftDistance, window);
            }
            if (mustRender[WINDOW_PAN_LEFT_UPDATE]) {
                if (lastCommand == WINDOW_PAN_LEFT_UPDATE) {
                    pixelShiftDistance = Math.min(++pixelShiftDistance, MAX_SHIFT_DISTANCE);
                } else {
                    pixelShiftDistance = 1;
                }
                lastCommand = WINDOW_PAN_LEFT_UPDATE;
                DoubleMandelbrotCalculator.panLeft(pixelShiftDistance, window);
            }
            if (mustRender[WINDOW_PAN_DOWN_UPDATE]) {
                if (lastCommand == WINDOW_PAN_DOWN_UPDATE) {
                    pixelShiftDistance = Math.min(++pixelShiftDistance, MAX_SHIFT_DISTANCE);
                } else {
                    pixelShiftDistance = 1;
                }
                lastCommand = WINDOW_PAN_DOWN_UPDATE;
                DoubleMandelbrotCalculator.panDown(pixelShiftDistance, window);
            }
            if (mustRender[WINDOW_PAN_UP_UPDATE]) {
                if (lastCommand == WINDOW_PAN_UP_UPDATE) {
                    pixelShiftDistance = Math.min(++pixelShiftDistance, MAX_SHIFT_DISTANCE);
                } else {
                    pixelShiftDistance = 1;
                }
                lastCommand = WINDOW_PAN_UP_UPDATE;
                DoubleMandelbrotCalculator.panUp(pixelShiftDistance, window);
            }
            if (mustRender[WINDOW_COLOR_UPDATE]) {
                lastCommand = WINDOW_COLOR_UPDATE;
                colorScheme = ++colorScheme % schemes.length;
            }
            colors = ColorScheme.generate(DoubleMandelbrotCalculator.getHistogram(), schemes[colorScheme]);
            color(img, data, colors);
            Arrays.fill(mustRender, false);
        }
        g.drawImage(img, null, 0, 0);
        g.setColor(Color.red);
        String str = window.toPresentationString();
        int textLength = Math.min(data.length, str.length() / 75 * data.length);
        g.setFont(GraphicsUtilities.fillRect(str, g, textLength, MAX_TEXT_HEIGHT));
        g.drawString(str, 0, data[0].length - MAX_TEXT_HEIGHT / 2);

    }

    private static final int MAX_TEXT_HEIGHT = 40;

    public void color(BufferedImage image, int[][] mandelbrotData, TreeMap<Integer, Integer> colors) {
        long start = System.currentTimeMillis();
        for (int x = 0; x < mandelbrotData.length; x++) {
            for (int y = 0; y < mandelbrotData[0].length; y++) {
                Integer color = colors.get(mandelbrotData[x][y]);
                //colors.replace(colors.lastKey(), Color.RED.getRGB()); //makes the most expensive renders be red
                if (color == null) {
                    img.setRGB(x, y, Color.RED.getRGB());
                } else {
                    img.setRGB(x, y, color);
                    //This is a problem, needs optimization, O(N^2) is BAD
                    //jk its like .1% of processor time lol
                }
//                img.setRGB(x, y, mandelbrotData[x][y] > -1 ? Color.RED.getRGB() : Color.BLACK.getRGB());
            }
        }
        long stop = System.currentTimeMillis();
        System.out.println("Pixel coloring took " + (stop - start) + " ms");
    }

    public void setColorScheme(int colorScheme) {
        this.colorScheme = colorScheme;
    }

    public void setWindow(DoubleWindow window) {
        this.window = window;
    }

    public DoubleWindow getWindow() {
        return window;
    }

    public int getPixelShiftDistance() {
        return pixelShiftDistance;
    }

    public void setPixelShiftDistance(int pixelShiftDistance) {
        this.pixelShiftDistance = pixelShiftDistance;
    }

}
