package de.fubatra.archiv.client.ui.widgets.filter;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

import de.fubatra.archiv.client.events.HasTrainingFilterChangedEventHandlers;
import de.fubatra.archiv.client.events.TrainingFilterChangedEvent;
import de.fubatra.archiv.shared.domain.Team;

public class TeamFilter extends Composite implements HasTrainingFilterChangedEventHandlers {

	private static TeamFilterUiBinder uiBinder = GWT.create(TeamFilterUiBinder.class);

	interface TeamFilterUiBinder extends UiBinder<Widget, TeamFilter> {
	}

	@UiField
	RadioButton btnAll;
	
	@UiField
	RadioButton btnSeniors;
	
	@UiField
	RadioButton btnAJun;
	
	@UiField
	RadioButton btnBJun;
	
	@UiField
	RadioButton btnCJun;
	
	@UiField
	RadioButton btnDJun;
	
	@UiField
	RadioButton btnEJun;
	
	@UiField
	RadioButton btnFJun;
	
	public TeamFilter() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addTrainingFilterChangedHandler(TrainingFilterChangedEvent.Handler handler) {
		return addHandler(handler, TrainingFilterChangedEvent.TYPE);
	}

	public List<Team> getValue() {
		if (btnAll.getValue()) {
			return Arrays.asList(Team.values());
		}
		if (btnSeniors.getValue()) {
			return Arrays.asList(Team.Senioren, Team.U23);
		}
		if (btnAJun.getValue()) {
			return Arrays.asList(Team.U19, Team.U18);
		}
		if (btnBJun.getValue()) {
			return Arrays.asList(Team.U17, Team.U16);
		}
		if (btnCJun.getValue()) {
			return Arrays.asList(Team.U15, Team.U14);
		}
		if (btnDJun.getValue()) {
			return Arrays.asList(Team.U13, Team.U12);
		}
		if (btnEJun.getValue()) {
			return Arrays.asList(Team.U11, Team.U10);
		}
		if (btnFJun.getValue()) {
			return Arrays.asList(Team.U9, Team.U8);
		}
		return null;
	}
	
	@UiHandler({"btnAll", "btnSeniors", "btnAJun","btnBJun", "btnCJun", "btnDJun", "btnEJun", "btnFJun"})
	void onValueChanged(ValueChangeEvent<Boolean> event) {
		TrainingFilterChangedEvent.fire(this, new TrainingFilterChangedEvent());
	}

}
