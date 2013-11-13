package com.leclercb.taskunifier.gui.commons.models;

import java.util.List;

import com.leclercb.commons.api.event.listchange.WeakListChangeListener;
import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.api.models.History;
import com.leclercb.taskunifier.api.models.HistoryFactory;

//senthil
public class HistoryModel extends AbstractBasicModelSortedModel {
	
	public HistoryModel(boolean commentNull) {
		if (commentNull)
			this.addElement(null);
		
		List<History> histories = HistoryFactory.getInstance().getList();
		for (History history : histories)
			this.addElement(history);
		
		HistoryFactory.getInstance().addListChangeListener(
				new WeakListChangeListener(HistoryFactory.getInstance(), this));
		HistoryFactory.getInstance().addPropertyChangeListener(
				new WeakPropertyChangeListener(
						HistoryFactory.getInstance(),
						this));
	}

}
