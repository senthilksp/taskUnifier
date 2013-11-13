package com.leclercb.taskunifier.api.models;

import com.leclercb.taskunifier.api.models.beans.HistoryBean;

public class HistoryFactory<H extends History, HB extends HistoryBean> extends AbstractModelFactory<History, HistoryBean, H, HB> {


private static HistoryFactory<History, HistoryBean> FACTORY;
	
	@SuppressWarnings("unchecked")
	public static <H extends History, HB extends HistoryBean> void initializeWithClass(
			Class<H> modelClass,
			Class<HB> modelBeanClass) {
		if (FACTORY == null) {
			FACTORY = (HistoryFactory<History, HistoryBean>) new HistoryFactory<H, HB>(
					modelClass,
					modelBeanClass);
		}
	}
	
	public static HistoryFactory<History, HistoryBean> getInstance() {
		if (FACTORY == null)
			FACTORY = new HistoryFactory<History, HistoryBean>(
					History.class,
					HistoryBean.class);
		
		return FACTORY;
	}
	
	private HistoryFactory(Class<H> modelClass, Class<HB> modelBeanClass) {
		super(History.class, HistoryBean.class, modelClass, modelBeanClass);
	}
	
	@Override
	protected String getModelNodeName() {
		return "history";
	}
	
	@Override
	protected String getModelListNodeName() {
		return "histories";
	}
	


}


