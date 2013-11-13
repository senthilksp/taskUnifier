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
package com.leclercb.taskunifier.gui.components.taskhistories.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;

import javax.swing.table.AbstractTableModel;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.event.listchange.WeakListChangeListener;
import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.HistoryList;
import com.leclercb.taskunifier.api.models.HistoryList.HistoryItem;
import com.leclercb.taskunifier.gui.api.accessor.PropertyAccessor;
import com.leclercb.taskunifier.gui.components.taskhistories.TaskHistoriesColumnList;

public class TaskHistoriesTableModel extends AbstractTableModel implements
		ListChangeListener, PropertyChangeListener {

	private HistoryList histories;

	public TaskHistoriesTableModel() {
		this.histories = new HistoryList();
	}

	public HistoryList getHistoryGroup() {
		return this.histories;
	}

	public void setHistoryGroup(HistoryList histories) {
		if (EqualsUtils.equals(this.histories, histories))
			return;

		if (this.histories != null) {
			this.histories.removeListChangeListener(this);
			this.histories.removePropertyChangeListener(this);
		}

		this.histories = histories;

		if (this.histories != null) {
			this.histories.addListChangeListener(new WeakListChangeListener(
					this.histories, this));
			this.histories
					.addPropertyChangeListener(new WeakPropertyChangeListener(
							this.histories, this));
		}

		this.fireTableDataChanged();
	}

	public HistoryItem getHistoryItem(int row) {
		return this.histories.get(row);
	}

	public PropertyAccessor<HistoryItem> getTaskHistoriesColumn(int col) {
		return TaskHistoriesColumnList.getInstance().getAccessor(col);
	}

	@Override
	public int getColumnCount() {
		return TaskHistoriesColumnList.getInstance().getSize();
	}

	@Override
	public int getRowCount() {
		if (this.histories == null)
			return 0;

		return this.histories.size();
	}

	@Override
	public String getColumnName(int col) {
		return this.getTaskHistoriesColumn(col).getLabel();
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return this.getTaskHistoriesColumn(col).getType().getType();
	}

	@Override
	public Object getValueAt(int row, int col) {
		HistoryItem item = this.histories.get(row);
		return this.getTaskHistoriesColumn(col).getProperty(item);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return this.getTaskHistoriesColumn(col).isEditable();
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		HistoryItem item = this.histories.get(row);
		PropertyAccessor<HistoryItem> column = this.getTaskHistoriesColumn(col);

		Object oldValue = column.getProperty(item);

		if (!EqualsUtils.equals(oldValue, value)) {
			column.setProperty(item, value);
		}
	}

	@Override
	public void listChange(ListChangeEvent event) {
		try {
			if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
				this.fireTableRowsInserted(event.getIndex(), event.getIndex());
			} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
				this.fireTableRowsDeleted(event.getIndex(), event.getIndex());
			}
		} catch (Exception e) {
			GuiLogger.getLogger().log(Level.WARNING, e.getMessage(), e);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		int index = this.histories.getIndexOf((HistoryItem) event.getSource());
		this.fireTableRowsUpdated(index, index);
	}

}
