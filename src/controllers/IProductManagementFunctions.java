package controllers;

import data_access.IProductRepository;
import data_access.IUserRepository;
import domain.User;
import utilities.AlreadyExistsException;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;

public interface IProductManagementFunctions {

	public IProductRepository getProductRepository();

	public void createPartAndEmployeeForAssemblyOfManager(int catalogueNumberForNewPart, String username,
			String password, User currentUser) throws NotFoundException, AlreadyExistsException;

	public void createAssemblyAndAssignToManagerForAssemblyOfManager(String newAssemblyName, int newAssemblyNumber,
			int managerNumber, User currentUser) throws NotFoundException, AlreadyExistsException;

	public void createAssemblyForAdmin(String productName, int productNumber, User currentUser)
			throws AlreadyExistsException;

	public String getCataloguesString();

	public String getAllAssembliesString();

	public void createCatalogueEntryForManager(int number, String name, double cost) throws AlreadyExistsException;

	public void saveProducts();

	public IUserRepository getUserRepository();

	public User createManagerForAdmin(String username, String password, User currentUser) throws AlreadyExistsException;

	public String getProductTreeStringOfManager(User manager);

	public String getAllManagersStringForAdmin(User admin);

	public String getAllEmployeesStringForAdmin(User admin);

	public String getAllProductTreesStringForAdmin(User admin);

	public String getEmployeesStringOfManager(User currentUser);

	public String getAllManagersWithoutProductsString();

	public User getCurrentUser(String username, String password) throws NotFoundException, PasswordIncorrectException;

	public void saveUsers();

	public void assignAssemblyToManagerForAdmin(int productNumber, int managerId, User currentUser)
			throws NotFoundException;
}
