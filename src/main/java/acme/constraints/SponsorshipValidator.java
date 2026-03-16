
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.sponsorships.DonationRepository;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

@Validator
public class SponsorshipValidator extends AbstractValidator<ValidSponsorship, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorshipRepository	repository;

	@Autowired
	private DonationRepository		donationRepository;

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

			// Ticker único
			{
				boolean uniqueSponsorship;
				Sponsorship existingSponsorship;

				existingSponsorship = this.repository.findSponsorshipByTicker(sponsorship.getTicker());
				uniqueSponsorship = existingSponsorship == null || existingSponsorship.equals(sponsorship);

				super.state(context, uniqueSponsorship, "ticker", "acme.validation.sponsorship.duplicated-ticker.message");
			}

			{
				boolean eurCurrency;

				eurCurrency = Boolean.TRUE.equals(sponsorship.getDraftMode()) || this.repository.findDonationsBySponsorShipId(sponsorship.getId()).stream().allMatch(d -> "EUR".equals(d.getMoney().getCurrency()));

				super.state(context, eurCurrency, "money.currency", "acme.validation.sponsorShip.eur-currency.message");
			}
			if (!sponsorship.getDraftMode()) {

				Integer donationsCount = this.repository.findDonationsSizeBySponsorshipId(sponsorship.getId());
				boolean hasDonations = donationsCount != null && donationsCount >= 1;

				super.state(context, hasDonations, "draftMode", "acme.validation.sponsorship.donations.error");

				Date start = sponsorship.getStartMoment();
				Date end = sponsorship.getEndMoment();

				boolean validDates = start != null && end != null && MomentHelper.isAfter(end, start);

				super.state(context, validDates, "startMoment", "acme.validation.sponsorship.dates.error");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}
}
