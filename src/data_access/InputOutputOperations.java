package data_access;

import domain.*;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utilities.CatalogueEntry;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class InputOutputOperations {

	private static FileWriter file;

	public InputOutputOperations() {
	}

	public void outputProducts(IProductRepository productRepository) {
		JSONObject productsJSON = new JSONObject();
		JSONObject assembliesAndPartsJson = getProductJson(productRepository);
		JSONObject catalogEntriesJson = getCatalogueEntriesJson(productRepository);
		productsJSON.put("assembliesAndParts", assembliesAndPartsJson);
		productsJSON.put("catalogEntries", catalogEntriesJson);
		try {

			// Constructs a FileWriter given a file name, using the platform's default
			// charset
			file = new FileWriter("products.json");
			file.write(productsJSON.toString());
//			CrunchifyLog("Successfully Copied JSON Object to File...");
//			CrunchifyLog("\nJSON Object: " + productsJSON);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private JSONObject getProductJson(IProductRepository productRepository) {
		JSONObject productsJSON = new JSONObject();
		List<JSONObject> assembliesJson = new ArrayList<>();

		List<Product> assemblies = productRepository.findAllAssemblies();
		for (Product product : assemblies) {
			assembliesJson.add(((Assembly) product).getProductTree());
		}

		productsJSON.put("ASSEMBLIES", assembliesJson);
		return productsJSON;
	}

	private JSONObject getCatalogueEntriesJson(IProductRepository productRepository) {
		JSONObject entriesJson = new JSONObject();
		List<CatalogueEntry> entries = productRepository.getEntries();
		List<JSONObject> entriesJsonList = new ArrayList<>();
		for (CatalogueEntry entry : entries) {
			entriesJsonList.add(entry.getJson());
		}
		entriesJson.put("CatalogEntries", entriesJsonList);
		return entriesJson;
	}

	public void outputUsers(IUserRepository userRepository) {
		JSONObject usersJson = new JSONObject();
		List<User> admins = userRepository.findAdmins();
		List<JSONObject> adminsJson = new ArrayList<>();
		for (User admin : admins) {
			adminsJson.add(admin.getJson());
		}
		usersJson.put("ALLUSERS", adminsJson);
		try {

			// Constructs a FileWriter given a file name, using the platform's default
			// charset
			file = new FileWriter("users.json");
			file.write(usersJson.toString());
//			CrunchifyLog("Successfully Copied JSON Object to File...");
//			CrunchifyLog("\nJSON Object: " + usersJson);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {
				file.flush();
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

//	static public void CrunchifyLog(String str) {
//		System.out.println(str);
//	}

	public IProductRepository inputProducts() {
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("products.json")) {
			Object obj = jsonParser.parse(reader);
			org.json.simple.JSONObject productsAndCatalogEntries = (org.json.simple.JSONObject) obj;
			List<CatalogueEntry> catalogueEntries = inputCatalogEntries(
					(org.json.simple.JSONObject) productsAndCatalogEntries.get("catalogEntries"));
			List<Product> products = inputProducts(
					(org.json.simple.JSONObject) productsAndCatalogEntries.get("assembliesAndParts"));
//			List<Product> allProducts = new ArrayList<>();
//			for(Product product:products){
//				if(product instanceof Part)
//					allProducts.add(product);
//				if(product instanceof Assembly)
//					allProducts.addAll(((Assembly) product).getAllProductsSeperatly());
//			}
			return new ProductRepository(products, catalogueEntries);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<CatalogueEntry> inputCatalogEntries(org.json.simple.JSONObject catalogEntriesJson) {
		List<CatalogueEntry> catalogueEntries = new ArrayList<>();
		org.json.simple.JSONArray entries = (org.json.simple.JSONArray) catalogEntriesJson.get("CatalogEntries");
		entries.forEach(entry -> catalogueEntries.add(CatalogueEntry.parseJson((org.json.simple.JSONObject) entry)));
		return catalogueEntries;

	}

	private List<Product> inputProducts(org.json.simple.JSONObject productsJson) {
		List<Product> products = new ArrayList<>();
		org.json.simple.JSONArray assemblies = (org.json.simple.JSONArray) productsJson.get("ASSEMBLIES");
		assemblies.forEach(entry -> products.add(Assembly.parseJson((org.json.simple.JSONObject) entry)));
		return products;
	}

	public IUserRepository inputUsers() {
		IProductRepository productRepository = inputProducts();
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("users.json")) {
			Object obj = jsonParser.parse(reader);
			org.json.simple.JSONObject usersJson = (org.json.simple.JSONObject) obj;
			org.json.simple.JSONArray admins = (org.json.simple.JSONArray) usersJson.get("ALLUSERS");
			List<User> users = parseUserArray(admins, productRepository);
			IUserRepository repository = new UserRepository(users);
			User.setId_counter(repository.findBiggestId() + 1);
			return repository;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<User> parseUserArray(org.json.simple.JSONArray usersJson, IProductRepository productRepository) {
		List<User> admins = new ArrayList<>();
		List<User> users = new ArrayList<>();
		usersJson.forEach(entry -> admins.add(Admin.parseJson((org.json.simple.JSONObject) entry, productRepository)));
		for (User admin : admins) {
			users.addAll(((Admin) admin).getManagers());
			users.addAll(((Admin) admin).getAllEmployees());
			users.add(admin);
		}

		return users;
	}

}
