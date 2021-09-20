package domain;

import org.json.JSONObject;

public abstract class User {

	private int id;
	private String username;
	private String password;
	private static int id_counter = 5;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.id = User.id_counter;
		User.id_counter++;
	}

	public User(int id, String username, String password) {
		this.username = username;
		this.password = password;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static int getId_counter() {
		return id_counter;
	}

	public static void setId_counter(int id_counter) {
		User.id_counter = id_counter;
	}

	public abstract JSONObject getJson();

}