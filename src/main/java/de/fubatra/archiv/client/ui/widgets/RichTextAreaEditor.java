package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.RichTextArea;

public class RichTextAreaEditor extends RichTextArea implements LeafValueEditor<String> {

	@Override
	public void setValue(String value) {
		if (value == null) {
			value = "";
		}
		this.setHTML(value);
	}

	@Override
	public String getValue() {
		return getHTML();
	}

}
