package utilities;

import domain.Product;

public class CompleteState extends StatusState {

	public CompleteState(Product product) {
		super(product);
	}

	@Override
	public String toString() {
		return "Complete";
	}

	@Override
	public Status getEnum() {
		return Status.COMPLETE;
	}

	@Override
	public int getValue() {
		return 2;
	}
}