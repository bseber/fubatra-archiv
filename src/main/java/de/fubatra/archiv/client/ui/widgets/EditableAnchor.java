package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.user.client.ui.Anchor;

public class EditableAnchor extends EditableTextBase<Anchor> {

	public EditableAnchor() {
		super(new MyAnchor());
	}
	
	private static class MyAnchor extends Anchor {
		
		public MyAnchor() {
			super();
			getElement().setAttribute("target", "_blank");
		}
		
		@Override
		public void setText(String text) {
			super.setText(text);
			if (text != null) {
				boolean http = text.startsWith("http://") || text.startsWith("https://");
				setHref(http ? text : "http://" + text);
			}
		}
		
	}
	
}
