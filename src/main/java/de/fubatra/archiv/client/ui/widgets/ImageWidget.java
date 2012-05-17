package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;

public class ImageWidget extends Image implements HasText, LeafValueEditor<String> {

	private String value;
	
	@Override
	public String getText() {
		return getValue();
	}
	
	@Override
	public void setText(String text) {
		setValue(text);
	}

	@Override
	public void setValue(String value) {
		this.value = value;
		if (value == null) {
			value = "";
		}
		setUrl(value);
	}

	@Override
	public String getValue() {
		return value;
	}

}
