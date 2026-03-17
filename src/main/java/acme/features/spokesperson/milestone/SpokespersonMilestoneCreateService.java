/*
 * SpokespersonMilestoneCreateService.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.spokesperson.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.MilestoneKind;
import acme.entities.campaigns.Campaign;
import acme.entities.campaigns.Milestone;
import acme.realms.Spokesperson;

@Service
public class SpokespersonMilestoneCreateService extends AbstractService<Spokesperson, Milestone> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SpokespersonMilestoneRepository	repository;

	private Milestone						milestone;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int campaignId;
		Campaign campaign;

		campaignId = super.getRequest().getData("campaignId", int.class);
		campaign = this.repository.findCampaignById(campaignId);

		this.milestone = super.newObject(Milestone.class);
		this.milestone.setTitle("");
		this.milestone.setAchievements("");
		this.milestone.setEffort(0.01);
		this.milestone.setKind(MilestoneKind.TEASER);
		this.milestone.setCampaign(campaign);
	}

	@Override
	public void authorise() {
		boolean status;
		int campaignId;
		Campaign campaign;

		campaignId = super.getRequest().getData("campaignId", int.class);
		campaign = this.repository.findCampaignById(campaignId);
		status = campaign != null && Boolean.TRUE.equals(campaign.getDraftMode()) && campaign.getSpokesperson().isPrincipal();

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.milestone, "title", "achievements", "effort", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.milestone);
	}

	@Override
	public void execute() {
		this.repository.save(this.milestone);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(MilestoneKind.class, this.milestone.getKind());

		tuple = super.unbindObject(this.milestone, "title", "achievements", "effort", "kind", "campaign.ticker");
		tuple.put("campaignId", this.milestone.getCampaign().getId());
		tuple.put("draftMode", this.milestone.getCampaign().getDraftMode());
		tuple.put("kinds", choices);
	}

}

