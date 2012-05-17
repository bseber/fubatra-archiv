package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import de.fubatra.archiv.shared.domain.Team;

public class EditableTeamListBox extends Composite implements LeafValueEditor<Team> {

	private SimplePanel container;
	private Label label;
	private TeamListBox listBox;
	
	private Team value;
	
	public EditableTeamListBox() {
		listBox = new TeamListBox();
		label = new Label();
		container = new SimplePanel();
		container.add(label);
		initWidget(container);
		
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				listBox.getValue();
			}
		});
	}
	
	/**
	 * Sets the original value and calls {@link #setEditable(boolean)} with false
	 */
	public void reset() {
		label.setText(value.name());
		setEditable(false);
	}
	
	public void setEditable(boolean editable) {
		container.clear();
		if (editable) {
			container.add(listBox);
		} else {
			container.add(label);
		}
	}

	@Override
	public void setValue(Team value) {
		if (value == null) {
			value = Team.NONE;
		}
		label.setText(value == Team.NONE ? "" : value.name());
		listBox.setValue(value);
		this.value = value;
	}

	@Override
	public Team getValue() {
		return listBox.getValue();
	}

}
