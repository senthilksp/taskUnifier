package com.leclercb.taskunifier.gui.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.lang3.StringUtils;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.properties.events.WeakSavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.Main;

public class TaskClientList implements PropertyChangeListener, SavePropertiesListener {
	
	private EventList<String> clients;
	private static TaskClientList INSTANCE;
	
	public static TaskClientList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskClientList();
		
		return INSTANCE;
	}
	
	public TaskClientList() {
		this.clients = new BasicEventList<String>();
		
		this.initialize();
		
		Main.getUserSettings().addPropertyChangeListener(
				"plugin.synchronizer.id",
				new WeakPropertyChangeListener(Main.getUserSettings(), this));
		
		Main.getUserSettings().addSavePropertiesListener(
				new WeakSavePropertiesListener(Main.getUserSettings(), this));
	}
	
	

	public EventList<String> getClients() {
		return clients;
	}


	public void setClients(EventList<String> clients) {
		this.clients = clients;
	}
	
	


	@Override
	public void saveProperties() {
		Main.getSettings().setStringProperty(
				"taskclients",
				StringUtils.join(this.clients, ";"));
	}
		

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		this.initialize();
		
	}
	
	
	private void initialize() {
		for (String client : this.getClients()) {
			this.removeClients(client);
		}
		
		
		String[] clients = SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().getStatusValues();
		
		if (clients == null) {
			String value = Main.getSettings().getStringProperty("taskClients");
			clients = value.split(";");
		}
		
		for (String client : clients) {
			this.addClents(client);
		}
	}
	
	public void addClents(String client) {
		CheckUtils.isNotNull(client);
		
		client = client.replaceAll(";", "");
		
		if (this.clients.contains(client))
			return;
		
		this.clients.add(client);
	}
	
	public void removeClients(String client) {
		CheckUtils.isNotNull(client);
		this.clients.remove(client);
	}
	
	public EventList<String> getEventList() {
		return GlazedLists.readOnlyList(this.clients);
	}

}
