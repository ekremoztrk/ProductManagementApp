package domain;

import org.json.JSONObject;
import utilities.CatalogueEntry;
import utilities.NotStartedState;
import utilities.StatusState;

public class Part extends Product {

	private CatalogueEntry entry;

	public Part(CatalogueEntry entry) {
		super(entry.getName(), entry.getNumber(), entry.getCost());
		setEntry(entry);
	}

	public CatalogueEntry getEntry() {
		return entry;
	}

	public void setEntry(CatalogueEntry entry) {
		this.entry = entry;
	}

	@Override
	public StatusState onChildStatusChange() {
		return getStatus();
	}

	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		json.put("number", getNumber());
		json.put("name", getName());
		json.put("status", getStatus());
		json.put("cost", getCost());
		return json;
	}

	public static Product parseJson(org.json.simple.JSONObject partJson) {
		String name = (String) partJson.get("name");
		double cost = ((Long) partJson.get("cost")).doubleValue();
		int number = ((Long) partJson.get("number")).intValue();
		String status = (String) partJson.get("status");
		Product part = new Part(new CatalogueEntry(name, number, cost));
		if (status.equals("Not Started"))
			part.setStatus(new NotStartedState(part));
		else if (status.equals("In Progress"))
			part.setStatus(new NotStartedState(part));
		else
			part.setStatus(new NotStartedState(part));
		return part;
	}

}