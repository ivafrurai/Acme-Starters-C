/*
 * SpokespersonCampaignController.java
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

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.campaigns.Campaign;
import acme.realms.Spokesperson;

@Controller
public class SpokespersonCampaignController extends AbstractController<Spokesperson, Campaign> {

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", SpokespersonCampaignListService.class);
		super.addBasicCommand("show", SpokespersonCampaignShowService.class);
		super.addBasicCommand("create", SpokespersonCampaignCreateService.class);
		super.addBasicCommand("update", SpokespersonCampaignUpdateService.class);
		super.addBasicCommand("delete", SpokespersonCampaignDeleteService.class);

		super.addCustomCommand("publish", "update", SpokespersonCampaignPublishService.class);
	}

}

