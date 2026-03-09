
package acme.entities.inventions;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface PartRepository extends AbstractRepository {

	@Query("select p from Part p where p.invention.id = :inventionId")
	Collection<Part> findByInventionId(int inventionId);

}
