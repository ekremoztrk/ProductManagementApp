package view;

import data_access.InputOutputOperations;
import domain.*;
import utilities.AlreadyExistsException;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;
import utilities.Status;
import java.util.InputMismatchException;
import java.util.Scanner;
import controllers.IProductManagementFunctions;
import controllers.ProductManagementFunctions;

public class ProductManagement {

	private User currentUser;
	private IProductManagementFunctions productFunctions;

	public ProductManagement(InputOutputOperations io) {
		this.productFunctions = new ProductManagementFunctions(io);
	}

	public void start() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("==================================================");
		System.out.println("=============== PRODUCT MANAGEMENT ===============");
		System.out.println("==================================================\n");
		try {
			login(scanner);
			mainMenu(scanner);
		} catch (NotFoundException | PasswordIncorrectException e) {
			System.out.println("User not found or password is wrong");
			start();
		}
	}

	private void login(Scanner scanner) throws NotFoundException, PasswordIncorrectException {
		System.out.println("===================== Login ======================");
		System.out.print("Please enter your username: ");
		String username = scanner.next();
		System.out.print("Please enter your password: ");
		String password = scanner.next();
		User currentUser = productFunctions.getCurrentUser(username, password);
		System.out.println("\nWelcome " + currentUser.getUsername() + "!\n");
		System.out.println("What would you like to do?");
		this.currentUser = currentUser;
	}

	private void mainMenu(Scanner scanner) {
		if (currentUser instanceof Admin)
			adminMenu(scanner);
		else if (currentUser instanceof Manager)
			managerMenu(scanner);
		else if (currentUser instanceof Employee)
			employeeMenu(scanner);
	}

	private void closeProgram(Scanner scanner) {
		scanner.close();
		productFunctions.saveUsers();
		productFunctions.saveProducts();
		System.exit(0);
	}

	private int scannerNextInt(Scanner scanner) {
		int choice = -1;
		try {
			choice = scanner.nextInt();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Invalid input, please try again.\n");
			return -1;
		}
		System.out.println();
		return choice;
	}

	private double scannerNextDouble(Scanner scanner) {
		double choice = -1;
		try {
			choice = scanner.nextDouble();
		} catch (InputMismatchException e) {
			scanner.nextLine();
			System.out.println("Invalid input, please try again.\n");
			return -1;
		}
		System.out.println();
		return choice;
	}

	// ADMIN
	private void adminMenu(Scanner scanner) {
		System.out.println();
		System.out.println("1. Create manager");
		System.out.println("2. Assign product to manager.");
		System.out.println("3. Print all managers.");
		System.out.println("4. Print all employees.");
		System.out.println("5. Print all product trees.");
		System.out.println("6. Log out.");
		System.out.println("7. Exit.");
		System.out.print("Please enter a number: ");
		int choice = scannerNextInt(scanner);
		if (choice == -1)
			adminMenu(scanner);
		switch (choice) {
		case 1:
			createManagerMenu(scanner);
			break;
		case 2:
			productMenuForAdmin(scanner);
			break;
		case 3:
			System.out.println(productFunctions.getAllManagersStringForAdmin(currentUser));
			adminMenu(scanner);
			break;
		case 4:
			System.out.println(productFunctions.getAllEmployeesStringForAdmin(currentUser));
			adminMenu(scanner);
			break;
		case 5:
			System.out.println(productFunctions.getAllProductTreesStringForAdmin(currentUser));
			adminMenu(scanner);
			break;
		case 6:
			productFunctions.saveUsers();
			productFunctions.saveProducts();
			start();
			break;
		case 7:
			closeProgram(scanner);
			break;
		default:
			adminMenu(scanner);
			break;
		}
	}

	private void createManagerMenu(Scanner scanner) {
		System.out.print("Please enter a manager username: ");
		String username = scanner.next();
		System.out.print("Please enter a manager password: ");
		String password = scanner.next();
		User createdManager = null;
		try {
			createdManager = productFunctions.createManagerForAdmin(username, password, currentUser);
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Manager with username " + createdManager.getUsername() + " created.");
		productFunctions.saveUsers();
		adminMenu(scanner);
	}

	private void productMenuForAdmin(Scanner scanner) {
		System.out.println();
		System.out.println("1. Assign assembly to manager");
		System.out.println("2. Create a new assembly and assign it to a manager.");
		System.out.println("3. Go back.");
		System.out.println("4. Log out.");
		System.out.println("0. Exit.");
		System.out.print("Please enter a number: ");
		int choice = scannerNextInt(scanner);
		if (choice == -1)
			productMenuForAdmin(scanner);
		switch (choice) {
		case 1:
			addAssemblyToManagerMenu(scanner);
			break;
		case 2:
			createAssemblyAndAssignToManagerMenuForAdmin(scanner);
			break;
		case 3:
			adminMenu(scanner);
			break;
		case 4:
			productFunctions.saveUsers();
			productFunctions.saveProducts();
			start();
			break;
		case 0:
			closeProgram(scanner);
			break;
		default:
			adminMenu(scanner);
		}
	}

	private void addAssemblyToManagerMenu(Scanner scanner) {
		System.out.println(productFunctions.getAllManagersStringForAdmin(currentUser));
		System.out.print("Please select a manager from above and enter the id: ");
		int managerId = scannerNextInt(scanner);
		if (managerId == -1)
			addAssemblyToManagerMenu(scanner);
		System.out.println(productFunctions.getAllAssembliesString());
		System.out.print("Please select an assembly from above and enter the number: ");
		int productNumber = scannerNextInt(scanner);
		if (productNumber == -1)
			addAssemblyToManagerMenu(scanner);
		try {
			productFunctions.assignAssemblyToManagerForAdmin(productNumber, managerId, currentUser);
			System.out.println("Assembly has been assigned to manager with id " + managerId + ".\n");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		productFunctions.saveUsers();
		productFunctions.saveProducts();
		adminMenu(scanner);
	}

	private void createAssemblyAndAssignToManagerMenuForAdmin(Scanner scanner) {
		System.out.println(productFunctions.getAllManagersStringForAdmin(currentUser));
		System.out.print("Please select a manager from above and enter the id: ");
		int managerId = scannerNextInt(scanner);
		if (managerId == -1)
			createAssemblyAndAssignToManagerMenuForAdmin(scanner);
		System.out.print("Please enter the name of the new assembly: ");
		String productName = scanner.next();
		System.out.print("Please enter the number of the new assembly: ");
		int productNumber = scannerNextInt(scanner);
		if (productNumber == -1)
			createAssemblyAndAssignToManagerMenuForAdmin(scanner);
		try {
			productFunctions.createAssemblyForAdmin(productName, productNumber, currentUser);
			System.out.println("Assembly created with name " + productName + " and number " + productNumber + ".\n");
		} catch (AlreadyExistsException e1) {
			System.out.println(e1.getMessage());
			adminMenu(scanner);
		}
		try {
			productFunctions.assignAssemblyToManagerForAdmin(productNumber, managerId, currentUser);
			System.out.println("Created assembly has been assigned to manager with id " + managerId + ".\n");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		productFunctions.saveUsers();
		productFunctions.saveProducts();
		adminMenu(scanner);
	}

	// MANAGER
	private void managerMenu(Scanner scanner) {
		System.out.println();
		System.out.println("1. Create part for my assembly and assign to new employee.");
		System.out.println("2. Create assembly for my assembly and assign to another manager.");
		System.out.println("3. Create catalogue entry.");
		System.out.println("4. Print catalogue entries.");
		System.out.println("5. Print product tree.");
		System.out.println("6. Print employees.");
		System.out.println("7. Log out.");
		System.out.println("0. Exit.");
		System.out.print("Please enter a number: ");
		int choice = scannerNextInt(scanner);
		if (choice == -1)
			managerMenu(scanner);
		switch (choice) {
		case 1:
			createPartAndEmployeeForMyAssemblyMenu(scanner);
			break;
		case 2:
			createAssemblyAndAssignToManagerForMyAssemblyMenu(scanner);
			break;
		case 3:
			createCatalogueMenu(scanner);
			break;
		case 4:
			System.out.println(productFunctions.getCataloguesString());
			managerMenu(scanner);
			break;
		case 5:
			System.out.println(productFunctions.getProductTreeStringOfManager(currentUser));
			managerMenu(scanner);
			break;
		case 6:
			System.out.println(productFunctions.getEmployeesStringOfManager(currentUser));
			managerMenu(scanner);
			break;
		case 7:
			productFunctions.saveUsers();
			productFunctions.saveProducts();
			start();
			break;
		case 0:
			closeProgram(scanner);
			break;
		default:
			managerMenu(scanner);
			break;
		}
	}

	private void createPartAndEmployeeForMyAssemblyMenu(Scanner scanner) {
		System.out.println(productFunctions.getCataloguesString());
		System.out.print("Please select the number of the part from the catalogue above: ");
		int catalogueNumberForNewPart = scannerNextInt(scanner);
		if (catalogueNumberForNewPart == -1)
			createPartAndEmployeeForMyAssemblyMenu(scanner);
		System.out.print("Please enter an employee username: ");
		String username = scanner.next();
		System.out.print("Please enter an employee password: ");
		String password = scanner.next();

		try {
			productFunctions.createPartAndEmployeeForAssemblyOfManager(catalogueNumberForNewPart, username, password,
					currentUser);
			System.out.println("Part created and assigned to new employee with username " + username
					+ ". New part added to my assembly.");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
		productFunctions.saveProducts();
		productFunctions.saveUsers();
		managerMenu(scanner);
	}

	private void createAssemblyAndAssignToManagerForMyAssemblyMenu(Scanner scanner) {
		System.out.print("Please enter the name of the new assembly: ");
		String newAssemblyName = scanner.next();
		System.out.print("Please enter the number of the new assembly: ");
		int newAssemblyNumber = scannerNextInt(scanner);
		if (newAssemblyNumber == -1)
			createAssemblyAndAssignToManagerForMyAssemblyMenu(scanner);
		System.out.println(productFunctions.getAllManagersWithoutProductsString());
		System.out.print("Please select the manager to assign the assembly above and enter their id: ");
		int managerNumber = scannerNextInt(scanner);
		if (managerNumber == -1)
			createAssemblyAndAssignToManagerForMyAssemblyMenu(scanner);

		try {
			productFunctions.createAssemblyAndAssignToManagerForAssemblyOfManager(newAssemblyName, newAssemblyNumber,
					managerNumber, currentUser);
			System.out.println("Assembly created with name " + newAssemblyName + " and assigned to manager with id: "
					+ managerNumber + ". New assembly added to my assembly.");
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
		productFunctions.saveUsers();
		productFunctions.saveProducts();
		managerMenu(scanner);
	}

	private void createCatalogueMenu(Scanner scanner) {
		System.out.print("Please enter the number: ");
		int number = scannerNextInt(scanner);
		if (number == -1)
			createCatalogueMenu(scanner);
		System.out.print("Please enter the name: ");
		String name = scanner.next();
		System.out.print("Please enter the cost: ");
		double cost = scannerNextDouble(scanner);
		if (cost == -1)
			createCatalogueMenu(scanner);
		try {
			productFunctions.createCatalogueEntryForManager(number, name, cost);
			System.out.println("Catalogue entry created.");
		} catch (AlreadyExistsException e) {
			System.out.println(e.getMessage());
		}
		productFunctions.saveProducts();
		managerMenu(scanner);
	}

	// EMPLOYEE
	private void employeeMenu(Scanner scanner) {
		System.out.println();
		System.out.println("1. Change status of part.");
		System.out.println("2. See details of part.");
		System.out.println("3. Log out.");
		System.out.println("0. Exit.");
		System.out.print("Please enter a number: ");
		int choice = scannerNextInt(scanner);
		if (choice == -1)
			employeeMenu(scanner);
		switch (choice) {
		case 1:
			changeStatusOfPartMenu(scanner);
			break;
		case 2:
			printStatusOfPart();
			employeeMenu(scanner);
			break;
		case 3:
			productFunctions.saveUsers();
			productFunctions.saveProducts();
			start();
			break;
		case 0:
			closeProgram(scanner);
			break;
		default:
			employeeMenu(scanner);
		}
	}

	private void changeStatusOfPartMenu(Scanner scanner) {
		printStatusOfPart();
		System.out.println();
		System.out.println("1. Not Started");
		System.out.println("2. In Progress");
		System.out.println("3. Complete");
		System.out.println("4. Go back.");
		System.out.println("5. Log out.");
		System.out.println("0. Exit.");
		System.out.print("Please enter a number: ");
		int choice = scannerNextInt(scanner);
		if (choice == -1)
			changeStatusOfPartMenu(scanner);
		switch (choice) {
		case 1:
			((Employee) currentUser).changeStatusOfPart(Status.NOT_STARTED);
			employeeMenu(scanner);
			break;
		case 2:
			((Employee) currentUser).changeStatusOfPart(Status.IN_PROGRESS);
			employeeMenu(scanner);
			break;
		case 3:
			((Employee) currentUser).changeStatusOfPart(Status.COMPLETE);
			employeeMenu(scanner);
			break;
		case 4:
			employeeMenu(scanner);
			break;
		case 5:
			productFunctions.saveUsers();
			productFunctions.saveProducts();
			start();
			break;
		case 0:
			closeProgram(scanner);
			break;
		default:
			employeeMenu(scanner);
		}
	}

	private void printStatusOfPart() {
		Product part = ((Employee) currentUser).getPart();
		if (part != null) {
			System.out.println(
					"Status of " + part.getName() + " (" + part.getNumber() + "): " + part.getStatus().toString());
			System.out.println();
		} else {
			System.out.println("Employee does not have an assigned part.\n");
		}
	}

}