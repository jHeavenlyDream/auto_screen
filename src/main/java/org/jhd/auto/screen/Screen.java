package org.jhd.auto.screen;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen {

    public static BufferedImage create() {
        BufferedImage image = null;
        try {
            image =  new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            e.printStackTrace();
        }

        return image;
    }
}
