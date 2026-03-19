/*
 * SpokespersonCampaignPublishService.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.spokesperson.campaign;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.client.helpers.MomentHelper;
import acme.entities.campaigns.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignPublishService extends AbstractService<Spokesperson, Campaign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.campaign != null && //
			Boolean.TRUE.equals(this.campaign.getDraftMode()) && this.campaign.getSpokesperson().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.campaign);

		{
			boolean hasMilestone;

			hasMilestone = !this.repository.findMilestonesByCampaignId(this.campaign.getId()).isEmpty();
			super.state(hasMilestone, "*", "acme.validation.campaign.milestones.error.message");
		}
		{
			Date now = MomentHelper.getBaseMoment();
			Date start = this.campaign.getStartMoment();
			Date end = this.campaign.getEndMoment();
			boolean validTime;

			if (start != null && end != null)
				validTime = MomentHelper.isAfter(start, now) && MomentHelper.isAfter(end, start);
			else
				validTime = false;

			super.state(validTime, "startMoment", "acme.validation.campaign.dates.error.message");
		}
	}

	@Override
	public void execute() {
		this.campaign.setDraftMode(false);
		this.repository.save(this.campaign);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive", "effort");
	}

}
