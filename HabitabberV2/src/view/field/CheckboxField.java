package view.field;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class CheckboxField extends BasicField {

	private CheckBox checkbox;
	
	public CheckboxField(String name) {
		this(name, "");
	}
	
	public CheckboxField(String name, String value) {
		super(name, value);
		CheckBox checkBox = new CheckBox();
		if (value != null && !value.isEmpty())
			checkBox.setSelected(value.substring(0,1).toLowerCase().equals("y"));
		else {
			checkBox.setSelected(true);
		}
		setCheckbox(checkBox);
	}
	
	/**
	 * @return the textField
	 */
	@Override
	public TextField getTextField() {
		if (getCheckbox().isSelected()) {
			super.getTextField().setText("v");
		} else {
			super.getTextField().setText(null);
		}
		return super.getTextField();
	}

	/**
	 * @param textField the textField to set
	 */
	@Override
	public void setTextField(TextField textField) {		
		if (getCheckbox() != null) {
			if (textField.getText() != null && !textField.getText().isEmpty()) {
				getCheckbox().setSelected(true);
			} else {
				getCheckbox().setSelected(false);
			}
		}
		super.setTextField(textField);
	}

	/**
	 * @return the checkbox
	 */
	public CheckBox getCheckbox() {
		return checkbox;
	}

	/**
	 * @param checkbox the checkbox to set
	 */
	public void setCheckbox(CheckBox checkbox) {
		this.checkbox = checkbox;		
	}

}

