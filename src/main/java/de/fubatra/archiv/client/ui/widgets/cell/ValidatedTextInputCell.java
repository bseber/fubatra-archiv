package de.fubatra.archiv.client.ui.widgets.cell;

import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.dom.client.Element;

public class ValidatedTextInputCell extends TextInputCell {

	public interface Validator {
		boolean validate(String value);
	}

	private final Validator validator;
	
	public ValidatedTextInputCell(Validator validator) {
		super();
		this.validator = validator;
	}

	@Override
	public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, String value) {
		super.setValue(context, parent, value);
		if (validator.validate(value)) {
			parent.getStyle().setBackgroundColor("red");
		} else {
			parent.getStyle().clearBackgroundColor();
		}
	}

}
