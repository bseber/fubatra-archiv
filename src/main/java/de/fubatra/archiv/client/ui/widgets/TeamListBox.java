package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

import de.fubatra.archiv.shared.domain.Team;

public class TeamListBox extends Composite implements LeafValueEditor<Team>, HasChangeHandlers {
	
	private ListBox listbox;
	
	public TeamListBox() {
		listbox = new ListBox();
		for (Team t : Team.values()) {
			String name = t == Team.NONE ? "" : t.name();
			listbox.addItem(name);
		}
		initWidget(listbox);
	}
	
	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return listbox.addChangeHandler(handler);
	}

	@Override
	public void setValue(Team value) {
		int index = value == null ? 0 : value.ordinal();
		listbox.setSelectedIndex(index);
	}

	@Override
	public Team getValue() {
		return Team.values()[listbox.getSelectedIndex()];
	}

}
