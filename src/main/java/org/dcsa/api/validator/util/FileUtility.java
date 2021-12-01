package org.dcsa.api.validator.util;

import java.io.*;
import java.net.URL;

public class FileUtility {

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
}
