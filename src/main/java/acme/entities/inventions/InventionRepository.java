
package acme.entities.inventions;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface InventionRepository extends AbstractRepository {

	@Query("SELECT SUM(p.cost.amount) FROM Part p WHERE p.invention.id = :inventionId")
	Double getCostPartsOfInvention(int inventionId);

	@Query("select i from Invention i where i.ticker = :ticker")
	Invention findInventionByTicker(String ticker);

}
