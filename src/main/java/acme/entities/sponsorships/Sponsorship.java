
package acme.entities.sponsorships;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidHeader;
import acme.constraints.ValidSponsorship;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidSponsorship
public class Sponsorship extends AbstractEntity {

	//Serialisation version 

	private static final long		serialVersionUID	= 1L;

	//Attributes

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String					ticker;

	@Mandatory
	@ValidHeader
	@Column
	private String					name;

	@Mandatory
	@ValidText
	@Column
	private String					description;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					startMoment;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					endMoment;

	@Optional
	@ValidUrl
	@Column
	private String					moreInfo;

	@Mandatory
	@Valid
	@Column
	private Boolean					draftMode;

	// Derived attributes -----------------------------------------------------

	@Transient
	@Autowired
	private SponsorshipRepository	sponsorshipRepository;


	@Transient
	@Valid
	public Double monthsActive() {
		double result = 0.0;
		if (this.startMoment != null && this.endMoment != null) {
			long diff = this.endMoment.getTime() - this.startMoment.getTime();
			result = diff / (1000.0 * 60 * 60 * 24 * 30.44);
			result = Math.round(result * 10.0) / 10.0;
		}
		return result;
	}

	@Transient
	public Money getTotalMoney() {
		Money totalMoney = new Money();
		totalMoney.setCurrency("EUR");

		Double res;
		res = this.sponsorshipRepository.totalMoney(this.getId());

		if (res == null)
			totalMoney.setAmount(0.0);
		else
			totalMoney.setAmount(res);
		return totalMoney;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Sponsor sponsor;

}
