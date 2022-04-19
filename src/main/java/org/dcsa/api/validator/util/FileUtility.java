package org.dcsa.api.validator.util;

import lombok.extern.java.Log;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

@Log
public class FileUtility {

        public static String loadFileAsString(String resource) {
            try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(resource)) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
            return "";
        }

    public static ByteArrayResource getFile(String resource) throws IOException {
        ByteArrayResource byteArrayResource;
        try {
            File file = new File(resource);
            Path path = Paths.get(file.getAbsolutePath());
            byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (Exception e)
        {
            throw new IllegalStateException("Cannot find file " + resource);
        }
        return  byteArrayResource;
    }
}
