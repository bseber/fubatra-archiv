package de.fubatra.archiv.client.ui.widgets.filter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

import de.fubatra.archiv.client.events.HasTrainingFilterChangedEventHandlers;
import de.fubatra.archiv.client.events.TrainingFilterChangedEvent;
import de.fubatra.archiv.shared.domain.TrainingSessionSubject;

public class TrainingSessionSubjectFilter extends Composite implements HasTrainingFilterChangedEventHandlers {

	private static TrainingSessionSubjectFilterUiBinder uiBinder = GWT.create(TrainingSessionSubjectFilterUiBinder.class);

	interface TrainingSessionSubjectFilterUiBinder extends UiBinder<Widget, TrainingSessionSubjectFilter> {
	}

	@UiField
	CheckBox cbBallcontrol;

	@UiField
	CheckBox cbDribbling;

	@UiField
	CheckBox cbFeints;

	@UiField
	CheckBox cbHeader;

	@UiField
	CheckBox cbPassing;

	@UiField
	CheckBox cbShoot;

	@UiField
	CheckBox cbOffIndividual;

	@UiField
	CheckBox cbOffGroup;

	@UiField
	CheckBox cbOffTeam;

	@UiField
	CheckBox cbDefIndividual;

	@UiField
	CheckBox cbDefGroup;

	@UiField
	CheckBox cbDefTeam;

	@UiField
	CheckBox cbStamina;

	@UiField
	CheckBox cbCoordination;

	@UiField
	CheckBox cbPower;

	@UiField
	CheckBox cbSpeed;

	public TrainingSessionSubjectFilter() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addTrainingFilterChangedHandler(TrainingFilterChangedEvent.Handler handler) {
		return addHandler(handler, TrainingFilterChangedEvent.TYPE);
	}

	public List<TrainingSessionSubject> getValue() {
		ArrayList<TrainingSessionSubject> list = new ArrayList<TrainingSessionSubject>();
		if (cbBallcontrol.getValue()) {
			list.add(TrainingSessionSubject.BALLCONTROL);
		}
		if (cbDribbling.getValue()) {
			list.add(TrainingSessionSubject.DRIBBLING);
		}
		if (cbFeints.getValue()) {
			list.add(TrainingSessionSubject.FEINTS);
		}
		if (cbHeader.getValue()) {
			list.add(TrainingSessionSubject.HEADER);
		}
		if (cbPassing.getValue()) {
			list.add(TrainingSessionSubject.PASSING);
		}
		if (cbShoot.getValue()) {
			list.add(TrainingSessionSubject.SHOOTING);
		}
		if (cbOffIndividual.getValue()) {
			list.add(TrainingSessionSubject.OFF_INDIVIDUAL);
		}
		if (cbOffGroup.getValue()) {
			list.add(TrainingSessionSubject.OFF_GROUP);
		}
		if (cbOffTeam.getValue()) {
			list.add(TrainingSessionSubject.OFF_TEAM);
		}
		if (cbDefIndividual.getValue()) {
			list.add(TrainingSessionSubject.DEF_INDIVIDUAL);
		}
		if (cbDefGroup.getValue()) {
			list.add(TrainingSessionSubject.DEF_GROUP);
		}
		if (cbDefTeam.getValue()) {
			list.add(TrainingSessionSubject.DEF_TEAM);
		}
		if (cbStamina.getValue()) {
			list.add(TrainingSessionSubject.STAMINA);
		}
		if (cbCoordination.getValue()) {
			list.add(TrainingSessionSubject.COORDINATION);
		}
		if (cbPower.getValue()) {
			list.add(TrainingSessionSubject.POWER);
		}
		if (cbSpeed.getValue()) {
			list.add(TrainingSessionSubject.SPEED);
		}
		return list;
	}

	@UiHandler({ "cbBallcontrol", "cbDribbling", "cbFeints", "cbHeader", "cbPassing", "cbShoot", "cbOffIndividual", "cbOffGroup", "cbOffTeam",
			"cbDefIndividual", "cbDefGroup", "cbDefTeam", "cbStamina", "cbCoordination", "cbPower", "cbSpeed" })
	void onValueChange(ValueChangeEvent<Boolean> event) {
		TrainingFilterChangedEvent.fire(this, new TrainingFilterChangedEvent());
	}

}
