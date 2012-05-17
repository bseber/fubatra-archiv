package de.fubatra.archiv.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.shared.domain.TrainingSessionSubject;

public class TrainingSessionSubjectsEditor extends Composite implements LeafValueEditor<List<TrainingSessionSubject>> {

	private static TrainingSessionSubjectsEditorUiBinder uiBinder = GWT.create(TrainingSessionSubjectsEditorUiBinder.class);

	interface TrainingSessionSubjectsEditorUiBinder extends UiBinder<Widget, TrainingSessionSubjectsEditor> {
	}
	
	@UiField
	CheckBox ballControl;
	
	@UiField
	CheckBox dribbling;
	
	@UiField
	CheckBox feints;
	
	@UiField
	CheckBox header;
	
	@UiField
	CheckBox passing;
	
	@UiField
	CheckBox shooting;
	
	@UiField
	CheckBox offInd;
	
	@UiField
	CheckBox offGroup;
	
	@UiField
	CheckBox offTeam;
	
	@UiField
	CheckBox defInd;
	
	@UiField
	CheckBox defGroup;
	
	@UiField
	CheckBox defTeam;
	
	@UiField
	CheckBox stamina;
	
	@UiField
	CheckBox coordination;
	
	@UiField
	CheckBox power;
	
	@UiField
	CheckBox speed;
	
	public TrainingSessionSubjectsEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setValue(List<TrainingSessionSubject> value) {
		if (value == null) {
			return;
		}
		
		for (TrainingSessionSubject subject : TrainingSessionSubject.values()) {
			
			boolean check = value.contains(subject);
			
			switch (subject) {
			case BALLCONTROL:
				ballControl.setValue(check);
				break;
			case COORDINATION:
				coordination.setValue(check);
				break;
			case DEF_GROUP:
				defGroup.setValue(check);
				break;
			case DEF_INDIVIDUAL:
				defInd.setValue(check);
				break;
			case DEF_TEAM:
				defTeam.setValue(check);
				break;
			case DRIBBLING:
				dribbling.setValue(check);
				break;
			case FEINTS:
				feints.setValue(check);
				break;
			case HEADER:
				header.setValue(check);
				break;
			case OFF_GROUP:
				offGroup.setValue(check);
				break;
			case OFF_INDIVIDUAL:
				offInd.setValue(check);
				break;
			case OFF_TEAM:
				offTeam.setValue(check);
				break;
			case PASSING:
				passing.setValue(check);
				break;
			case POWER:
				power.setValue(check);
				break;
			case SHOOTING:
				shooting.setValue(check);
				break;
			case SPEED:
				speed.setValue(check);
				break;
			case STAMINA:
				stamina.setValue(check);
				break;
			}
		}
	}

	@Override
	public List<TrainingSessionSubject> getValue() {
		ArrayList<TrainingSessionSubject> list = new ArrayList<TrainingSessionSubject>();
		if (ballControl.getValue()) {
			list.add(TrainingSessionSubject.BALLCONTROL);
		}
		if (coordination.getValue()) {
			list.add(TrainingSessionSubject.COORDINATION);
		}
		if (defGroup.getValue()) {
			list.add(TrainingSessionSubject.DEF_GROUP);
		}
		if (defInd.getValue()) {
			list.add(TrainingSessionSubject.DEF_INDIVIDUAL);
		}
		if (defTeam.getValue()) {
			list.add(TrainingSessionSubject.DEF_TEAM);
		}
		if (dribbling.getValue()) {
			list.add(TrainingSessionSubject.DRIBBLING);
		}
		if (feints.getValue()) {
			list.add(TrainingSessionSubject.FEINTS);
		}
		if (header.getValue()) {
			list.add(TrainingSessionSubject.HEADER);
		}
		if (offGroup.getValue()) {
			list.add(TrainingSessionSubject.OFF_GROUP);
		}
		if (offInd.getValue()) {
			list.add(TrainingSessionSubject.OFF_INDIVIDUAL);
		}
		if (offTeam.getValue()) {
			list.add(TrainingSessionSubject.OFF_TEAM);
		}
		if (passing.getValue()) {
			list.add(TrainingSessionSubject.PASSING);
		}
		if (power.getValue()) {
			list.add(TrainingSessionSubject.POWER);
		}
		if (shooting.getValue()) {
			list.add(TrainingSessionSubject.SHOOTING);
		}
		if (speed.getValue()) {
			list.add(TrainingSessionSubject.SPEED);
		}
		if (stamina.getValue()) {
			list.add(TrainingSessionSubject.STAMINA);
		}
		return list;
	}

}
