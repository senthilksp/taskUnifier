package com.leclercb.taskunifier.api.models.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.History;
import com.leclercb.taskunifier.api.models.HistoryFactory;
import com.leclercb.taskunifier.api.models.HistoryList;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.HistoryList.HistoryItem;
import com.leclercb.taskunifier.api.models.beans.HistoryListBean.HistoryItemBean;
import com.thoughtworks.xstream.annotations.XStreamAlias;


public class HistoryListBean implements Cloneable, Serializable, Iterable<HistoryItemBean> {

	@XStreamAlias("historylist")
	private List<HistoryItemBean> histories;
	
	public HistoryListBean() {
		this.histories = new ArrayList<HistoryItemBean>();
	}
	
	@Override
	protected HistoryListBean clone() {
		HistoryListBean list = new HistoryListBean();
		list.histories.addAll(this.histories);
		return list;
	}
	
	@Override
	public Iterator<HistoryItemBean> iterator() {
		return this.histories.iterator();
	}
	
	public List<HistoryItemBean> getList() {
		return new ArrayList<HistoryItemBean>(this.histories);
	}
	
	public void add(HistoryItemBean item) {
		CheckUtils.isNotNull(item);
		this.histories.add(item);
	}
	
	public void addAll(Collection<HistoryItemBean> items) {
		if (items == null)
			return;
		
		for (HistoryItemBean item : items)
			this.add(item);
	}
	
	public void remove(HistoryItemBean item) {
		CheckUtils.isNotNull(item);
		this.histories.remove(item);
	}
	
	public void clear() {
		for (HistoryItemBean item : this.getList())
			this.remove(item);
	}
	
	public int size() {
		return this.histories.size();
	}
	
	public int getIndexOf(HistoryItemBean item) {
		return this.histories.indexOf(item);
	}
	
	public HistoryItemBean get(int index) {
		return this.histories.get(index);
	}
	
	public HistoryList toHistoryGroup() {
		HistoryList list = new HistoryList();
		
		for (HistoryItemBean item : this)
			list.add(item.toHistoryItem());
		
		return list;
	}
	
	@XStreamAlias("historyitem")
	public static class HistoryItemBean implements Serializable {
		
		@XStreamAlias("comment")
		private String comment;
		
		@XStreamAlias("date")
		private String date;
		
		@XStreamAlias("status")
		private String status;
		
		@XStreamAlias("hoursWorked")
		private String hoursWorked;
		
		/*@XStreamAlias("bugTrackNumber")
		private String bugTrackNumber;*/
		
		public HistoryItemBean() {
			this.setComment(null);
			this.setDate(null);
		}
		
		public HistoryItemBean(String comment, String status, String date, String hrsWorked) {
			this.setComment(comment);
			this.setDate(date);
			this.setStatus(status);
			this.setDate(date);
			this.setHoursWorked(hrsWorked);
			//this.setBugTrackNumber(bugTrackNo);
			
		}
		
		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		public String getHoursWorked() {
			return hoursWorked;
		}

		public void setHoursWorked(String hoursWorked) {
			this.hoursWorked = hoursWorked;
		}

		/*public String getBugTrackno() {
			return bugTrackNumber;
		}

		public void setBugTrackNumber(String bugTrackno) {
			this.bugTrackNumber = bugTrackno;
		}*/

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public HistoryItem toHistoryItem() {
			if (this.comment == null)
				return new HistoryItem(null, this.comment);
			return new HistoryItem(this.comment, this.status, this.date, this.hoursWorked);
		}
		
		
	}

}
