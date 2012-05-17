package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import de.fubatra.archiv.shared.domain.TrainingSessionVisiblity;

public class TrainingSessionVisiblityEditor extends Composite implements LeafValueEditor<TrainingSessionVisiblity> {

	private static TrainingSessionVisiblityEditorUiBinder uiBinder = GWT.create(TrainingSessionVisiblityEditorUiBinder.class);

	interface TrainingSessionVisiblityEditorUiBinder extends UiBinder<Widget, TrainingSessionVisiblityEditor> {
	}

	@UiField
	RadioButton btnPublic;
	
	@UiField
	RadioButton btnPrivate;
	
	public TrainingSessionVisiblityEditor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void setValue(TrainingSessionVisiblity value) {
		if (value == null || value == TrainingSessionVisiblity.PUBLIC) {
			btnPublic.setValue(true);
		} else {
			btnPrivate.setValue(true);
		}
	}

	@Override
	public TrainingSessionVisiblity getValue() {
		if (btnPrivate.getValue()) {
			return TrainingSessionVisiblity.PRIVATE;
		}
		return TrainingSessionVisiblity.PUBLIC;
	}

}
