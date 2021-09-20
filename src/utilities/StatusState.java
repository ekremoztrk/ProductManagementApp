package utilities;

import domain.Product;

public abstract class StatusState {

	private Product product;

	public StatusState(Product product) {
		setProduct(product);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public abstract String toString();

	public abstract Status getEnum();

	public abstract int getValue();
}