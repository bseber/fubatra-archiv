package de.fubatra.archiv.client.ui.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.LazyView;
import com.mvp4g.client.view.ReverseViewInterface;

public class FeedbackView extends Composite implements LazyView, ReverseViewInterface<FeedbackView.IPresenter> {

	private static FeedbackViewUiBinder uiBinder = GWT.create(FeedbackViewUiBinder.class);

	public interface IPresenter {

		void onCancelClicked();

		void onSendFeedbackClicked();
		
	}
	
	interface FeedbackViewUiBinder extends UiBinder<Widget, FeedbackView> {
	}

	@UiField
	HTMLPanel form;
	
	@UiField
	TextBox email;
	
	@UiField
	TextArea message;
	
	private IPresenter presenter;

	@Override
	public void createView() {
		initWidget(uiBinder.createAndBindUi(this));
		form.setVisible(false);
		message.getElement().setPropertyString("placeholder", "Einen Fehler gefunden? Ein Feature vermisst? Oder einfach nur Lobeshymnen :-)");
	}
	
	public void setUserEmail(String email) {
		this.email.setText(email);
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IPresenter getPresenter() {
		return presenter;
	}
	
	public HasText getMessage() {
		return message;
	}
	
	/**
	 * Resets the message field and hides the form
	 */
	public void resetAndHide() {
		message.setText("");
		form.setVisible(false);
	}
	
	@UiHandler("btnCancel")
	void onCancelClicked(ClickEvent event) {
		presenter.onCancelClicked();
	}
	
	@UiHandler("btnFeedback")
	void onFeedbackClicked(ClickEvent event) {
		boolean visible = form.isVisible();
		if (visible) {
			resetAndHide();
		} else {
			form.setVisible(true);
		}
	}
	
	@UiHandler("btnSend")
	void onSendClicked(ClickEvent event) {
		presenter.onSendFeedbackClicked();
	}
	
}
