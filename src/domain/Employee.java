package domain;

import java.util.ArrayList;
import data_access.IProductRepository;
import org.json.JSONObject;
import utilities.NotFoundException;
import utilities.Status;

public class Employee extends User {

	private Product part;

	public Employee(String username, String password) {
		super(username, password);
	}

	public Employee(int id, String username, String password) {
		super(id, username, password);
	}

	public void changeStatusOfPart(Status status) {
		Product part = getPart();
		if (part == null) {
			System.out.println("Employee does not have an assigned part.");
			return;
		}
		getPart().changeStatus(status);
	}

	public Product getPart() {
		return part;
	}

	public void setPart(Product part) {
		this.part = part;
	}

	@Override
	public JSONObject getJson() {
		JSONObject employeeJson = new JSONObject();
		employeeJson.put("Username", getUsername());
		if (getPart() == null) {
			employeeJson.put("Part", new ArrayList<>());
		} else {
			employeeJson.put("Part", ((Part) getPart()).getJson());
		}
		employeeJson.put("password", getPassword());
		employeeJson.put("Id", getId());

		return employeeJson;
	}

	public static User parseJson(org.json.simple.JSONObject userJson, IProductRepository productRepository)
			throws NotFoundException {
		String userName = (String) userJson.get("Username");
		String password = (String) userJson.get("password");
		int id = ((Long) userJson.get("Id")).intValue();
		// org.json.simple.JSONArray employeesJson = (org.json.simple.JSONArray)
		// userJson.get("EMPLOYEES");
		Product part = Part.parseJson((org.json.simple.JSONObject) userJson.get("Part"));
		part = productRepository.findPartByNumber(part.getNumber());
		Employee employee = new Employee(id, userName, password);
		employee.setPart(part);
		return employee;
	}
}
