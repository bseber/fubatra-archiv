package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Hyperlink;

/**
 * A Hyperlink with a possible image on the left
 */
public class ImageHyperlink extends Hyperlink {

	@UiConstructor
	public ImageHyperlink(ImageResource image) {
		setImage(image);
	}
	
	public void setImage(ImageResource resource) {
		String span = getSpanOrNull(getHTML());
		String image = AbstractImagePrototype.create(resource).getHTML();
		image = image.replace("style='", "style='float:left;");
		setHTML(span == null ? image : image + span);
	}
	
	@Override
	public void setText(String text) {
		String image = getImageOrNull(getHTML());
		String span = "<div style='display:inline-block'>" + text + "</div>";
		setHTML(image == null ? span : image + span);
	}
	
	private String getSpanOrNull(String html) {
		int begin = html.indexOf("<span>");
		if (begin > -1) {
			int end = html.indexOf("</span>") + 7;
			return html.substring(begin, end);
		}
		return null;
	}
	
	private String getImageOrNull(String html) {
		int begin = html.indexOf("<img");
		if (begin > -1) {
			int end = html.indexOf(">") + 1;
			return html.substring(begin, end);
		}
		return null;
	}

}
