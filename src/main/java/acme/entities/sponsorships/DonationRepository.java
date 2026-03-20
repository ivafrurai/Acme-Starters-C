
package acme.entities.sponsorships;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface DonationRepository extends AbstractRepository {

	@Query("SELECT SUM(d.money.amount) FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	Double totalMoneyBySponsorshipId(int sponsorshipId);

	@Query("SELECT COUNT(d) FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	Long countBySponsorshipId(int sponsorshipId);

}
