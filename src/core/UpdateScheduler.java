package core;

public class UpdateScheduler extends Thread {

    private long updateTime;//In milliseconds. Conversion performed in the constructor
    private Core core;

    /**
     * Creates a scheduler that updates podcast information.
     * @param core The core that has to check for updates.
     * @param updateTime The time between updates. Value has to be expressed in
     * minutes.
     */
    public UpdateScheduler(Core core,long updateTime) {
        //Changing the representation from minutes to milliseconds
        this.updateTime = updateTime*60000;
        this.core = core;
    }

    @Override
    public void run() {
        boolean interrupted = false;
        while(interrupted == false){
            try {
                sleep(updateTime);
                core.updateAllPodcasts();
            } catch (InterruptedException ex) {
                interrupted = true;
            }
        }        
    }
    
}
