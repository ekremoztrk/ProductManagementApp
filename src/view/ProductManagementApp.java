package view;

import data_access.InputOutputOperations;

public class ProductManagementApp {

	public static void main(String[] args) throws Exception {
		InputOutputOperations io = new InputOutputOperations();
		ProductManagement productManagement = new ProductManagement(io);
		productManagement.start();
	}

}