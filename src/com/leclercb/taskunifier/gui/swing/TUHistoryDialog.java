package com.leclercb.taskunifier.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventComboBoxModel;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumnList;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.TaskStatusList;
import com.toedter.calendar.JDateChooser;

import javax.swing.JPanel;
//senthil

public class TUHistoryDialog  extends TUDialog{

	private JTextField textFieldComments = new JTextField();
	private JComboBox comboStatus;
	private JDateChooser textFieldDate = new JDateChooser();
	private JTextField textFieldHoursWorked = new JTextField();
	//private JTextField bugTrackNumber = new JTextField();
			
	public JTextField getTextFieldComments() {
		return textFieldComments;
	}


	public void setTextFieldComments(JTextField textFieldComments) {
		this.textFieldComments = textFieldComments;
	}

	public JComboBox getComboStatus() {
		return comboStatus;
	}



	public void setComboStatus(JComboBox comboStatus) {
		this.comboStatus = comboStatus;
	}



	public JDateChooser getTextFieldDate() {
		return textFieldDate;
	}



	public void setTextFieldDate(JDateChooser textFieldDate) {
		this.textFieldDate = textFieldDate;
	}



	public JTextField getTextFieldHoursWorked() {
		return textFieldHoursWorked;
	}
	public void setTextFieldHoursWorked(JTextField textFieldHoursWorked) {
		this.textFieldHoursWorked = textFieldHoursWorked;
	}

	/*public JTextField getBugTrackNumber() {
		return bugTrackNumber;
	}
	public void setBugTrackNumber(JTextField bugTrackNumber) {
		this.bugTrackNumber = bugTrackNumber;
	}*/

	private boolean cancelled;
	
	public TUHistoryDialog(boolean open, String title) {
		this.initialize(open, title);
	}
	
	
	
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			this.setCancelled(false);
			this.setLocationRelativeTo(FrameUtils.getCurrentWindow());
		}
		
		super.setVisible(visible);
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	private void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	private void initialize(boolean open, String title) {
		this.setModal(true);
		this.setTitle(title);
		this.setSize(600, 250);
		this.setResizable(true);
		//this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
				
				JPanel rightPanel = new JPanel();
				rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				rightPanel.setLayout(new BorderLayout());
				
				FormBuilder builder = new FormBuilder(
						"right:pref, 4dlu, fill:default:grow");
				this.comboStatus = ComponentFactory.createTaskStatusComboBox(null, true);
				
				// History Comment
				builder.appendI15d("general.history.comment", true, this.textFieldComments);
				// Task Status
				EventList<String> eventList = new SortedList<String>(
						TaskStatusList.getInstance().getEventList());
				
				this.comboStatus.setModel(new EventComboBoxModel<String>(eventList));
				builder.appendI15d("general.history.status", true,this.comboStatus);
				builder.appendI15d("general.history.date", true, this.textFieldDate);
				builder.appendI15d("general.history.hoursworked", true,this.textFieldHoursWorked);
				//builder.appendI15d("general.history.bugtrackno", true, this.bugTrackNumber);
				
				// Lay out the panel
				rightPanel.add(builder.getPanel(), BorderLayout.CENTER);
				this.add(rightPanel, BorderLayout.NORTH);
				
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand().equals("OK")) {
					TUHistoryDialog.this.setCancelled(false);
					TUHistoryDialog.this.setVisible(false);
				}
				
				if (event.getActionCommand().equals("CANCEL")) {
					TUHistoryDialog.this.setCancelled(true);
					TUHistoryDialog.this.setVisible(false);
				}
			}
			
		};
		
		JButton okButton = new TUOkButton(listener);
		JButton cancelButton = new TUCancelButton(listener);
		
		JPanel panel = new TUButtonsPanel(okButton, cancelButton);
		
		this.add(panel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(okButton);
	}

}
