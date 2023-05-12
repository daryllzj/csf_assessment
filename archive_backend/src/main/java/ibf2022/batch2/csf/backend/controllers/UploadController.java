package ibf2022.batch2.csf.backend.controllers;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.services.ImageService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping
public class UploadController {


	@Autowired
	ImageService imageService;

	@Autowired
	ArchiveRepository archiveRepository;

	// TODO: Task 2, Task 3, Task 4
	@PostMapping(path="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> uploadDocument(@RequestPart MultipartFile file, @RequestPart String name, @RequestPart String fileName, @RequestPart String title, @RequestPart String comments) throws IOException {
	
	log.info("connected to server");
	log.info("name> " + name);
	log.info("file> " + file);
	log.info("fileName> " + fileName);
	log.info("comments> " + comments);
	log.info("title> " + title);

	String key = UUID.randomUUID().toString().substring(0, 8);
    log.info("key> " + key);
	
	List<String> urls = imageService.unzipFile(file, name, key);

	//try catch for updating archive

	try {
		String bundleId = archiveRepository.recordBundle(key, name, comments, urls, title);

		log.info("bundleId> " + bundleId);

		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("bundleId", bundleId);
		JsonObject jsonObject = jsonObjectBuilder.build();

		return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
		
	} catch (Exception e) {

		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("error", "unable to upload document");
		JsonObject jsonObject = jsonObjectBuilder.build();

		return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
		
	}
	}
	

	// TODO: Task 5
	@GetMapping (path = "/bundle/{bundleId}")
	public ResponseEntity<String> getBundbleByBundleId(@PathVariable String bundleId) {

		log.info("bundleId> " + bundleId);

		try {
			JsonObject json = archiveRepository.getBundleByBundleId(bundleId);

			return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(json.toString());
			
		} catch (Exception e) {
			JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
			jsonObjectBuilder.add("error", "unable to get bundle");
			JsonObject jsonObject = jsonObjectBuilder.build();

			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
		}
	}
	

	// TODO: Task 6
	@GetMapping (path = "/bundles")
	public ResponseEntity<String> getBundles() {

		log.info("gettingBundles> ");

		try {
			List<JsonObject> bundles = archiveRepository.getBundles();

			return ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(bundles.toString());
			
		} catch (Exception e) {
			JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
			jsonObjectBuilder.add("error", "unable to retrieve bundles");
			JsonObject jsonObject = jsonObjectBuilder.build();

			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
		}
	}
}
