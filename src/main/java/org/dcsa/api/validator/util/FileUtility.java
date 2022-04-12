package org.dcsa.api.validator.util;

import lombok.extern.java.Log;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

@Log
public class FileUtility {

        public static String loadFileAsString(String resource) {
            try {
                URL url =  FileUtility.class.getClassLoader().getResource(resource);
                if(url != null){
                    return new String(Files.readAllBytes(Paths.get(url.toURI())));
                }
            }catch (IOException | URISyntaxException e) {
                log.log(Level.WARNING, "Resource not found");
            }
            return "";
        }

    public static ByteArrayResource getFile(String resource) throws IOException {
        try {
            File file = new File(resource);
            Path path = Paths.get(file.getAbsolutePath());
            return new ByteArrayResource(Files.readAllBytes(path));
        } catch (Exception e)
        {
            throw new IllegalStateException("Cannot find file " + resource);
        }
    }
}
