package org.dcsa.api.validator.util;

import lombok.extern.java.Log;
import org.springframework.core.io.ByteArrayResource;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Log
public class FileUtility {

    private static final String SCRIPT_DIR = "postman-collection";

    private static final String NEWMAN_DIR = "newman";

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

    public static String getScriptPath(String resourceName){
        try {
            String localPath = new File(".").getCanonicalPath();
            return localPath+File.separator+SCRIPT_DIR+File.separator+resourceName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeFile(BufferedReader bufferedReader) {
        File testResultFile  = new File("testResultFile.log");
        BufferedWriter bw = null;
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (bw==null) {
                    bw=new BufferedWriter(new FileWriter(testResultFile));
                }
                bw.write(line+System.getProperty("line.separator"));
            }
            if (bw!=null) {
                bw.close();
            }
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
        return testResultFile.getAbsolutePath();
    }

    public static boolean isTntCollection(String filePath){
        boolean isTntCollection = false;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
                if(line.toLowerCase().contains("tnt")){
                    isTntCollection = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return isTntCollection;
    }

    public static String getNewmanReport(String reportKeyword){
        String newmanReportDir = SCRIPT_DIR+File.separator+NEWMAN_DIR;
        try {
            String localPath = new File(".").getCanonicalPath();
            File folder = new File(localPath+File.separator+newmanReportDir);
            File[] listOfFiles = folder.listFiles();
            List<File> fileList = Arrays.stream(Objects.requireNonNull(listOfFiles))
                                        .filter(file -> file.getAbsolutePath().toLowerCase().contains(reportKeyword.toLowerCase()))
                                        .collect(Collectors.toList());
            Objects.requireNonNull(fileList).sort(Comparator.comparingLong(File::lastModified).reversed());

            return fileList.stream().findFirst().map(File::getAbsolutePath).orElse("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPostmanCollectionName(String fileName){
        try {
            String appDir = new File(".").getCanonicalPath();
            List<String> filenames = Files.list(Paths.get(appDir + File.separator + SCRIPT_DIR))
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toAbsolutePath().toString().toLowerCase().contains(fileName.toLowerCase()))
                    .map(file -> file.getFileName().toString()).toList();
            if(filenames.size() > 0){
                return filenames.get(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

}
