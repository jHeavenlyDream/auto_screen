package org.jhd.auto.screen;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.Timer;

public class MainApp {

    private PopupMenu menu;
    private CheckboxMenuItem autoItem;
    private Timer timer;
    private AutoScreenScheduler autoScreenScheduler;
    private String path;
    private int period;

    public MainApp() {
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null, "Not support tray");
            return;
        }
        period = 20000;
        timer = new Timer();
        autoScreenScheduler = new AutoScreenScheduler();
        autoScreenScheduler.setFileExtension("png");
        autoScreenScheduler.setPath("");

        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage("icon.png");

        menu = new PopupMenu();
        Menu autoScreenMenu = new Menu("Авто");
        autoItem = new CheckboxMenuItem("Автоматические снимки");
        MenuItem pathItem = new MenuItem("Путь сохранения");
        MenuItem timeItem = new MenuItem("Интервал времени");
        autoScreenMenu.add(autoItem);
        autoScreenMenu.add(pathItem);
        autoScreenMenu.add(timeItem);

        MenuItem screenItem = new MenuItem("Сделать снимок");
        MenuItem closeItem = new MenuItem("Выход");

        menu.add(autoScreenMenu);
        menu.add(screenItem);
        menu.addSeparator();
        menu.add(closeItem);

        closeItem.addActionListener(e -> System.exit(0));
        screenItem.addActionListener(e -> createScreen());
        autoItem.addItemListener(e -> autoScreen());
        pathItem.addActionListener(e -> changePathToSave());
        timeItem.addActionListener(e -> changeTimeInterval());

        TrayIcon trayIcon = new TrayIcon(image, "Auto screen", menu);
        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
    }

    private void createScreen(){
        //даем время скрыться меню
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //создаем снимок
        BufferedImage image = Screen.create();
        if(image == null) return;

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and JPEG files", "png", "jpeg", "jpg");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int res = fileChooser.showSaveDialog(null);

        if (res == JFileChooser.APPROVE_OPTION){

            try {
                ImageIO.write(image,
                              FilenameUtils.getExtension(fileChooser.getSelectedFile().getAbsolutePath()),
                              fileChooser.getSelectedFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void autoScreen(){
        System.out.println("check");
        if(autoItem.getState()){
            System.out.println("start scheduler");
            timer.schedule(autoScreenScheduler, 0, period);
        }else{
            System.out.println("stop scheduler");
            timer.cancel();
        }
    }

    private void changePathToSave(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            autoScreenScheduler.setPath(fileChooser.getSelectedFile().getPath());
        }
    }

    private void changeTimeInterval(){
        try {
            int value = Integer.parseInt(JOptionPane.showInputDialog("Введите количество минут",
                    JOptionPane.INPUT_VALUE_PROPERTY));
            if(value <= 0) {
                JOptionPane.showMessageDialog(null, "no correct");
            }else{
                period = value  * 60 * 1000;
            }

        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "no correct");
        }

    }
}
