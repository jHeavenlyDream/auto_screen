package org.jhd.auto.screen;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class AutoScreenScheduler extends TimerTask {

    private String fileExtension;
    private String path;

    @Override
    public void run() {
        String timeStamp = new SimpleDateFormat("HH:mm:ss.dd.MM.yyyy").format(new Date());
        String tFile = path + "/" + timeStamp + "." + fileExtension;
        File file = new File(tFile);
        try {
            ImageIO.write(Screen.create(), fileExtension, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
