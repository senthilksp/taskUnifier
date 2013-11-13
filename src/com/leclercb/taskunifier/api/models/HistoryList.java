package com.leclercb.taskunifier.api.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.ListChangeSupport;
import com.leclercb.commons.api.event.listchange.ListChangeSupported;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.FileList.FileItem;
import com.leclercb.taskunifier.api.models.HistoryList.HistoryItem;
import com.leclercb.taskunifier.api.models.beans.FileListBean;
import com.leclercb.taskunifier.api.models.beans.HistoryListBean;
import com.leclercb.taskunifier.api.models.beans.FileListBean.FileItemBean;
import com.leclercb.taskunifier.api.models.beans.HistoryListBean.HistoryItemBean;

public class HistoryList implements Cloneable, Serializable, Iterable<HistoryItem>, PropertyChangeListener, ListChangeSupported, PropertyChangeSupported {
	
	private ListChangeSupport listChangeSupport;
	private PropertyChangeSupport propertyChangeSupport;
	
	private List<HistoryItem> histories;
	
	public HistoryList() {
		this.listChangeSupport = new ListChangeSupport(this);
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		
		this.histories = new ArrayList<HistoryItem>();
	}
	
	@Override
	protected HistoryList clone() {
		HistoryList list = new HistoryList();
		list.histories.addAll(this.histories);
		return list;
	}
	
	@Override
	public Iterator<HistoryItem> iterator() {
		return this.histories.iterator();
	}
	
	public List<HistoryItem> getList() {
		return new ArrayList<HistoryItem>(this.histories);
	}
	
	public void add(HistoryItem item) {
		CheckUtils.isNotNull(item);
		this.histories.add(item);
		
		item.addPropertyChangeListener(this);
		int index = this.histories.indexOf(item);
		this.listChangeSupport.fireListChange(
				ListChangeEvent.VALUE_ADDED,
				index,
				item);
	}
	
	public void addAll(Collection<HistoryItem> items) {
		if (items == null)
			return;
		
		for (HistoryItem item : items)
			this.add(item);
	}
	
	public void remove(HistoryItem item) {
		CheckUtils.isNotNull(item);
		
		int index = this.histories.indexOf(item);
		if (this.histories.remove(item)) {
			item.removePropertyChangeListener(this);
			this.listChangeSupport.fireListChange(
					ListChangeEvent.VALUE_REMOVED,
					index,
					item);
		}
	}
	
	public void clear() {
		for (HistoryItem item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.histories.size();
	}
	
	public int getIndexOf(HistoryItem item) {
		return this.histories.indexOf(item);
	}
	
	public HistoryItem get(int index) {
		return this.histories.get(index);
	}
	
	@Override
	public String toString() {
		List<String> histories = new ArrayList<String>();
		for (HistoryItem history : this.histories) {
			if (history.toString().length() != 0)
				histories.add(history.toString());
		}
		
		return StringUtils.join(histories, ", ");
	}
	
	public HistoryListBean toHistoryGroupBean() {
		HistoryListBean list = new HistoryListBean();
		
		for (HistoryItem item : this)
			list.add(item.toHistoryItemBean());
		
		return list;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.propertyChangeSupport.firePropertyChange(event);
	}
	
	@Override
	public void addListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.addListChangeListener(listener);
	}
	
	@Override
	public void removeListChangeListener(ListChangeListener listener) {
		this.listChangeSupport.removeListChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				propertyName,
				listener);
	}
	
	
	
	public static class HistoryItem implements PropertyChangeSupported {
		
		public static final String PROP_COMMENT= History.PROP_COMMENTS;
		public static final String PROP_STATUS = History.PROP_STATUS;
		public static final String PROP_DATE   = History.PROP_DATE;
		public static final String PROP_HOURS   = History.PROP_HOURS_WORKD;
		//public static final String PROP_BUG_NO = History.PROP_BUGTRACKNO;
		
		private PropertyChangeSupport propertyChangeSupport;
		
		private String comment;
		private String date;
		private String status;
		private String hoursWorked;
		//private String bugTrackNumber;
		
		public HistoryItem(String comment, String Date) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			this.setComment(comment);
			this.setDate(date);
		}
		
		public HistoryItem(String comment, String status, String date, String hoursWorked ) {
			this.propertyChangeSupport = new PropertyChangeSupport(this);
			
			this.setComment(comment);
			this.setStatus(status);
			this.setDate(date);
			this.setHoursWorked(hoursWorked);
			//this.setBugTrackNumber(bugTrackNumber);
			
		}
		
		public String getComment() {
			return this.comment;
		}
		public void setComment(String comment) {
			String oldComment = this.comment;
			this.comment = comment;
			this.propertyChangeSupport.firePropertyChange(
					PROP_COMMENT,
					oldComment,
					comment);
		}
		

		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			String oldStatus = this.status;
			this.status = status;
			this.propertyChangeSupport.firePropertyChange(
					PROP_STATUS,
					oldStatus,
					status);
			
		}
		
		public String getDate() {
			return this.date;
		}
		public void setDate(String date) {
			String oldDate = this.date;
			this.date = date;
			this.propertyChangeSupport.firePropertyChange(
					PROP_DATE,
					oldDate,
					date);
			this.date = date;
		}


		public String getHoursWorked() {
			return hoursWorked;
		}
		public void setHoursWorked(String hoursWorked) {
			String oldHoursWorked = this.hoursWorked;
			this.hoursWorked = hoursWorked;
			this.propertyChangeSupport.firePropertyChange(
					String.valueOf(PROP_HOURS),
					oldHoursWorked,
					String.valueOf(hoursWorked));
		}

	/*	public String getBugTrackNumber() {
			return bugTrackNumber;
		}

		public void setBugTrackNumber(String bugTrackNumber) {
			String oldbugTrackNumber = this.bugTrackNumber;
			this.bugTrackNumber = bugTrackNumber;
			this.propertyChangeSupport.firePropertyChange(
					PROP_STATUS,
					oldbugTrackNumber,
					bugTrackNumber);
		}*/

		@Override
		public String toString() {
			String comment = (this.comment == null ? "" : this.comment);
			String date = (String) (this.date == null ? "" : this.date);
			
			if (comment.length() != 0)
				comment = "(" + this.comment + ")";
			
			return comment + " " + date;
		}
		
		public HistoryItemBean toHistoryItemBean() {
			return new HistoryItemBean(PROP_COMMENT,PROP_STATUS, PROP_DATE, PROP_HOURS);
		}
		

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(listener);
		}
		
		@Override
		public void addPropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.addPropertyChangeListener(
					propertyName,
					listener);
		}
		
		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(listener);
		}
		
		@Override
		public void removePropertyChangeListener(
				String propertyName,
				PropertyChangeListener listener) {
			this.propertyChangeSupport.removePropertyChangeListener(
					propertyName,
					listener);
		}

	}
}

