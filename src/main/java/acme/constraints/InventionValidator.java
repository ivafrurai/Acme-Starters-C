
package acme.constraints;

import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.inventions.Invention;
import acme.entities.inventions.InventionRepository;
import acme.entities.inventions.Part;
import acme.entities.inventions.PartRepository;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	@Autowired
	private InventionRepository	inventionRepository;

	@Autowired
	private PartRepository		partRepository;


	@Override
	public void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (invention == null)
			result = true;
		else {
			{
				boolean uniqueInvention;
				Invention existingInvention;

				existingInvention = this.inventionRepository.findInventionByTicker(invention.getTicker());
				uniqueInvention = existingInvention == null || existingInvention.equals(invention);

				super.state(context, uniqueInvention, "ticker", "acme.validation.invention.duplicated-ticker.message");

			}
			{
				boolean atLeastOnePart = true;

				Boolean draftMode = invention.getDraftMode();
				boolean isPublished = draftMode != null && !draftMode;

				if (isPublished) {
					Collection<Part> parts = this.partRepository.findByInventionId(invention.getId());
					atLeastOnePart = parts != null && !parts.isEmpty();
				}

				super.state(context, atLeastOnePart, "*", "acme.validation.invention.no-parts.message");

			}
			{
				boolean validTimeInterval = true;

				Date start = invention.getStartMoment();
				Date end = invention.getEndMoment();

				if (start != null && end != null) {
					boolean endAfterStart = MomentHelper.isAfter(end, start);

					Boolean draftMode = invention.getDraftMode();
					boolean isDraft = draftMode != null && draftMode;

					if (isDraft)
						validTimeInterval = endAfterStart;
					else {
						Date now = MomentHelper.getCurrentMoment();
						boolean startFuture = MomentHelper.isAfter(start, now);
						boolean endFuture = MomentHelper.isAfter(end, now);
						validTimeInterval = endAfterStart && startFuture && endFuture;
					}

				}
				super.state(context, validTimeInterval, "*", "acme.validation.invention.not-valid-interval.message");
			}
			{
				boolean onlyEurosAccepted = true;

				if (invention.getId() != 0) {
					Collection<Part> parts = this.partRepository.findByInventionId(invention.getId());

					if (parts != null)
						for (Part p : parts) {
							Money cost = p.getCost();
							if (cost != null) {
								String currency = cost.getCurrency();
								if (currency == null || !"EUR".equals(currency)) {
									onlyEurosAccepted = false;
									break;
								}
							}
						}
				}
				super.state(context, onlyEurosAccepted, "*", "acme.validation.invention.not-euros.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
