
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Spokeperson extends AbstractRole {

	// Serialisation Identifier
	private static final long	serialVersionUID	= 1L;
	// Attributes
	@Mandatory
	@ValidString
	@Column
	private String				cv;
	@Mandatory
	@ValidString
	@Column
	private String				achievements;
	@Mandatory
	//Valid
	@Column
	private boolean				licensed;
}
