package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.client.presenter.MainPresenter;
import de.fubatra.archiv.client.ui.animation.FadeAnimation;
import de.fubatra.archiv.client.ui.animation.FadeAnimation.Type;

public class MainView extends Composite implements MainPresenter.IView {

	private static final int FADE_ANIMATION_TIME = 500;

	private static MainViewUiBinder uiBinder = GWT.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {
	}

	@UiField
	SimplePanel menuContainer;
	
	@UiField
	ScrollPanel centerContainer;
	
	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public SimplePanel getMenuContainer() {
		return menuContainer;
	}
	
	@Override
	public void setContent(final IsWidget widget) {
		
		Widget currentWidget = centerContainer.getWidget();
		
		if (currentWidget == null) {
			centerContainer.add(widget);
			new FadeAnimation((Widget)widget, Type.FADE_IN).run(FADE_ANIMATION_TIME);
		} else {
			new FadeAnimation(currentWidget, Type.FADE_OUT) {
				@Override
				protected void onComplete() {
					super.onComplete();
					centerContainer.clear();
					centerContainer.add(widget);
					new FadeAnimation((Widget)widget, Type.FADE_IN).run(FADE_ANIMATION_TIME);
				}
			}.run(FADE_ANIMATION_TIME);
		}
	}
	
}
