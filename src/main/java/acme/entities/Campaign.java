
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.realms.Spokeperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Campaign extends AbstractEntity {

	// Serialisation Identifier
	private static final long	serialVersionUID	= 1L;
	// Attributes
	@Mandatory
	//@ValidTicker 
	@Column(unique = true)
	private String				ticker;
	@Mandatory
	@ValidString
	@Column
	private String				name;
	@Mandatory
	//@ValidText
	@Column
	private String				description;
	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startMoment;
	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endMoment;
	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;
	@Mandatory
	@Column
	private boolean				draftMode;
	//Derived Attributes
	/*
	 * @Valid
	 * 
	 * @Transient
	 * public Double getMonthsActive() {
	 * return (double) (this.startMoment.getMonth() - this.endMoment.getMonth());
	 * }
	 * 
	 * @ValidNumber
	 * 
	 * @Transient
	 * public Double effort() {
	 * }
	 */
	//Relationships
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Spokeperson			spokeperson;

}
