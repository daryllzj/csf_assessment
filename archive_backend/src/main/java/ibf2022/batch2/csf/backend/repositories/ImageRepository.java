package ibf2022.batch2.csf.backend.repositories;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ImageRepository {

	@Autowired
    private AmazonS3 s3Client;

    @Value("${DO_STORAGE_BUCKETNAME}")
    private String bucketName;

	@Value("${DO_STORAGE_ENDPOINT}")
	private String endPoint; 

	//TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public String upload(File file, String name, String key) {

		log.info("fileName> " + file.getName());
		log.info("filePath> " + file.getAbsolutePath());

		String namekey = UUID.randomUUID().toString().substring(0, 8);
    	log.info("namekey> " + namekey);

		Map<String, String> userData = new HashMap<>();
        userData.put("name", name);
		userData.put("key", key);
        userData.put("filename", file.getName());

		FileNameMap fileNameMap = URLConnection.getFileNameMap();
    	String mimeType = fileNameMap.getContentTypeFor(file.getName());

		log.info("fileType> " + mimeType);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(mimeType);
        metadata.setContentLength(file.length());
        metadata.setUserMetadata(userData);

		PutObjectRequest putRequest = new PutObjectRequest( bucketName, "myobject%s".formatted(namekey), file);
        putRequest = putRequest.withCannedAcl(CannedAccessControlList.PublicRead);

		putRequest.setMetadata(metadata);

        s3Client.putObject(putRequest);

        log.info("file uploaded");

		String imageUrl = "https://"+ bucketName+"."+endPoint+"/"+ "myobject%s".formatted(namekey);

       log.info(imageUrl);

		return imageUrl;
	}

	
}
