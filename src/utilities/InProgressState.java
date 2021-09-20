package utilities;

import domain.Product;

public class InProgressState extends StatusState {

	public InProgressState(Product product) {
		super(product);
	}

	@Override
	public String toString() {
		return "In Progress";
	}

	@Override
	public Status getEnum() {
		return Status.IN_PROGRESS;
	}

	@Override
	public int getValue() {
		return 1;
	}
}