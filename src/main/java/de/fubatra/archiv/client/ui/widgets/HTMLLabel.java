package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class HTMLLabel extends Composite implements LeafValueEditor<String> {

	private HTML html;
	
	public HTMLLabel() {
		html = new HTML();
		initWidget(html);
	}
	
	@Override
	public void setValue(String value) {
		if (value == null) {
			value = "";
		}
		html.setHTML(value);
	}

	@Override
	public String getValue() {
		return html.getHTML();
	}
	
}
