/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package data.SubItems;

import java.io.Serializable;
import java.net.URL;

/**
 *
 * @author Mario
 */
public class Enclosure implements Serializable{
    private URL url;
    private int length;//Filesize in bytes
    private String type;
    
    public Enclosure(URL url,int length, String type){
        this.url =url;
        this.length = length;
        this.type = type;
    }

    public String getFilename() {
        return url.getFile().substring(url.getFile().lastIndexOf('/'));
    }

    public int getLength() {
        return length;
    }

    public URL getURL() {
        return url;
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("URL:\t");
        builder.append(url.toString());
        builder.append("\nLength:\t");
        builder.append(length);
        builder.append("\nType:\t");
        builder.append(type);
        return builder.toString();
    }

}
