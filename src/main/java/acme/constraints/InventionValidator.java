
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.inventions.Invention;
import acme.entities.inventions.InventionRepository;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	@Autowired
	private InventionRepository inventionRepository;


	@Override
	public void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {
		return false;
	}

}
