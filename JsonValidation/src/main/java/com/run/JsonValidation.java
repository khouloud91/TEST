package com.run;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class JsonValidation {
	private static int numberJsonParsed;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Set<URL> urls = new HashSet<URL>();
		urls.add(new URL("https://git.io/vpg9V"));
		urls.add(new URL("https://git.io/vpg95"));

		for (URL url : urls) {
			validateUrl(url);
		}
	}

	public static void validateUrl(URL url) throws IOException {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		WebResource service = client.resource(url.toString());
		String response = service.get(String.class);
		JSONArray jsonArray = new JSONArray(response);

		ObjectMapper mapper = new ObjectMapper();

		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		JsonNode shema = JsonLoader.fromResource("/shema.json");
		JsonSchema schema = null;
		try {
			schema = factory.getJsonSchema(shema);
		} catch (ProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (schema != null) {
			for (Object obj : jsonArray) {
				JsonNode actualObj;
				try {
					actualObj = mapper.readTree(obj.toString());
					numberJsonParsed++;

					ProcessingReport report;
					try {
						report = schema.validate(actualObj);
						System.out.println(report);
					} catch (ProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			System.out.println("Number OF Json Parsed : " + numberJsonParsed);	
			System.out.println("***************************************");


		}
	}

}
