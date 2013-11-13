package com.leclercb.taskunifier.api.models.beans;

import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;

public class HistoryBean extends AbstractModelBean {
	
	private String comments;
	private String status;
	private String date;
	private float hoursworked;
	private String bugTrackNumber;
	
	public HistoryBean() {
		this((ModelId) null);
	}
	
	public HistoryBean(ModelId modelId) {
		super(modelId);
		
		this.setComments(null);
		this.setStatus(null);
		this.setDate(null);
		this.setBugTrackNumber(null);
		this.setHoursworked(0f);
		
		
	}
	
	public HistoryBean(HistoryBean bean) {
		super(bean);

		this.setStatus(bean.getStatus());
		this.setBugTrackNumber(bean.getBugTrackNumber());
		this.setComments(bean.getComments());
		this.setDate(bean.getDate());
		this.setHoursworked(bean.getHoursworked());
	}
	


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public float getHoursworked() {
		return hoursworked;
	}

	public void setHoursworked(float hoursworked) {
		this.hoursworked = hoursworked;
	}

	public String getBugTrackNumber() {
		return bugTrackNumber;
	}

	public void setBugTrackNumber(String bugTrackNumber) {
		this.bugTrackNumber = bugTrackNumber;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}


	@Override
	public ModelType getModelType() {
		return ModelType.HISTORY;
	}
	
	

}
