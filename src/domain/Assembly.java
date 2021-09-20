package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import utilities.Status;
import utilities.StatusState;

@SuppressWarnings("unchecked")
public class Assembly extends Product {

	private List<Product> products;

	public Assembly(String name, int number) {
		super(name, number);
		setProducts(new ArrayList<>());
		setCallback(this);
	}

	public void addProduct(Product product) {
		List<Product> products = getProducts();
		product.setCallback(this);
		products.add(product);
		setProducts(products);
		this.onChildStatusChange();
	}

	public JSONObject getProductTree() {
		List<Product> products = getProducts();
		List<JSONObject> parts = new ArrayList<>();
		List<JSONObject> assemblies = new ArrayList<>();

		JSONObject productTree = new JSONObject();
		productTree.put("number", getNumber());
		productTree.put("name", getName());
		productTree.put("status", getStatus());
		productTree.put("cost", getCost());
		for (Product product : products) {
			if (product instanceof Part) {
				JSONObject part = new JSONObject();
				part.put("number", product.getNumber());
				part.put("name", product.getName());
				part.put("status", product.getStatus());
				part.put("cost", product.getCost());
				parts.add(part);
			} else
				assemblies.add(((Assembly) product).getProductTree());
		}
		productTree.put("PARTS", parts);
		productTree.put("ASSEMBLIES", assemblies);
		return productTree;
	}

	public String getProductString(JSONObject json, int indentation, String str) {
		String indentString = new String(new char[indentation]).replace("\0", "  ");
		String name = (String) json.get("name");
		int number = (int) json.get("number");
		double cost = (double) json.get("cost");
		StatusState status = (StatusState) json.get("status");
		str += indentString + "Name: " + name + "\n";
		str += indentString + "Number: " + number + "\n";
		str += indentString + "Cost: " + cost + "\n";
		str += indentString + "Status: " + status.toString() + "\n";

		if (json.has("PARTS")) {
			JSONArray parts = json.optJSONArray("PARTS");
			str = fillStringWithProducts(parts, indentString, indentation, "Parts", str);
		}
		if (json.has("ASSEMBLIES")) {
			JSONArray assemblies = json.optJSONArray("ASSEMBLIES");
			str = fillStringWithProducts(assemblies, indentString, indentation, "Assemblies", str);
		}
		return str;
	}

	private String fillStringWithProducts(JSONArray products, String indentString, int indentation, String print,
			String str) {
		if (products != null) {
			str += indentString + print + ": " + (products.length() == 0 ? "None" : "") + "\n";
			for (int i = 0; i < products.length(); i++) {
				JSONObject product = (JSONObject) products.get(i);
				str = getProductString(product, indentation + 1, str);
				if (i != products.length() - 1)
					str += "\n";
			}
		} else {
			str += indentString + print + ": None\n";
		}
		return str;
	}

	@Override
	public double getCost() {
		double cost = 0;
		List<Product> products = getProducts();
		for (Product product : products) {
			cost += product.getCost();
		}
		return cost;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public StatusState onChildStatusChange() {
		List<Product> products = getProducts();
		int total = 0;
		for (Product product : products) {
			total += product.getStatus().getValue();
		}
		if (total == 0 && getStatus().getEnum() != Status.NOT_STARTED) {
			changeStatus(Status.NOT_STARTED);
		} else if (total == 2 * products.size() && getStatus().getEnum() != Status.COMPLETE) {
			changeStatus(Status.COMPLETE);
		} else if (total < 2 * products.size() && total > 0 && getStatus().getEnum() != Status.IN_PROGRESS) {
			changeStatus(Status.IN_PROGRESS);
		}
		return getStatus();
	}

	public static Assembly parseJson(org.json.simple.JSONObject assemblyJson) {
		String name = (String) assemblyJson.get("name");
		int number = ((Long) assemblyJson.get("number")).intValue();
		List<Product> products = new ArrayList<>();
		org.json.simple.JSONArray parts = (org.json.simple.JSONArray) assemblyJson.get("PARTS");
		org.json.simple.JSONArray assemblies = (org.json.simple.JSONArray) assemblyJson.get("ASSEMBLIES");
		if (parts.size() > 0) {
			parts.forEach(entry -> products.add(Part.parseJson((org.json.simple.JSONObject) entry)));
		}
		if (assemblies.size() > 0) {
			assemblies.forEach(entry -> products.add(Assembly.parseJson((org.json.simple.JSONObject) entry)));
		}
		Assembly assembly = new Assembly(name, number);
		for (Product product : products)
			assembly.addProduct(product);
		return assembly;
	}

	public List<Product> getAllProductsSeperatly() {
		List<Product> allProducts = new ArrayList<>();
		for (Product product : products) {
			if (product instanceof Part)
				allProducts.add(product);
			if (product instanceof Assembly)
				allProducts.addAll(((Assembly) product).getAllProductsSeperatly());
		}
		allProducts.add(this);
		return allProducts;
	}

	public List<Product> getAssembliesInProducts() {
		List<Product> assemblies = new ArrayList<>();
		for (Product product : products) {
			if (product instanceof Assembly)
				assemblies.addAll(((Assembly) product).getAssembliesInProducts());
			else
				return Arrays.asList(this);
		}
		assemblies.add(this);
		return assemblies;
	}

	public List<Product> getPartsInProducts() {
		List<Product> assemblies = getAssembliesInProducts();
		List<Product> parts = new ArrayList<>();
		for (Product product : assemblies) {
			for (Product product1 : ((Assembly) product).getProducts()) {
				if (product1 instanceof Part)
					parts.add(product1);
			}

		}
		return parts;
	}
}