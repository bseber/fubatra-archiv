package de.fubatra.archiv.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import de.fubatra.archiv.client.ioc.AppEventBus;
import de.fubatra.archiv.client.ui.view.MainView;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.IView, AppEventBus> {


	public interface IView extends IsWidget {
		
		SimplePanel getMenuContainer();

		void setContent(IsWidget widget);
	}

	public void onDisplayApplication() {
		RootPanel.get("ajax-loader").getElement().removeFromParent();
		RootLayoutPanel.get().add(view);
	}
	
	public void onEntityNotFound() {
		Label label = new Label("Uuuups... Gewünschtes Item konnte nicht gefunden werden. ");
		Anchor anchor = new Anchor("Zurück");
		anchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.back();
			}
		});
		
		HorizontalPanel panel = new HorizontalPanel(); 
		panel.add(label);
		panel.add(anchor);
		
		view.setContent(panel);
	}
	
	public void onSetContent(IsWidget widget) {
		view.setContent(widget);
	}
	
	public void onStart() {
		eventBus.initMenuView(view.getMenuContainer());
	}
	
	public void onHideInitialLoadingIndicator() {
	}

}
