package ibf2022.batch2.csf.backend.repositories;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ArchiveRepository {

	@Autowired
    private MongoTemplate mongoTemplate;

	private static final String ARCHIVES_COL = "archives";
	
	//TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method

	// db.archives.insert({
	// 	bundleId:"bundleId",
	// 	date: "date",
	// 	title:"title",
	// 	name: "name",
	// 	urls: ["url1", "url2"]
	// })

	public String recordBundle(String key, String name, String comments, List<String> urls, String title ) {

		log.info("key> " + key);
		log.info("name> " + name);
		log.info("comments> " + comments);
		log.info("title> " + title);
		log.info("urls> " + urls);

		Document document = new Document();
		document.append("bundleId", key);
		document.append("date", LocalDate.now().toString());
		document.append("title", title);
		document.append("name", name);
		document.append("comments", comments);
		document.append("url", urls);

		log.info("document> " + document.toJson());

		mongoTemplate.insert(document, ARCHIVES_COL);

		return key;
	}


	//TODO: Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name

	// Write the native mongo query that you will be using in this method
	// db.archives.find({bundleId: "bundleId"})

	public JsonObject getBundleByBundleId(String bundleId) {

		Criteria criteria = Criteria.where("bundleId").is(bundleId);

        Query query = Query.query(criteria);

        List<Document> list = mongoTemplate.find(query, Document.class, ARCHIVES_COL);

		Document document = list.get(0);

		document.remove("_id");

		log.info("retrieved document> " + document.toJson());

		JsonObject jsonObject = toJson(document);

		return jsonObject;

	}

	//TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name


	// Write the native mongo query that you will be using in this method
	// db.archives.find({},{_id:0, date:1, title:1}).sort({date:-1, title:1})
	
	public List<JsonObject> getBundles() {

        Query query = Query.query(Criteria.where("title").exists(true)).with(Sort.by(Sort.Direction.DESC,"date").by(Sort.Direction.ASC,"title"));

		query.fields().exclude("_id").include("date","title");

		List<JsonObject> list = new LinkedList<>();

		try {
			List<Document> listOfBundles = mongoTemplate.find(query, Document.class, ARCHIVES_COL);

			log.info("listOfBundles> " + listOfBundles.toString());

			

			for (Document document : listOfBundles) {
				JsonObject json = toJson(document);
				list.add(json);
			}

			log.info("jsonList> " + list.toString());

			return list;
			
		} catch (Exception e) {
			return list;
		}

		
	}



	private JsonObject toJson(Document document) {

		JsonReader reader = Json.createReader(new StringReader(document.toJson()));
		JsonObject json = reader.readObject();

		log.info("json> " + json.toString());

		return json;
		
	}


}
