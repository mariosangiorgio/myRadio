package core;

import core.config.Config;
import core.exceptions.NameAlreadyInUseException;
import core.interfaces.ICoreToUser;
import core.interfaces.listeners.INewElementListener;
import data.Item;
import core.interfaces.listeners.INetworkErrorListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

public class Core implements ICoreToUser, Serializable {

    private static final long serialVersionUID = 7526472295622776147L;
    private Map<String, Podcast> podcasts;
    private transient UpdateScheduler updateScheduler;
    private transient Collection<INewElementListener> newElementListeners;
    private transient Collection<INetworkErrorListener> networkErrorListeners;

    private Core(Config config) {
        podcasts = new HashMap<String, Podcast>();
        updateScheduler = new UpdateScheduler(this, config.getAutoUpdateInterval());
        updateScheduler.start();
        newElementListeners = new Vector<INewElementListener>();
        networkErrorListeners = new Vector<INetworkErrorListener>();
        setSystemProxy();
    }

    @Override
    public Map<String, Podcast> getPodcasts() {
        return podcasts;
    }

    @Override
    public void subscribe(String name, URL url) throws NameAlreadyInUseException {
        if (podcasts.containsKey(name)) {
            throw new NameAlreadyInUseException();
        } else {
            podcasts.put(name, new Podcast(url, name, networkErrorListeners));
        }
    }

    @Override
    public void unsubscribe(String name) {
        Podcast podcast = podcasts.remove(name);
        podcast.clear();
    }

    public static Core load() {
        //Loading config
        Config config = Config.getConfig();

        //Deserializing object
        Core core = null;
        FileInputStream input = null;
        ObjectInputStream serializedObject = null;
        try {
            input = new FileInputStream(config.getSaveFile());
            serializedObject = new ObjectInputStream(input);
            core = (Core) serializedObject.readObject();
        } catch (IOException ex) {
            //Can't read the file, I provide a new instance of core
            core = new Core(config);
            myRadioLogger.getLogger().log(Level.SEVERE, "Error in deserializig core", ex);
        } catch (ClassNotFoundException ex) {
            myRadioLogger.getLogger().log(Level.SEVERE, "Error in deserializig core", ex);
        } finally {
            try {
                if (input != null) {
                    input.close();
                    serializedObject.close();
                }
            } catch (IOException ex) {
                myRadioLogger.getLogger().log(Level.SEVERE, "Error in deserializig core", ex);
            }
        }
        return core;
    }

    @Override
    public void save() {
        //Stopping update scheduer to have a clean shutdown and prevent data corruption
        updateScheduler.interrupt();
        //Saving data to file
        FileOutputStream output = null;
        {
            ObjectOutputStream serializedObject = null;
            try {
                output = new FileOutputStream(Config.getConfig().getSaveFile());
                serializedObject = new ObjectOutputStream(output);
                serializedObject.writeObject(this);
            } catch (IOException ex) {
                myRadioLogger.getLogger().log(Level.SEVERE, "Error in serializing core", ex);
            } finally {
                try {
                    output.close();
                } catch (IOException ex) {
                    myRadioLogger.getLogger().log(Level.SEVERE, "Error in serializing core", ex);
                }
                try {
                    serializedObject.close();
                } catch (IOException ex) {
                    myRadioLogger.getLogger().log(Level.SEVERE, "Error in serializing core", ex);
                }
            }
        }
    }

    @Override
    public Downloader download(Podcast podcast, Item item) {
        Downloader downloader = new Downloader(item, podcast.getBasePath(), networkErrorListeners);
        //Adding listener of the podcast to put the item form the available list
        //to the downloaded list
        downloader.addListener(podcast);
        //Creating the downloader thread
        downloader.start();
        return downloader;
    }

    @Override
    public void updateAllPodcasts() {
        for (Podcast podcast : podcasts.values()) {
            updatePodcast(podcast);
        }
    }

    /*
     * Deserialization method override.
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        updateScheduler = new UpdateScheduler(this, Config.getConfig().getAutoUpdateInterval());
        updateScheduler.start();
        newElementListeners = new Vector<INewElementListener>();
        networkErrorListeners = new Vector<INetworkErrorListener>();
        /*
         * Setting on all the podcast the error listener to work around the
         * impossibility of throwing an exception from the start() method of a thread
         */
        for (Podcast podcast : podcasts.values()) {
            podcast.setNetworkErrorListeners(networkErrorListeners);
        }
        setSystemProxy();
    }

    private void setSystemProxy() {
        System.setProperty("java.net.useSystemProxies", "true");
    }

    @Override
    public void registerNewElementListener(INewElementListener newElementListener) {
        newElementListeners.add(newElementListener);
    }

    @Override
    public void updatePodcast(Podcast podcast) {
        podcast.updateInfo(newElementListeners);
    }

    @Override
    public void registerNetworkErrorListener(INetworkErrorListener networkErrorListener) {
        networkErrorListeners.add(networkErrorListener);
    }

    @Override
    public void markAsDownloaded(Podcast podcast, Item item) {
        podcast.markAsDownloaded(item);
    }

	@Override
	public void setDownloadFolder(String folder) {
		Config config = Config.getConfig();
		config.setPodcastFolder(folder);
		try {
			config.saveConfig();
		} catch (IOException e) {
			System.err.println("Cannot write to preferences file.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}