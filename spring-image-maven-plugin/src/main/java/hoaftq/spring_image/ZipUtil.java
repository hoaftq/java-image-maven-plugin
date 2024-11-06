package hoaftq.spring_image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    public static void unzip(String filePath, String outputFilePath) throws IOException {
        createIfNotExist(outputFilePath);

        try (var zipInputStream = new ZipInputStream(new FileInputStream((filePath)))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }

                var unzipedFile = new File(outputFilePath, zipEntry.getName());
                unzipedFile.getParentFile().mkdirs();
                writeToFile(zipInputStream, unzipedFile);

                zipInputStream.closeEntry();
            }
        }
    }

    private static void createIfNotExist(String outputFilePath) {
        var file = new File(outputFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void writeToFile(ZipInputStream zipInputStream, File unzipedFile) throws IOException {
        try (var outputFileStream = new FileOutputStream(unzipedFile)) {
            byte[] buffer;
            do {
                buffer = zipInputStream.readNBytes(1024);
                outputFileStream.write(buffer);
            } while (buffer.length > 0);
        }
    }
}
