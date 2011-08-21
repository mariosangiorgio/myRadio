package core.interfaces;

import core.*;
import core.interfaces.listeners.INewElementListener;
import data.Item;
import core.Podcast;
import core.exceptions.NameAlreadyInUseException;
import core.interfaces.listeners.INetworkErrorListener;
import java.net.URL;
import java.util.Map;

/**
 * Interface used to expose Core methods to user interface
 * @author Mario
 */
public interface ICoreToUser {

    /**
     * Method that downloads the selected item of the selected podcast. You
     * may also add the listener to the UI's objects you want to be notified of
     * the end of the download.
     * @param podcast The podcast that contains the item
     * @param item The item we want to download
     * @return The instance of the downloader. The thread is yet started, use it
     * only to get the file size and the downloaded size.
     */
    public Downloader download(Podcast podcast, Item item);

    /**
     * Method to get the subscribed podcast list.
     * @return The subscribed podcast list in the form &ltName,Podcast&gt
     */
    public Map<String, Podcast> getPodcasts();

    /**
     * Subscribing to a podcast setting its name and its URL
     * @param name The name of the podacast
     * @param url The URL of the podcast
     * @throws core.exceptions.NameAlreadyInUseException If the name selected
     * for the podcast is already in use.
     */
    public void subscribe(String name, URL url) throws NameAlreadyInUseException;

    /**
     * Drops the subscription to the Podcast named as the variable.
     * @param name The podcast to drop.
     */
    public void unsubscribe(String name);

    /**
     * Saves the podcast list and the lists of downloaded and available items
     * to a file.
     * The default file is myRadio.data
     */
    public void save();

    /**
     * Updates all the podcast information downloading fresh feeds.
     */
    public void updateAllPodcasts();

    /**
     * Updates the selected podcast.
     * @param podcast The podcast to update.
     */
    public void updatePodcast(Podcast podcast);

    /**
     * Registers a listener fot the "new item found" event.
     * @param newElementListener The object that wants to listen for new elements.
     */
    public void registerNewElementListener(INewElementListener newElementListener);

    /**
     * Registers a listener for the "network error" event.
     * @param networkErrorListener The object that wants to listen for a network error.
     */
    public void registerNetworkErrorListener(INetworkErrorListener networkErrorListener);
    
    /**
     * Marks an item as downloaded. Use it to remove a file for the available list
     * without having to download it.
     * @param podcast The podcast that contains the item
     * @param item The item to mark as downloaded
     */
    public void markAsDownloaded(Podcast podcast,Item item);
    
    /**
     * Sets directory location of where podcasts should be downloaded.
     * Use it to set where podcasts should be downloaded.
     * @param folder
     */
    public void setDownloadFolder(String folder);
}
