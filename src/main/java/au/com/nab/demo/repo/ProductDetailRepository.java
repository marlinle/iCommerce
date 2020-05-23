package au.com.nab.demo.repo;

import au.com.nab.demo.domain.ProductDetail;
import org.springframework.data.repository.CrudRepository;

public interface ProductDetailRepository extends CrudRepository<ProductDetail, Integer> {
}
