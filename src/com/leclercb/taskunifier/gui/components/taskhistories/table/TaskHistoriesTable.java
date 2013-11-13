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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXTable;

import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.HistoryList;
import com.leclercb.taskunifier.api.models.HistoryList.HistoryItem;
import com.leclercb.taskunifier.gui.api.accessor.PropertyAccessor;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.components.taskhistories.TaskHistoriesColumnList;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumnModel;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;

public class TaskHistoriesTable extends JXTable implements
		SavePropertiesListener {

	private TUTableProperties<HistoryItem> tableProperties;

	public TaskHistoriesTable(TUTableProperties<HistoryItem> tableProperties) {
		CheckUtils.isNotNull(tableProperties);
		this.tableProperties = tableProperties;

		this.initialize();
	}

	public HistoryList getHistoryGroup() {
		TaskHistoriesTableModel model = (TaskHistoriesTableModel) this
				.getModel();
		if (model.getHistoryGroup() == null) {
			return new HistoryList();
		} else {
			return model.getHistoryGroup();
		}
	}

	public void setHistoryGroup(HistoryList list) {
		this.commitChanges();
		TaskHistoriesTableModel model = (TaskHistoriesTableModel) this
				.getModel();
		model.setHistoryGroup(list);
	}

	public int getHistoryItemCount() {
		return this.getRowCount();
	}

	public HistoryItem getHistoryItem(int row) {
		try {
			int index = this.getRowSorter().convertRowIndexToModel(row);
			return ((TaskHistoriesTableModel) this.getModel())
					.getHistoryItem(index);
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}

	public HistoryItem[] getSelectedHistoryItems() {
		int[] indexes = this.getSelectedRows();

		List<HistoryItem> items = new ArrayList<HistoryItem>();
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1) {
				HistoryItem item = this.getHistoryItem(indexes[i]);

				if (item != null)
					items.add(item);
			}
		}

		return items.toArray(new HistoryItem[0]);
	}

	public void commitChanges() {
		if (this.getCellEditor() != null)
			this.getCellEditor().stopCellEditing();
	}

	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		TUTableColumnModel<HistoryItem> columnModel = new TUTableColumnModel<HistoryItem>(
				this.tableProperties);
		TaskHistoriesTableModel tableModel = new TaskHistoriesTableModel();

		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setRowHeight(24);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.setShowGrid(true, false);

		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);

		this.setSortable(true);
		this.setSortsOnUpdates(false);
		this.setSortOrderCycle(SortOrder.ASCENDING, SortOrder.DESCENDING);
		this.setColumnControlVisible(true);
		this.setSortOrder(
				TaskHistoriesColumnList.getInstance().get(
						TaskHistoriesColumnList.COMMENTS), SortOrder.ASCENDING);

		this.initializeSettings();
		this.initializeDragAndDrop();
		this.initializeDoubleClick();
		this.initializeHighlighters();
	}

	private void initializeSettings() {
		this.setHorizontalScrollEnabled(Main.getSettings().getBooleanProperty(
				this.tableProperties.getPropertyName()
						+ ".horizontal_scroll_enabled", false));
	}

	private void initializeDragAndDrop() {
		this.setDragEnabled(true);
		// this.setTransferHandler(new TaskFilesTransferHandler());
		this.setDropMode(DropMode.INSERT_ROWS);
	}

	private void initializeDoubleClick() {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON1
						&& event.getClickCount() == 2) {
					try {
						int rowIndex = TaskHistoriesTable.this.rowAtPoint(event
								.getPoint());

						rowIndex = TaskHistoriesTable.this.getRowSorter()
								.convertRowIndexToModel(rowIndex);

						if (rowIndex == -1)
							return;

						int colIndex = TaskHistoriesTable.this
								.columnAtPoint(event.getPoint());

						PropertyAccessor<HistoryItem> column = (PropertyAccessor<HistoryItem>) TaskHistoriesTable.this
								.getColumn(colIndex).getIdentifier();

						HistoryItem item = ((TaskHistoriesTable) TaskHistoriesTable.this
								.getModel()).getHistoryItem(rowIndex);

						if (item == null)
							return;

						if (item.getComment() == null)
							return;

					} catch (Exception e) {

					}
				}
			}
		});
	}

	private void initializeHighlighters() {
		this.setHighlighters(new AlternateHighlighter());
	}

	@Override
	public void saveProperties() {
		Main.getSettings().setBooleanProperty(
				this.tableProperties.getPropertyName()
						+ ".horizontal_scroll_enabled",
				this.isHorizontalScrollEnabled());
	}

}
