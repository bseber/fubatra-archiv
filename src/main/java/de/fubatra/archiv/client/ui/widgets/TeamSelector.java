package de.fubatra.archiv.client.ui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.client.ui.resources.AppResources;
import de.fubatra.archiv.shared.domain.Team;

public class TeamSelector extends Composite implements HasEditorErrors<List<Team>>, LeafValueEditor<List<Team>> {
	
	private static TeamSelectorUiBinder uiBinder = GWT.create(TeamSelectorUiBinder.class);

	interface TeamSelectorUiBinder extends UiBinder<Widget, TeamSelector> {
	}
	
	@UiField
	CheckBox btnSeniors;
	
	@UiField
	CheckBox btnAjun;
	
	@UiField
	CheckBox btnBjun;
	
	@UiField
	CheckBox btnCjun;
	
	@UiField
	CheckBox btnDjun;
	
	@UiField
	CheckBox btnEjun;
	
	@UiField
	CheckBox btnFjun;
	
	public TeamSelector() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setValue(List<Team> value) {
		if (value == null) {
			return;
		}
		if (value.contains(Team.U19) || value.contains(Team.U18)) {
			btnSeniors.setValue(true);
		} else if (value.contains(Team.U19) || value.contains(Team.U18)) {
			btnAjun.setValue(true);
		} else if (value.contains(Team.U17) || value.contains(Team.U16)) {
			btnBjun.setValue(true);
		} else if (value.contains(Team.U15) || value.contains(Team.U14)) {
			btnCjun.setValue(true);
		} else if (value.contains(Team.U13) || value.contains(Team.U12)) {
			btnDjun.setValue(true);
		} else if (value.contains(Team.U11) || value.contains(Team.U10)) {
			btnEjun.setValue(true);
		} else if (value.contains(Team.U9) || value.contains(Team.U8)) {
			btnFjun.setValue(true);
		}
	}

	@Override
	public List<Team> getValue() {
		List<Team> result = new ArrayList<Team>();
		if (btnSeniors.getValue()) {
			result.add(Team.Senioren);
			result.add(Team.U23);
		}
		if (btnAjun.getValue()) {
			result.add(Team.U19);
			result.add(Team.U18);
		}
		if (btnBjun.getValue()) {
			result.add(Team.U17);
			result.add(Team.U16);
		}
		if (btnCjun.getValue()) {
			result.add(Team.U15);
			result.add(Team.U14);
		}
		if (btnDjun.getValue()) {
			result.add(Team.U13);
			result.add(Team.U12);
		}
		if (btnEjun.getValue()) {
			result.add(Team.U11);
			result.add(Team.U10);
		}
		if (btnFjun.getValue()) {
			result.add(Team.U9);
			result.add(Team.U8);
		}
		return result;
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		if (errors.isEmpty()) {
			getElement().getStyle().clearBackgroundColor();
		} else {
			getElement().getStyle().setBackgroundColor(AppResources.ERROR_BG_COLOR);
		}
	}

}
