package data_access;

import domain.Admin;
import domain.Employee;
import domain.Manager;
import domain.User;
import utilities.AlreadyExistsException;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {

	private List<User> users;

	public UserRepository(List<User> users) {
		this.users = users;
	}

	public User findByUserNameAndPassword(String userName, String password)
			throws NotFoundException, PasswordIncorrectException {
		for (User user : users) {
			if (user.getUsername().equals(userName)) {
				if (user.getPassword().equals(password))
					return user;
				else
					throw new PasswordIncorrectException();
			}

		}
		throw new NotFoundException("User with the username not found.");
	}

	public User findManagerById(int id) throws NotFoundException {
		for (User user : users) {
			if (user instanceof Manager && user.getId() == id) {
				return user;
			}
		}
		throw new NotFoundException("Manager with the given id not found.");
	}

	public User findEmployeeById(int id) throws NotFoundException {
		for (User user : users) {
			if (user instanceof Employee && user.getId() == id) {
				return user;
			}
		}
		throw new NotFoundException("Employee with the given id not found.");
	}

	public List<User> findAdmins() {
		List<User> admins = new ArrayList<>();
		for (User user : users) {
			if (user instanceof Admin)
				admins.add(user);
		}
		return admins;
	}

	public List<User> findManagers() {
		List<User> managers = new ArrayList<>();
		for (User user : users) {
			if (user instanceof Manager)
				managers.add(user);
		}
		return managers;
	}

	public List<User> findEmployees() {
		List<User> employees = new ArrayList<>();
		for (User user : users) {
			if (user instanceof Employee)
				employees.add(user);
		}
		return employees;
	}

	public User save(User user) throws AlreadyExistsException {
		if (existUserName(user.getUsername()))
			throw new AlreadyExistsException("User already exist in the system");
		users.add(user);
		return user;
	}

	public int findBiggestId() {
		int biggestID = 0;
		for (User user : users) {
			if (user.getId() > biggestID)
				biggestID = user.getId();
		}
		return biggestID;
	}

	public boolean existUserName(String userName) {
		for (User user : users) {
			if (user.getUsername().equals(userName))
				return true;
		}
		return false;
	}
}