/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import architecture.Window;

/**
 *
 * @author David
 */
public class ImageWriter {

    String path;
    BufferedImage img2;
    Graphics2D g2d;

    public ImageWriter(String path, int width, int height) {
        this.path = path;

        img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        c = 0;
        g2d = img2.createGraphics();
        g2d.setBackground(Color.BLACK);

    }

   
    private int c;
    static String prefix = "img";
    public void writeToFile(BufferedImage img, Window w, boolean staticNumbering) {
        long start = System.currentTimeMillis();
        g2d.clearRect(0, 0, img2.getWidth(), img2.getHeight());
        g2d.drawImage(img, 0, 0, null);
        File outputfile = new File(String.format("%s\\%s%013d.jpg",path,prefix, staticNumbering ? c++ : start));
        try {
            outputfile.createNewFile();
            ImageIO.write(img2, "jpg", outputfile);
            System.out.print("Wrote image to " + outputfile);
        } catch (IOException ex) {
            System.out.println("IOException while writing image");
        }
        long stop = System.currentTimeMillis();
        System.out.println(" in " + (stop - start) + " ms");
    }
}
