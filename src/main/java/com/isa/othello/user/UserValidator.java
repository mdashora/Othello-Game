
package com.isa.othello.user;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *A utility class for USER.
 * @author Bhavana
 */
@FacesValidator("othello.user.validator")
public class UserValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String name = (String) value;
        if(UserService.exists(name))
        {
            FacesMessage msg = 
		new FacesMessage("User exists.", 
						"This User exists already. Choose a new name!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
    
}
