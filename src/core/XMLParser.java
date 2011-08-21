package core;

import data.Channel;
import data.Item;
import data.SubItems.Enclosure;
import data.iTunesEnum.iTunesExplicit;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A XML xerces-based parser used to extract data form RSS feeds.
 * The parsing style used is SAX.
 * @author Mario
 */
public class XMLParser extends DefaultHandler {

    private URL feedAddress;
    //Storage
    private Channel channel;
    private Collection<Item> items;
    //Helpers
    private SimpleDateFormat dateParser;
    //Temporary parsing values
    private String readValue;
    private boolean parsingItem;
    private boolean parsingChannel;
    //Temporary ITEM values
    private String itemTitle;
    private Date itemPubDate;
    private Enclosure itemEnclosure;
    private String itemGuid;
    private String itemiTunesAuthor;
    private boolean itemiTunesBlock;
    private String itemiTunesDuration;
    private iTunesExplicit itemiTunesExplicit;
    private String itemiTunesKeywords;
    private String itemiTunesSubtitle;
    private String itemiTunesSummary;
    private String itemDescription;
    //Temporary CHANNEL value
    private String channelTitle;
    private URL channelLink;
    private String channelCopyright;
    private String channeliTunesAuthor;
    private boolean channeliTunesBlock;
    private URL channeliTunesImage;
    private iTunesExplicit channeliTunesExplicit;
    private URL channeliTunesNewFeedURL;
    private String channeliTunesSubtitle;
    private String channeliTunesSummary;
    private String channelLanguage;
    private String channelDescription;

    public XMLParser(URL feedAddress) {
        this.feedAddress = feedAddress;
        parsingItem = false;
        items = new Vector<Item>();
        dateParser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    }

    public Channel getChannel() {
        return channel;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public void parseDocument() throws IOException {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(feedAddress.toString(), this);
        } catch (SAXException ex) {
            myRadioLogger.getLogger().log(Level.SEVERE, "Error parsing "+feedAddress, ex);
        } catch (ParserConfigurationException ex) {
            myRadioLogger.getLogger().log(Level.SEVERE,"Error configuring the parser",ex);
        }
    }

    //SAX Event handlers
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        readValue = new String(ch, start, length);
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        if (parsingItem == false) {
            //Parsing ITEM element
            if (qName.equalsIgnoreCase("item")) {
                //Setting the I'm reading an item
                parsingItem = true;
                //Resetting all the old values
                itemEnclosure = null;
                itemGuid = null;
                itemPubDate = null;
                itemTitle = null;
                itemiTunesAuthor = null;
                itemiTunesBlock = false;
                itemiTunesDuration = null;
                itemiTunesExplicit = itemiTunesExplicit.no;
                itemiTunesKeywords = null;
                itemiTunesSubtitle = null;
                itemiTunesSummary = null;
                itemDescription = null;
                return;
            }
            //Parsing CHANNEL
            if (qName.equalsIgnoreCase("channel")) {
                //Setting the I'm reading an item
                parsingChannel = true;
                //Resetting all the old values
                channelCopyright = null;
                channelLink = null;
                channelTitle = null;
                channeliTunesAuthor = null;
                channeliTunesBlock = false;
                channeliTunesExplicit = itemiTunesExplicit.no;
                channeliTunesImage = null;
                channeliTunesNewFeedURL = null;
                channeliTunesSubtitle = null;
                channeliTunesSummary = null;
                channelDescription = null;
                channelLanguage = null;
                items = new Vector<Item>();
                return;
            }
        }
        if (parsingItem) {
            //Parsing item sub-elements
            if (qName.equalsIgnoreCase("enclosure")) {
                try {
                    //Creating the enclosure for this item
                    itemEnclosure = new Enclosure(new URL(atts.getValue("url")), Integer.parseInt(purify(atts.getValue("length"))), atts.getValue("type"));
                } catch (MalformedURLException ex) {
                    myRadioLogger.getLogger().log(Level.SEVERE,"Can't parse enclosure link "+atts.getValue("url"),ex);
                }
            }
        }
        if (parsingChannel) {
            if (qName.equalsIgnoreCase("itunes:image")) {
                try {
                    channeliTunesImage = new URL(atts.getValue("href"));
                    return;
                } catch (MalformedURLException ex) {
                    myRadioLogger.getLogger().log(Level.SEVERE, "Error parsing "+atts.getValue("href"), ex);
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (parsingItem) {
            if (qName.equalsIgnoreCase("item")) {
                //Adding item to the collection
                items.add(new Item(itemTitle, itemPubDate, itemEnclosure, itemGuid,
                        itemiTunesAuthor, itemiTunesBlock, itemiTunesDuration, itemiTunesExplicit,
                        itemiTunesKeywords, itemiTunesSubtitle, itemiTunesSummary,itemDescription));
                parsingItem = false;
                return;
            }
            //I'm parisng an item
            if (qName.equalsIgnoreCase("title")) {
                itemTitle = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("description")) {
                itemDescription = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:author")) {
                itemiTunesAuthor = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:subtitle")) {
                itemiTunesSubtitle = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:summary")) {
                itemiTunesSummary = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("guid")) {
                itemGuid = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("pubDate")) {
                try {
                    itemPubDate = dateParser.parse(readValue);
                    return;
                } catch (ParseException ex) {
                    myRadioLogger.getLogger().log(Level.SEVERE, "Error parsing pubDate "+readValue, ex);
                }
            }
            if (qName.equalsIgnoreCase("itunes:duration")) {
                itemiTunesDuration = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:keywords")) {
                itemiTunesKeywords = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:explicit")) {
                itemiTunesExplicit = iTunesExplicit.valueOf(readValue);
                return;
            }
            if (qName.equalsIgnoreCase("itunes:block") && readValue.equalsIgnoreCase("Yes")) {
                itemiTunesBlock = true;
                return;
            }
        }
        if (parsingChannel) {
            if (qName.equalsIgnoreCase("Channel")) {
                channel = new Channel(channelTitle, channelLink, channelLanguage,
                        channelCopyright, channelDescription, channeliTunesAuthor,
                        channeliTunesBlock, channeliTunesImage, channeliTunesExplicit,
                        channeliTunesNewFeedURL, channeliTunesSubtitle, channeliTunesSummary);
                parsingChannel = false;
                return;
            }
            if (qName.equalsIgnoreCase("title")) {
                channelTitle = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("link")) {
                try {
                    channelLink = new URL(readValue);
                    return;
                } catch (MalformedURLException ex) {
                    myRadioLogger.getLogger().log(Level.SEVERE, "Error parsing 'link' "+readValue, ex);
                }
            }
            if (qName.equalsIgnoreCase("language")) {
                channelLanguage = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("copyright")) {
                channelCopyright = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:subtitle")) {
                channeliTunesSubtitle = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:author")) {
                channeliTunesAuthor = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:summary")) {
                channeliTunesSummary = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("description")) {
                channelDescription = readValue;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:explicit")) {
                channeliTunesExplicit = iTunesExplicit.valueOf(readValue.toLowerCase());
                return;
            }
            if (qName.equalsIgnoreCase("itunes:block") && readValue.equalsIgnoreCase("Yes")) {
                channeliTunesBlock = true;
                return;
            }
            if (qName.equalsIgnoreCase("itunes:new-feed-url")) {
                try {
                    channeliTunesNewFeedURL = new URL(readValue);
                    return;
                } catch (MalformedURLException ex) {
                    myRadioLogger.getLogger().log(Level.SEVERE, "Error retreiving feed url from "+feedAddress, ex);
                }
            }
        }

    }

    private String purify(String string) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c == '0' || c == '1' ||
                    c == '2' || c == '3' ||
                    c == '4' || c == '5' ||
                    c == '6' || c == '7' ||
                    c == '8' || c == '9') {
                s.append(c);
            }
        }
    return s.toString ();
    }
}
