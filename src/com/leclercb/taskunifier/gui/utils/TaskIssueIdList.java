package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.lang3.StringUtils;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.WeakSavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;

public class TaskIssueIdList implements PropertyChangeListener, SavePropertiesListener{
	
	private EventList<String> issueIds;
	public EventList<String> getTaskIds() {
		return issueIds;
	}

	public void setTaskIds(EventList<String> issueIds) {
		this.issueIds = issueIds;
	}

	private static TaskIssueIdList INSTANCE;
	
	public static TaskIssueIdList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskIssueIdList();
		
		return INSTANCE;
	}
	
	public TaskIssueIdList() {
		this.issueIds = new BasicEventList<String>();
		
		this.initialize();
		
		Main.getUserSettings().addPropertyChangeListener(
				"plugin.synchronizer.id",
				new WeakPropertyChangeListener(Main.getUserSettings(), this));
		
		Main.getUserSettings().addSavePropertiesListener(
				new WeakSavePropertiesListener(Main.getUserSettings(), this));
	}
	
	private void initialize() {
		for (String issueId : this.getTaskIds()) {
			this.removeIssueIds(issueId);
		}
		
		
		String[] issueIds = SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getStatusValues();
		
		if (issueIds == null) {
			String value = Main.getSettings().getStringProperty("taskIssueIds");
			issueIds = value.split(";");
		}
		
		for (String issueId : issueIds) {
			this.addIssueId(issueId);
		}
	}

	private void addIssueId(String issueId) {
		CheckUtils.isNotNull(issueId);
		
		issueId = issueId.replaceAll(";", "");
		
		if (this.issueIds.contains(issueId))
			return;
		
		this.issueIds.add(issueId);
		
	}

	private void removeIssueIds(String issueId) {
		CheckUtils.isNotNull(issueId);
		this.issueIds.remove(issueId);
		
	}

	@Override
	public void saveProperties() {
		Main.getSettings().setStringProperty(
				"taskissueIds",
				StringUtils.join(this.issueIds, ";"));
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
