package data_access;

import domain.User;
import utilities.AlreadyExistsException;
import utilities.NotFoundException;
import utilities.PasswordIncorrectException;
import java.util.List;

public interface IUserRepository {

	User findByUserNameAndPassword(String userName, String password)
			throws NotFoundException, PasswordIncorrectException;

	User findManagerById(int id) throws NotFoundException;

	User findEmployeeById(int id) throws NotFoundException;

	List<User> findAdmins();

	List<User> findManagers();

	List<User> findEmployees();

	User save(User user) throws AlreadyExistsException;

	int findBiggestId();

	boolean existUserName(String userName);
}