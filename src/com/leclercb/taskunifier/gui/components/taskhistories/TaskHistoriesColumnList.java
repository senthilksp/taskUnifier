/*
 * TaskUnifier
 * Copyright (c) 2013, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.taskhistories;

import java.util.Calendar;

import com.leclercb.taskunifier.api.models.HistoryList.HistoryItem;
import com.leclercb.taskunifier.gui.api.accessor.DefaultPropertyAccessor;
import com.leclercb.taskunifier.gui.api.accessor.PropertyAccessorList;
import com.leclercb.taskunifier.gui.api.accessor.PropertyAccessorType;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskHistoriesColumnList extends PropertyAccessorList<HistoryItem> {

	public static final String COMMENTS = "COMMENTS";
	public static final String DATE = "DATE";
	public static final String STATUS = "STATUS";
	public static final String HOURES = "HOURES";
	//public static final String BUGTRACK = "BUGTRACKNO";

	private static TaskHistoriesColumnList INSTANCE;

	public static TaskHistoriesColumnList getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TaskHistoriesColumnList();

		return INSTANCE;
	}

	private TaskHistoriesColumnList() {
		this.initialize();
	}

	private void initialize() {
		this.add(new DefaultPropertyAccessor<HistoryItem>("COMMENTS", null,
				PropertyAccessorType.STRING, null, Translations
						.getString("general.history.comment"), true, true, true) {

			@Override
			public Object getProperty(HistoryItem item) {
				return item.getComment();
			}

			@Override
			public void setProperty(HistoryItem item, Object value) {
				item.setComment((String) value);
			}

		});

		this.add(new DefaultPropertyAccessor<HistoryItem>("STATUS", null,
				PropertyAccessorType.STRING, null, Translations
						.getString("general.history.status"), true, true, true) {

			@Override
			public Object getProperty(HistoryItem item) {
				return item.getStatus();
			}

			@Override
			public void setProperty(HistoryItem item, Object value) {
				item.setStatus((String) value);
			}

		});

		this.add(new DefaultPropertyAccessor<HistoryItem>("DATE", null,
				PropertyAccessorType.HISTORY, null, Translations
						.getString("general.history.date"), true, true, true) {

			@Override
			public Object getProperty(HistoryItem item) {
				return item.getDate();
			}

			@Override
			public void setProperty(HistoryItem item, Object value) {
				item.setDate((String) value);
			}

		});

		this.add(new DefaultPropertyAccessor<HistoryItem>("HOURS WORKED", null,
				PropertyAccessorType.HISTORY, null, Translations
						.getString("general.history.hoursworked"), true, true,
				true) {

			@Override
			public Object getProperty(HistoryItem item) {
				return item.getHoursWorked();
			}

			@Override
			public void setProperty(HistoryItem item, Object value) {
				item.setHoursWorked((String) value);
			}

		});

		/*this.add(new DefaultPropertyAccessor<HistoryItem>("BUG TRACK NO", null,
				PropertyAccessorType.HISTORY, null, Translations
						.getString("general.history.bugtrackno"), true, true,
				true) {

			@Override
			public Object getProperty(HistoryItem item) {
				return item.getBugTrackNumber();
			}

			@Override
			public void setProperty(HistoryItem item, Object value) {
				item.setBugTrackNumber((String) value);
			}

		});*/
	}

}
