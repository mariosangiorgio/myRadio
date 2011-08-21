package core.interfaces.listeners;

/**
 * Classes that wants to be warned of network problems must implement this
 * interface and be registered to the Core.
 * @author Mario
 */
public interface INetworkErrorListener {
    /**
     * The method that must handle network error.
     * @param exception The exception raised to know what the error exactly was.
     */
    public void networkErrorDetected(Exception exception);
}
