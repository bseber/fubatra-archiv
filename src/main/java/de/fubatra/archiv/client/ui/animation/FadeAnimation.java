package de.fubatra.archiv.client.ui.animation;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;

public class FadeAnimation extends Animation {

	public enum Type {
		FADE_IN, FADE_OUT
	}
	
	private final Type type;
	private final Style widgetStyle;

	private boolean alive = false;
	
	public FadeAnimation(Widget widget, Type type) {
		this.widgetStyle = widget.getElement().getStyle();
		this.type = type;
	}

	public boolean isAlive() {
		return alive;
	}
	
	@Override
	protected void onComplete() {
		super.onComplete();
		alive = false;
	}
	
	@Override
	protected void onStart() {
		switch (type) {
		case FADE_IN:
			widgetStyle.setOpacity(0);
			break;
		case FADE_OUT:
			widgetStyle.setOpacity(1);
			break;
		}
		
		alive = true;
		
		super.onStart();
	}
	
	@Override
	protected void onUpdate(double progress) {
		switch (type) {
		case FADE_IN:
			widgetStyle.setOpacity(progress);
			break;
		case FADE_OUT:
			widgetStyle.setOpacity(1 - progress);
			break;
		}
	}

}
