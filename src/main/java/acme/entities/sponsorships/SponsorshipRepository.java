
package acme.entities.sponsorships;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorshipRepository extends AbstractRepository {

	@Query("select sum(d.money.amount) from Donation d where d.sponsorship.id = :sponsorshipId")
	Double totalMoney(int sponsorshipId);

	@Query("select s from Sponsorship s where s.ticker = :ticker")
	Sponsorship findSponsorshipByTicker(String ticker);

	@Query("SELECT COUNT(d) from Donation d where d.sponsorship.id = :sponsorshipId")
	int findDonationsSizeBySponsorshipId(int sponsorshipId);;

	@Query("SELECT d FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	List<Donation> findDonationsBySponsorShipId(int sponsorshipId);
}
