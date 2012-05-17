package de.fubatra.archiv.client.ui.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class EditableTextBase<T extends Widget & HasText> extends Composite implements HasAllFocusHandlers, LeafValueEditor<String> {

	private SimplePanel container;
	private TextBox textBox;
	
	private boolean editable;
	private final T element;
	
	protected EditableTextBase(T element) {
		this.element = element;
		textBox = new TextBox();
		container = new SimplePanel();
		container.add(element);
		initWidget(container);
		
		element.getElement().getStyle().setProperty("padding", "5px 4px");
	}
	
	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return textBox.addFocusHandler(handler);
	}
	
	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return textBox.addBlurHandler(handler);
	}
	
	public boolean isChanged() {
		return element.getText().equals(textBox.getText());
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
		container.clear();
		container.add(editable ? textBox : element);
	}
	
	@Override
	public void setValue(String value) {
		element.setText(value);
		textBox.setText(value);
	}

	@Override
	public String getValue() {
		return editable ? textBox.getValue() : element.getText();
	}
	
	/**
	 * Sets the original value and calls {@link #setEditable(boolean)} with false
	 */
	public void reset() {
		textBox.setValue(element.getText());
		setEditable(false);
	}
	
}
