package com.leclercb.taskunifier.gui.commons.editors;



import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventComboBoxModel;

import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.TaskClientList;
import com.leclercb.taskunifier.gui.utils.TaskStatusList;

public class ClientsEditor extends ComboBoxCellEditor {
	
	
	public ClientsEditor() {
		super(ComponentFactory.createTaskClientComboBox(
				new EventComboBoxModel<String>(new SortedList<String>(
						TaskClientList.getInstance().getEventList())),
				true));
	}

}
