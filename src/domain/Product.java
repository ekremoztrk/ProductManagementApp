package domain;

import utilities.CompleteState;
import utilities.InProgressState;
import utilities.NotStartedState;
import utilities.Status;
import utilities.StatusChangeCallback;
import utilities.StatusState;

public abstract class Product implements StatusChangeCallback {

	private String name;
	private int number;
	private double cost;
	private StatusState status;
	private StatusChangeCallback callback;

	public Product(String name, int number) {
		setName(name);
		setNumber(number);
		setStatus(new NotStartedState(this));
	}

	public Product(String name, int number, double cost) {
		setName(name);
		setNumber(number);
		setCost(cost);
		setStatus(new NotStartedState(this));
	}

	public void changeStatus(Status status) {
		StatusChangeCallback callback = getCallback();
		StatusState newStatus = null;
		if (status == getStatus().getEnum()) {
			System.out
					.println("Status of '" + getName() + "' not changed as it was already: " + getStatus().toString());
			System.out.println();
			return;
		} else if (status == Status.NOT_STARTED) {
			newStatus = new NotStartedState(this);
		} else if (status == Status.IN_PROGRESS) {
			newStatus = new InProgressState(this);
		} else if (status == Status.COMPLETE) {
			newStatus = new CompleteState(this);
		}
		System.out.println("Status of '" + getName() + "' changed to: " + newStatus.toString());
		System.out.println();
		setStatus(newStatus);
		callback.onChildStatusChange();
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

	public StatusState getStatus() {
		return status;
	}

	protected void setStatus(StatusState status) {
		this.status = status;
	}

	public StatusChangeCallback getCallback() {
		return callback;
	}

	public void setCallback(StatusChangeCallback callback) {
		this.callback = callback;
	}

}