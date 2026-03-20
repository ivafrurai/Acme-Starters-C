
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.campaigns.Campaign;
import acme.entities.campaigns.CampaignRepository;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign> {

	// Internal State
	@Autowired
	private CampaignRepository cr;
	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}
	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (campaign == null)
			result = true;
		else {
			{
				boolean uniqueCampaign;
				Campaign existingCampaign;

				existingCampaign = this.cr.findCampaignByTicker(campaign.getTicker());
				uniqueCampaign = existingCampaign == null || existingCampaign.equals(campaign);

				super.state(context, uniqueCampaign, "ticker", "acme.validation.campaign.duplicated-ticker.message");
			}
			{
				boolean hasMilestone;

				hasMilestone = Boolean.TRUE.equals(campaign.getDraftMode()) || this.cr.countMilestonesByCampaign(campaign.getId()) != 0;

				super.state(context, hasMilestone, "*", "acme.validation.campaign.milestones.error.message");
			}
			{
				Date now = MomentHelper.getBaseMoment();
				Date start = campaign.getStartMoment();
				Date end = campaign.getEndMoment();
				boolean validTime;
				boolean validPublishedCampaign;

				if (start != null && end != null)
					validTime = MomentHelper.isAfter(start, now) && MomentHelper.isAfter(end, start);
				else
					validTime = false;

				validPublishedCampaign = Boolean.TRUE.equals(campaign.getDraftMode()) || validTime;

				super.state(context, validPublishedCampaign, "startMoment", "acme.validation.campaign.dates.error.message");
			}
			result = !super.hasErrors(context);
			}

		return result;
	}
}
