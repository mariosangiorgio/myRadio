package core;

import core.interfaces.listeners.IDownloadListener;
import core.interfaces.listeners.INetworkErrorListener;
import core.utils.PathHandler;
import data.Item;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Vector;

public class Downloader extends Thread {

    private Item item;
    private String filePath;
    private Collection<IDownloadListener> listeners;
    private int downloadedBytes;
    private Collection<INetworkErrorListener> networkErrorListeners;

    public Downloader(Item item, String baseFilePath,
            Collection<INetworkErrorListener> networkErrorListeners) {
        this.item = item;
        this.filePath = filePath(baseFilePath);
        this.networkErrorListeners = networkErrorListeners;
        listeners = new Vector<IDownloadListener>();
        downloadedBytes = 0;
    }
    
    public String filePath(String baseFilePath){
    	// This was added after setting download folder option was added.
    	// Needs to make the folder and non-existing parent folders at the first instance.
    	File makedir = new File(baseFilePath);
    	if (!makedir.exists())
    		makedir.mkdirs();
    	
        filePath =
                baseFilePath+"/"+PathHandler.removeForbiddenCharachters(item.getTitle()+item.getFileExtension());
        return PathHandler.avoidNameCollision(filePath);
    }

    public int getTotal() {
        return item.getSize();
    }

    public int getDownloaded() {
        return downloadedBytes;
    }

    public void addListener(IDownloadListener uiListener) {
        listeners.add(uiListener);
    }

    private void notifyDownloadComplete() {
        for (IDownloadListener listener : listeners) {
            listener.downloadCompleted(item);
        }
    }

    private void download(URL uri, String path) throws MalformedURLException, IOException {
        InputStream fileLocation = uri.openStream();
        int read;
        byte[] buffer = new byte[1024 * 100];
        FileOutputStream file = new FileOutputStream(path);
        while ((isInterrupted() == false) &&
                (read = fileLocation.read(buffer)) != -1) {
            file.write(buffer, 0, read);
            downloadedBytes += read;
        }
        fileLocation.close();
        file.close();
        if (isInterrupted()) {
            //Cleaning up partial files
            File partialFile = new File(path);
            partialFile.delete();
        }
    }

    @Override
    public void run() {
        try {
            //Using the / charachter because it is portable 
            download(item.getURL(), filePath);
            if (!interrupted()) {
                notifyDownloadComplete();
            }
        } catch (Exception ex) {
            for (INetworkErrorListener networkErrorListener : networkErrorListeners) {
                networkErrorListener.networkErrorDetected(ex);
            }
        }
    }

    public void cancelDownload() {
        //Stopping download
        this.interrupt();
    }

    public String getDownloadTitle() {
        return item.getTitle();
    }
}
