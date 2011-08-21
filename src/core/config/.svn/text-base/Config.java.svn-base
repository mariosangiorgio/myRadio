package core.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Config {
    static Config config;

    private String saveFile;
    private String podcastFolder;
    private long autoUpdateInterval;

    private Config() {
        //Setting default
        saveFile = "myRadio.data";
        podcastFolder = "downloadedItems/";
        autoUpdateInterval = 60;//Setting the scheduer to check every hour.
        load();
    }
    
    public static Config getConfig(){
        if(config == null){
            config = new Config();
            return config;
        }
        else{
            return config;
        }
    }

    /**
     * Setting directory location where podcasts should be saved.
     * @param folder name of directory.
     */
    public void setPodcastFolder(String folder) {
    	podcastFolder = folder + "/";
    }
    
    private void load()  {
   		try {
			BufferedReader input =  new BufferedReader(new FileReader("config.prefs"));
			saveFile = input.readLine();
			podcastFolder = input.readLine();
			autoUpdateInterval = Long.parseLong(input.readLine());
			input.close();			
		} catch (FileNotFoundException e) {
			System.err.println("Preference file not found.");
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Resetting configuration to defaults before returning from load()
	        saveFile = "myRadio.data";
	        podcastFolder = "downloadedItems/";
	        autoUpdateInterval = 60;
			return;
		} catch (IOException e) {
			System.out.println("Error reading input.");
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Resetting configuration to defaults before returning from load()
	        saveFile = "myRadio.data";
	        podcastFolder = "downloadedItems/";
	        autoUpdateInterval = 60;
			return;
		}
		// Creates podcast folder if it doesn't exist.
		// File folder = new File(podcastFolder);
		// folder.mkdir();
    }

    public String getPodcastFolder() {
        return podcastFolder;
    }

    public String getSaveFile() {
        return saveFile;
    }

    public long getAutoUpdateInterval() {
        return autoUpdateInterval;
    } 
    
    public void saveConfig() throws IOException {
    	Writer output = new BufferedWriter(new FileWriter("config.prefs"));
    	output.write(saveFile + "\n");
    	output.write(podcastFolder + "\n");
    	output.write(((Long)autoUpdateInterval).toString());
    	output.close();
    }
    
    @Override
    public String toString() {
    	String result = "";
    	result += "Contents of configuration file: " + "\n";
    	result += "saveFile: " + getSaveFile() + "\n";
    	result += "podcastFolder: " + getPodcastFolder() + "\n";
    	result += "autoUpdateInterval: " + getAutoUpdateInterval() + "\n";
    	return result;
    }
    
/*    public static void main(String[] args) {
    	Config testConfig = Config.getConfig();
    	System.out.println(testConfig);
    	testConfig.setPodcastFolder("/home/holing/downloadedItems");
    	try {
			testConfig.saveConfig();
		} catch (IOException e) {
			System.err.println("Error saving configuration.");
			e.printStackTrace();
			return;
		}
    }*/
}
