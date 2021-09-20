package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import data_access.IProductRepository;
import org.json.JSONObject;
import utilities.NotFoundException;

@SuppressWarnings("unchecked")
public class Admin extends User {

	private List<Product> products = new ArrayList<>();
	private List<User> managers = new ArrayList<>();

	public Admin(String username, String password) {
		super(username, password);
	}

	public Admin(int id, String username, String password) {
		super(id, username, password);
	}

	@Override
	public JSONObject getJson() {
		JSONObject usersJson = new JSONObject();
		List<JSONObject> managersJson = new ArrayList<>();
		for (User manager : managers) {
			managersJson.add(((Manager) manager).getJson());
		}
		usersJson.put("Id", getId());
		usersJson.put("Username", getUsername());
		usersJson.put("MANAGERS", managersJson);
		usersJson.put("password", getPassword());

		return usersJson;
	}

	public List<User> getAllEmployees() {
		List<User> managers = getManagers();
		List<User> employees = new ArrayList<>();
		for (User manager : managers)
			if (((Manager) manager).getEmployees() != null) {
				employees.addAll(((Manager) manager).getEmployees());
			}
		return employees;
	}

	public Product createAssembly(String name, int number) {
		Product assembly = new Assembly(name, number);
		List<Product> products = getProducts();
		products.add(assembly);
		setProducts(products);
		return assembly;
	}

	public User createManager(String username, String password) {
		User manager = new Manager(username, password);
		managers.add(manager);
		return manager;
	}

	public void assignManagerToAssembly(User manager, Product assembly) {
		((Manager) manager).setProduct(assembly);
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void addProduct(Product product) {
		List<Product> products = getProducts();
		products.add(product);
		setProducts(products);
	}

	public List<User> getManagers() {
		return managers;
	}

	public void setManagers(List<User> managers) {
		this.managers = managers;
	}

	public static User parseJson(org.json.simple.JSONObject userJson, IProductRepository productRepository) {
		String userName = (String) userJson.get("Username");
		String password = (String) userJson.get("password");
		int id = ((Long) userJson.get("Id")).intValue();
		org.json.simple.JSONArray managersJson = (org.json.simple.JSONArray) userJson.get("MANAGERS");
		List<User> managers = new ArrayList<>();
		if (managersJson.size() > 0) {
			managersJson.forEach(entry -> {
				try {
					managers.add(Manager.parseJson((org.json.simple.JSONObject) entry, productRepository));
				} catch (NotFoundException e) {
					System.out.println(e.getMessage());
				}
			});
		}
		Admin admin = new Admin(id, userName, password);
		Set<Product> adminProducts = new HashSet<>();
		if (managers != null)
			for (User manager : managers) {
				adminProducts.add(((Manager) manager).getProduct());
			}
		admin.setProducts(new ArrayList<>(adminProducts));
		admin.setManagers(managers);
		return admin;
	}
}
