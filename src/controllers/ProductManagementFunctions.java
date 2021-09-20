package controllers;

import java.util.List;
import data_access.IProductRepository;
import data_access.IUserRepository;
import data_access.InputOutputOperations;
import domain.*;
import utilities.AlreadyExistsException;
import utilities.CatalogueEntry;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;

public class ProductManagementFunctions implements IProductManagementFunctions {

	private IProductRepository productRepository;
	private IUserRepository userRepository;
	private InputOutputOperations io;

	public ProductManagementFunctions(InputOutputOperations io) {
		this.io = io;
		this.productRepository = io.inputProducts();
		this.userRepository = io.inputUsers();
	}

	public IProductRepository getProductRepository() {
		return productRepository;
	}

	public void createPartAndEmployeeForAssemblyOfManager(int catalogueNumberForNewPart, String username,
			String password, User currentUser) throws NotFoundException, AlreadyExistsException {
		CatalogueEntry entry = productRepository.findCatalogueEntryByNumber(catalogueNumberForNewPart);
		Product newPart = new Part(entry);
		if (userRepository.existUserName(username))
			throw new AlreadyExistsException("This employee already exists.");
		User user = new Employee(username, password);
		userRepository.save(user);
		Product assembly = productRepository.findAssemblyByNumber(((Manager) currentUser).getProduct().getNumber());
		((Assembly) assembly).addProduct(newPart);
		((Manager) currentUser).createEmployeeAndAssignPart(user, newPart);
		((Manager) currentUser).addAnotherProductToProduct(newPart);
	}

	public void createAssemblyAndAssignToManagerForAssemblyOfManager(String newAssemblyName, int newAssemblyNumber,
			int managerNumber, User currentUser) throws NotFoundException, AlreadyExistsException {

		if (productRepository.isAssemblyExistByNameAndNumber(newAssemblyName, newAssemblyNumber))
			throw new AlreadyExistsException("This assembly already exists.");
		Product newAssembly = new Assembly(newAssemblyName, newAssemblyNumber);
		User manager = userRepository.findManagerById(managerNumber);
		((Manager) currentUser).addAnotherProductToProduct(newAssembly);
		((Manager) currentUser).assignAssemblyToManager(newAssembly, manager);
	}

	public void createAssemblyForAdmin(String productName, int productNumber, User currentUser)
			throws AlreadyExistsException {
		try {
			productRepository.findAssemblyByNumber(productNumber);
			throw new AlreadyExistsException("This assembly already exists.");
		} catch (NotFoundException e) {
			Product assembly = new Assembly(productName, productNumber);
			((Admin) currentUser).addProduct(assembly);
			productRepository.save(assembly);
		}
	}

	public String getCataloguesString() {
		String str = "";
		for (CatalogueEntry entry : productRepository.getEntries()) {
			str += "\nNumber: " + entry.getNumber();
			str += "\nName: " + entry.getName();
			str += "\nCost: " + entry.getCost() + "\n";
		}
		return str;
	}

	public String getAllAssembliesString() {
		String str = "";
		for (Product assembly : productRepository.findAllAssemblies()) {
			str += "\nNumber: " + assembly.getNumber();
			str += "\nName: " + assembly.getName();
			str += "\nCost: " + assembly.getCost() + "\n";
		}
		return str;
	}

	public void createCatalogueEntryForManager(int number, String name, double cost) throws AlreadyExistsException {
		try {
			productRepository.findCatalogueEntryByNumber(number);
			throw new AlreadyExistsException("This catalogue entry already exists.");
		} catch (NotFoundException e) {
			CatalogueEntry entry = new CatalogueEntry(name, number, cost);
			productRepository.saveEntry(entry);
		}
	}

	public void saveProducts() {
		io.outputProducts(productRepository);
	}

	public IUserRepository getUserRepository() {
		return userRepository;
	}

	public User createManagerForAdmin(String username, String password, User currentUser)
			throws AlreadyExistsException {
		User manager = ((Admin) currentUser).createManager(username, password);
		userRepository.save(manager);
		return manager;
	}

	public String getProductTreeStringOfManager(User manager) {
		return ((Manager) manager).getProductTreeString();
	}

	public String getAllManagersStringForAdmin(User admin) {
		List<User> managers = ((Admin) admin).getManagers();
		String str = "";
		for (User manager : managers) {
			str += manager.getId() + ": " + manager.getUsername() + "\n";
		}
		return str;
	}

	public String getAllEmployeesStringForAdmin(User admin) {
		List<User> employees = ((Admin) admin).getAllEmployees();
		String str = "";
		for (User employee : employees) {
			str += employee.getId() + ": " + employee.getUsername() + "\n";
		}
		return str;
	}

	public String getAllProductTreesStringForAdmin(User admin) {
		List<User> managers = ((Admin) admin).getManagers();
		String str = "";
		for (User manager : managers) {
			str += ((Manager) manager).getProductTreeString() + "\n";
		}
		return str;
	}

	public String getEmployeesStringOfManager(User currentUser) {
		List<User> employees = ((Manager) currentUser).getEmployees();
		String str = "";
		for (User employee : employees) {
			str += employee.getId() + ": " + employee.getUsername() + "\n";
		}
		return str;
	}

	public String getAllManagersWithoutProductsString() {
		List<User> managers = userRepository.findManagers();
		String str = "";
		for (User manager : managers) {
			if (((Manager) manager).getProduct() == null)
				str += manager.getId() + ": " + manager.getUsername() + "\n";
		}
		return str;
	}

	public User getCurrentUser(String username, String password) throws NotFoundException, PasswordIncorrectException {
		return userRepository.findByUserNameAndPassword(username, password);
	}

	public void saveUsers() {
		io.outputUsers(userRepository);
	}

	public void assignAssemblyToManagerForAdmin(int productNumber, int managerId, User currentUser)
			throws NotFoundException {
		User manager = userRepository.findManagerById(managerId);
		Product product = productRepository.findAssemblyByNumber(productNumber);
		((Admin) currentUser).assignManagerToAssembly(manager, product);
	}
}
