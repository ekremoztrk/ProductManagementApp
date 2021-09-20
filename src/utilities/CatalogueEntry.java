package utilities;

import org.json.JSONObject;

public class CatalogueEntry {

	private String name;
	private int number;
	private double cost;

	public CatalogueEntry(String name, int number, double cost) {
		this.name = name;
		this.number = number;
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public JSONObject getJson() {
		JSONObject entryJson = new JSONObject();
		entryJson.put("name", getName());
		entryJson.put("number", getNumber());
		entryJson.put("cost", getCost());
		return entryJson;
	}

	public static CatalogueEntry parseJson(org.json.simple.JSONObject catalogEntry) {
		// Get employee first name
		String name = (String) catalogEntry.get("name");
		double cost = ((Long) catalogEntry.get("cost")).doubleValue();
		int number = ((Long) catalogEntry.get("number")).intValue();
		return new CatalogueEntry(name, number, cost);
	}

}
