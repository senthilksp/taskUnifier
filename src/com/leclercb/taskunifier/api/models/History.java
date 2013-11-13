package com.leclercb.taskunifier.api.models;

import java.util.Calendar;

import com.leclercb.taskunifier.api.models.beans.HistoryBean;

public class History extends AbstractModel implements Model {

	public static final String PROP_COMMENTS = "comments";
	public static final String PROP_STATUS = "status";
	public static final String PROP_DATE   = "date";
	public static final String PROP_HOURS_WORKD = "hoursWorked";
	//public static final String PROP_BUGTRACKNO = "bugTrackNumber";

	private String comments;
	private String status;
	private Calendar date;
	private String hoursWorked;
	//private String bugTrackNumber;

	public Calendar getDate() {
		return date;
	}


	public String getHoursWorked() {
		return hoursWorked;
	}


	public void setHoursWorked(String hoursWorked) {
		this.hoursWorked = hoursWorked;
	}


	public void setDate(Calendar date) {
		this.date = date;
	}


	protected History(HistoryBean bean, boolean loadReferenceIds) {
		this(bean.getModelId(), bean.getTitle());
		this.loadBean(bean, loadReferenceIds);
	}

	protected History(String title) {
		this(new ModelId(), title);
	}

	protected History(ModelId modelId, String title) {
		super(modelId, title);
		
		this.setComments(null);
		this.setStatus(null);
		this.setDate(null);
		this.setHoursWorked(null);
		//this.setBugTrackNumber(null);
		
		this.getFactory().register(this);
	}

	@Override
	public History clone(ModelId modelId) {
		History history = this.getFactory().create(modelId, this.getTitle());

		history.setComments(this.getComments());
		history.setStatus(this.getStatus());
		//history.setBugTrackNumber(this.getBugTrackNumber());
		history.setDate(this.getDate());
		history.setHoursWorked(hoursWorked);
		// After all other setXxx methods
		history.setOrder(this.getOrder());
		history.addProperties(this.getProperties());
		history.setModelStatus(this.getModelStatus());
		history.setModelCreationDate(Calendar.getInstance());
		history.setModelUpdateDate(Calendar.getInstance());

		return history;
	}

	@Override
	public HistoryFactory<History, HistoryBean> getFactory() {
		return HistoryFactory.getInstance();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/*public String getBugTrackNumber() {
		return bugTrackNumber;
	}

	public void setBugTrackNumber(String bugTrackNumber) {
		this.bugTrackNumber = bugTrackNumber;
	}*/


	@Override
	public ModelType getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

}
