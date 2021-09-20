package utilities;

import domain.Product;

public class NotStartedState extends StatusState {

	public NotStartedState(Product product) {
		super(product);
	}

	@Override
	public String toString() {
		return "Not Started";
	}

	@Override
	public Status getEnum() {
		return Status.NOT_STARTED;
	}

	@Override
	public int getValue() {
		return 0;
	}
}