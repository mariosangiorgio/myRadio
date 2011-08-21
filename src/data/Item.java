package data;

import data.SubItems.Enclosure;
import data.iTunesEnum.iTunesExplicit;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Class to represent a feed item according to podcast specification.
 * http://www.apple.com/itunes/store/podcaststechspecs.html
 * @author Mario
 */
public class Item implements Serializable {

    private static final long serialVersionUID = 7526472295622776149L;
    //<title>
    private String title;
    //<pubDate>
    private Date pubDate;
    //<enclosure>
    private Enclosure enclosure;
    //<guid>
    private String guid;
    //iTunes tags
    //<itunes:author>
    private String iTunesAuthor;
    //<itunes:block>    prevent an episode or podcast from appearing
    private boolean iTunesBlock;
    //<itunes:duration>
    private String iTunesDuration;
    //<itunes:explicit> parental advisory
    private iTunesExplicit iTunesExplicit;
    //<itunes:keywords>
    private String iTunesKeywords;
    //<itunes:subtitle>
    private String iTunesSubtitle;
    //<itunes:summary>
    private String iTunesSummary;
    //<duration>
    private String description;

    public Item(String title, Date pubDate, Enclosure enclosure, String guid,
            String iTunesAuthor, boolean iTunesBlock, String iTunesDuration,
            iTunesExplicit iTunesExplicit, String iTunesKeywords,
            String iTunesSubtitle, String iTunesSummary,String description) {
        this.enclosure = enclosure;
        this.guid = guid;
        this.iTunesAuthor = iTunesAuthor;
        this.iTunesBlock = iTunesBlock;
        this.iTunesDuration = iTunesDuration;
        this.iTunesExplicit = iTunesExplicit;
        this.iTunesKeywords = iTunesKeywords;
        this.iTunesSubtitle = iTunesSubtitle;
        this.iTunesSummary = iTunesSummary;
        this.description = description;
        this.pubDate = pubDate;
        this.title = title;
    }

    public String getFileExtension() {
        String fileName = enclosure.getFilename();
        int lastDot = fileName.lastIndexOf('.');
        return fileName.substring(lastDot);
    }
    
    public String getTitle() {
        return title;
    }

    public String getIdentifier() {
        if (guid != null) {
            return guid;
        } else {
            return enclosure.getURL().toString();
        }
    }

    public int getSize() {
        return enclosure.getLength();
    }

    @Override
    public String toString() {
        //Usgin RegExp to find if the string is empty or composed only by spaces
        
        if(iTunesSummary!=null && Pattern.matches("\\s*", iTunesSummary)==false){
            return title+" ("+iTunesSummary+")";
        }
        if(description!=null && Pattern.matches("\\s*", description)==false){
            return title+" ("+description+")";
        }
        return title;
        
    }

    public URL getURL() {
        return enclosure.getURL();
    }
}