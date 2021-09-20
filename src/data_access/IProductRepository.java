package data_access;

import domain.Product;
import utilities.CatalogueEntry;
import utilities.NotFoundException;
import java.util.List;

public interface IProductRepository {

	List<Product> findAllAssemblies();

	List<Product> findAllParts();

	Product findAssemblyByNumber(int number) throws NotFoundException;

	Product findPartByNumber(int number) throws NotFoundException;

	List<Product> findAssemblyParts(Product assembly);

	CatalogueEntry findCatalogueEntryByNumber(int number) throws NotFoundException;

	List<CatalogueEntry> getEntries();

	Product save(Product product);

	CatalogueEntry saveEntry(CatalogueEntry entry);

	boolean isAssemblyExistByNameAndNumber(String name, int number);

	boolean isCatalogEntryExistByNameAndId(String name, int number);
}