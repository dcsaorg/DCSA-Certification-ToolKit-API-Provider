package org.dcsa.api.validator.util;

import lombok.extern.java.Log;
import org.springframework.core.io.ByteArrayResource;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

@Log
public class FileUtility {
    public static InputStream getInputStream(String resource){
        InputStream inputStream = null;
        try{
            Path filePath = Path.of(resource);
            inputStream = Files.newInputStream(filePath.toAbsolutePath());
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return inputStream;
    }

    public static String loadResourceAsString(String resource) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return "";
    }

        public static String loadFileAsString(String resource) {
            return parseResourceWithStream(resource, inputStream -> {
                Reader dataInputStream = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                char[] buffer = new char[4096];
                while (dataInputStream.read(buffer) >= 1) {
                    stringBuilder.append(buffer);
                }
                return stringBuilder.toString().trim();
            });
        }
        private static <T> T parseResourceWithStream(String classpath, ParserFunction<InputStream, T> reader) {
            InputStream inputStream = null;
            try {
                inputStream = openStream(classpath);
                return reader.apply(inputStream);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                closeStream(inputStream);
            }
        }
        private static void closeStream(InputStream inputStream) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }

        private static InputStream openStream(String resource) throws IOException {
            URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
            if (url == null) {
                try {
                    File file = new File(resource);
                    FileInputStream fis = new FileInputStream(file);
                    return fis;
                } catch (Exception e)
                {
                    throw new IllegalStateException("Cannot find json file " + resource);
                }
            }
            return url.openStream();
        }

        private interface ParserFunction<T, R> {
            R apply(T t) throws Exception;
        }

    public static ByteArrayResource getFile(String resource) throws IOException {
        try {
            File file = new File(resource);
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
            return byteArrayResource;
        } catch (Exception e)
        {
            throw new IllegalStateException("Cannot find file " + resource);
        }
    }
    public static String winPathToUnixPth(String path) {
        return path.indexOf('\\') < 0 ? path : path.replace('\\', '/');
    }

    public static String getTestSuitePath(String resourceName){
        String suiteDir = "suitexmls";
        try {
            String localPath = new File(".").getCanonicalPath();
            return localPath+File.separator+suiteDir+File.separator+resourceName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
