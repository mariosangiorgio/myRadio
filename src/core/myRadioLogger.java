package core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class myRadioLogger {
    static private Logger logger;
    
    static public Logger getLogger(){
        if(logger==null){
            try {
                logger = Logger.getLogger("myRadio");
                FileHandler fileHandler = new FileHandler("log.txt", true);
                logger.addHandler(fileHandler);
                logger.setLevel(Level.ALL);
                SimpleFormatter simpleFormatter = new SimpleFormatter();
                fileHandler.setFormatter(simpleFormatter);
            } catch (IOException ex) {
                Logger.getLogger(myRadioLogger.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(myRadioLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return logger;
    }    
}
