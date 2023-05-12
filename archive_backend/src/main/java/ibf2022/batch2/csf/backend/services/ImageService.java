package ibf2022.batch2.csf.backend.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.repositories.ImageRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public List<String> unzipFile(MultipartFile file, String name, String key) throws IOException {

        byte[] buffer = new byte[1024];
        InputStream inputStream = file.getInputStream();
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();

        List<String> urls = new LinkedList<>();

        while (zipEntry !=null) {

            String fileName = zipEntry.getName();
            log.info(fileName);
            File newFile = new File (fileName);

            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            log.info("newFileName> " + newFile.getName());

            String url = imageRepository.upload(newFile, name, key);
            urls.add(url);

            fos.close();
            zipEntry = zis.getNextEntry();
        }

        zis.close();
        inputStream.close();

        return urls;
    }
    
}
