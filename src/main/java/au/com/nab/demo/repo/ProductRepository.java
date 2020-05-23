package au.com.nab.demo.repo;

import au.com.nab.demo.domain.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    List<Product> findByName(@Param("name") String name);

    List<Product> findByBrand(@Param("brand") String brand);

    List<Product> findByColor(@Param("color") String color);

    List<Product> findByPriceBetween(@Param("priceLow") Float priceLow, @Param("priceHigh") Float priceHigh);
}
