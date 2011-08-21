package userInterface.GUI.systemTray;

import core.interfaces.listeners.INewElementListener;
import core.Podcast;
import core.interfaces.listeners.IDownloadListener;
import core.interfaces.listeners.INetworkErrorListener;
import core.myRadioLogger;
import data.Item;
import userInterface.GUI.*;
import userInterface.GUI.exceptions.NoSystemTrayException;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

public class SystemTrayElement implements INewElementListener,
        INetworkErrorListener,IDownloadListener{

    TrayIcon trayIcon;
    SystemTray systemTray;
    MainWindow mainWindow;

    public SystemTrayElement(MainWindow mainWindow) throws NoSystemTrayException {
        //Creating system tray element
        if (SystemTray.isSupported()) {
            this.mainWindow = mainWindow;
            systemTray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/trayIcon.gif"));

            trayIcon = new TrayIcon(image, "myRadio");
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    showMainWindows();
                }
            });

            try {
                //Adding element to the traybar
                systemTray.add(trayIcon);
            } catch (AWTException ex) {
                myRadioLogger.getLogger().log(Level.SEVERE, "Error adding system tray", ex);
            }

        } else {
            throw new NoSystemTrayException();
        }
    }

    private void showMainWindows() {
        mainWindow.setVisible(true);
        mainWindow.setExtendedState(MainWindow.NORMAL);
    }

    @Override
    public void newElementFound(Podcast podcast) {
        trayIcon.displayMessage("New item(s) found",podcast.getName(),TrayIcon.MessageType.INFO);
    }

    @Override
    public void networkErrorDetected(Exception exception) {
        trayIcon.displayMessage("Network Error",exception.toString(),TrayIcon.MessageType.ERROR);
    }

    @Override
    public void downloadCompleted(Item item) {
        trayIcon.displayMessage("Download completed",item.getTitle(),TrayIcon.MessageType.INFO);
    }
}
