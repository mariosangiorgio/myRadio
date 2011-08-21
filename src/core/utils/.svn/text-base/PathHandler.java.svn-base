package core.utils;

import java.io.File;
import java.util.Date;

public class PathHandler {

    static public String getValidPath(String path) {
        return avoidNameCollision(removeForbiddenCharachters(path));
    }

    static public String avoidNameCollision(String path) {
        File file = new File(path);
        if (file.exists()) {
            Date date = new Date();
            path = path + date.getTime();
        }
        return path;
    }

    static public String removeForbiddenCharachters(String path) {
        StringBuilder purfiedStringBuilder = new StringBuilder();
        for (char c : path.toCharArray()) {
            if (!(c == ':' ||
                    c == '/' ||
                    c == '\\' ||
                    c == '*' ||
                    c == '?' ||
                    c == '|' ||
                    c == '"' ||
                    c == '>' ||
                    c == '<')) {
                purfiedStringBuilder.append(c);
            }
        }
        return purfiedStringBuilder.toString();
    }
}
