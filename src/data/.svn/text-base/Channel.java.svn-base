package data;

import data.iTunesEnum.iTunesExplicit;
import java.io.Serializable;
import java.net.URL;

/**
 * Class to represent a Channel according to iTunes specification
 * http://www.apple.com/itunes/store/podcaststechspecs.html
 * @author Mario
 */
public class Channel implements Serializable{
    //<title>
    private String title;
    //<link>
    private URL link;
    //<copyright>
    private String copyright;
    //<itunes:author>
    private String iTunesAuthor;
    //<itunes:block>
    private boolean iTunesBlock;
    //<itunes:category>
    //Not supported
    //<itunes:image>
    private URL iTunesImage;//Read href attribute
    //<itunes:explicit>
    private iTunesExplicit iTunesExplicit;
    //<itunes:new-feed-url>
    private URL iTunesNewFeedURL;
    //<itunes:owner>
    //Not supported
    //<itunes:subtitle>
    private String iTunesSubtitle;
    //<itunes:summary>
    private String iTunesSummary;
    //<language>
    private String langauge;
    //<description>
     private String description;

    public Channel(String title, URL link, String language, String copyright, String description,
            String iTunesAuthor, boolean iTunesBlock, URL iTunesImage,
            iTunesExplicit iTunesExplicit, URL iTunesNewFeedURL,String iTunesSubtitle, String iTunesSummary) {
        this.title = title;
        this.link = link;
        this.langauge = language;
        this.copyright = copyright;
        this.iTunesAuthor = iTunesAuthor;
        this.iTunesBlock = iTunesBlock;
        this.iTunesImage = iTunesImage;
        this.iTunesExplicit = iTunesExplicit;
        this.iTunesNewFeedURL = iTunesNewFeedURL;
        this.iTunesSubtitle = iTunesSubtitle;
        this.iTunesSummary = iTunesSummary; 
        this.description = description;
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Title:\t");
        builder.append(title);
        builder.append("\nLink:\t");
        builder.append(link);
        builder.append("\nDescription:\t");
        builder.append(description);
        return  builder.toString();
    }
        
}
