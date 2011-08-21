package core;

import data.*;
import core.config.Config;
import core.interfaces.listeners.IDownloadListener;
import core.interfaces.listeners.INetworkErrorListener;
import core.interfaces.listeners.INewElementListener;
import core.utils.PathHandler;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Podcast implements IDownloadListener, Serializable {

    private static final long serialVersionUID = 7526472295622776148L;
    String name;
    String basePath;
    URL podcastURL;
    Channel channelInfo;
    Map<String, Item> items;
    Collection<Item> downloadQueue;
    transient XMLParser parser;
    transient Collection<INetworkErrorListener> networkErrorListeners;

    public Podcast(URL podcastURL, String name, Collection<INetworkErrorListener> networkErrorListeners) {
        this.podcastURL = podcastURL;
        this.name = name;
        this.basePath = PathHandler.getValidPath(name);
        this.networkErrorListeners = networkErrorListeners;
        //Making directory for podcasts
        // * Ho-Ling: I had to comment this function out for now, since
        // * the podcast directory is now set by user,
        // * having this line will make an extra directory
        // * in the same directory as where this programming
        // * is running.  This can be seen when the program
        // * is ran as a jar.
        // new File(name).mkdir();
        items = new HashMap<String, Item>();
        parser = new XMLParser(podcastURL);
        downloadQueue = new Vector<Item>();
    }

    public Map<String, Item> getDownloadedItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    void setNetworkErrorListeners(Collection<INetworkErrorListener> networkErrorListeners) {
        this.networkErrorListeners = networkErrorListeners;
    }

    private void deleteDirectory(File basePath) {
        if (basePath.isFile()) {
            basePath.delete();
            return;
        }
        if (basePath.isDirectory()) {
            for (File content : basePath.listFiles()) {
                deleteDirectory(content);
            }
            //Now the directory should be empty
            basePath.delete();
            return;
        }
    }

    public void clear() {
        deleteDirectory(new File(name));
    }

    public String getBasePath() {
    	//return name;
    	// This is the correct base path if using Config class to store
    	// new podcast directory besides default.
    	return Config.getConfig().getPodcastFolder() + name;
        //return Config.getConfig().getPodcastFolder() + name;
    }

    void updateInfo(Collection<INewElementListener> newElementListeners) {
        new Updater(this, newElementListeners).start();
    }

    /*
     *Inner class to start the update in a new thread
     */
    private class Updater extends Thread {

        Collection<INewElementListener> newElementListeners;
        Podcast podcast;

        public Updater(Podcast podcast, Collection<INewElementListener> newElementListeners) {
            this.newElementListeners = newElementListeners;
            this.podcast = podcast;
        }

        @Override
        public void run() {
            try {
                parser.parseDocument();
                channelInfo = parser.getChannel();
                downloadQueue.clear();
                
                for (Item item : parser.getItems()) {
                    if (items.containsKey(item.getIdentifier()) == false) {
                        downloadQueue.add(item);
                    }
                }
                if (downloadQueue.isEmpty() == false) {
                    for (INewElementListener newElementListener : newElementListeners) {
                        newElementListener.newElementFound(podcast);
                    }
                }
            } catch (IOException ex) {
                for (INetworkErrorListener networkErrorListener : networkErrorListeners) {
                    networkErrorListener.networkErrorDetected(ex);
                }
            }
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        parser = new XMLParser(podcastURL);
    }

    public Collection<Item> getDownloadQueue() {
        return downloadQueue;
    }

    public void markAsDownloaded(Item item) {
        items.put(item.getIdentifier(), item);
        downloadQueue.remove(item);
    }

    public Channel getChannelInfo() {
        return channelInfo;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void downloadCompleted(Item item) {
        items.put(item.getIdentifier(), item);
        downloadQueue.remove(item);
    }
}