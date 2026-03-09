
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

public class SponsorshipValidator extends AbstractValidator<ValidSponsorship, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorshipRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidSponsorship annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Sponsorship sponsorship, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (sponsorship == null)
			result = true;

		else {
			{
				boolean uniqueSponsorship;
				Sponsorship existingSponsorship;

				existingSponsorship = this.repository.findSponsorshipByTicker(sponsorship.getTicker());
				uniqueSponsorship = existingSponsorship == null || existingSponsorship.equals(sponsorship);

				super.state(context, uniqueSponsorship, "ticker", "acme.validation.sponsorship.duplicated-ticker.message");
			}
			{
				if (!sponsorship.getDraftMode()) {

					boolean correctDate;
					correctDate = sponsorship.getEndMoment().after(sponsorship.getStartMoment());

					super.state(context, correctDate, "endMoment", "acme.validation.sponsorship.invalid-date.message");

					boolean atLeastOneDonation = true;
					int existingDonations;

					existingDonations = this.repository.findDonationsSizeBySponsorshipId(sponsorship.getId());
					atLeastOneDonation = existingDonations >= 1;

					super.state(context, atLeastOneDonation, "*", "acme.validation.sponsorship.missing-donations.message");
				}
				result = !super.hasErrors(context);
			}

		}
		return result;
	}
}
