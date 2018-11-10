package Application.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver implements ISaver {

    private final String path;

    public FileSaver(String path) {
        this.path = path;
    }

    @Override
    public boolean save(String document, String name) {

        String[] tokens = name.split("[/.:]");
        StringBuilder builder = new StringBuilder(path);

        if (tokens.length > 1) {
            builder.append('/');
            builder.append(tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                if (tokens[i].length() > 0) {
                    builder.append('_');
                    builder.append(tokens[i]);
                }
            }
        }
        else {
            builder.append('/');
            builder.append(tokens[0]);
        }

        builder.append(".html");

        try {
            File file = new File(builder.toString());
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(document);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
}
