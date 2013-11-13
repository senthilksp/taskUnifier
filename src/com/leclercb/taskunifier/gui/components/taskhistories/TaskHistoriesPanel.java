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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.leclercb.taskunifier.api.models.HistoryList;
import com.leclercb.taskunifier.api.models.HistoryList.HistoryItem;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.components.taskhistories.TaskHistoriesView;
import com.leclercb.taskunifier.gui.components.taskhistories.table.TaskHistoriesTable;
import com.leclercb.taskunifier.gui.main.main.DBUtilsSave;
import com.leclercb.taskunifier.gui.swing.TUFileDialog;
import com.leclercb.taskunifier.gui.swing.TUHistoryDialog;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.DateTimeFormatUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class TaskHistoriesPanel extends JPanel implements TaskHistoriesView,
		ModelSelectionListener {

	private TaskHistoriesTable table;
	private JToolBar toolBar;

	private Action addAction;
	private Action removeAction;

	public TaskHistoriesPanel(TUTableProperties<HistoryItem> tuTableProperties) {
		this.initialize(tuTableProperties);
	}

	private void initialize(TUTableProperties<HistoryItem> tableProperties) {
		this.setOpaque(false);
		this.setLayout(new BorderLayout());

		this.table = new TaskHistoriesTable(tableProperties);

		this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		this.toolBar.setOpaque(false);
		this.toolBar.setFloatable(false);

		this.initializeActions();

		this.toolBar.add(this.addAction);
		this.toolBar.add(this.removeAction);
		this.toolBar.add(Help.getInstance().getHelpButton("task_files"));

		this.add(ComponentFactory.createJScrollPane(this.table, false),
				BorderLayout.CENTER);
		this.add(this.toolBar, BorderLayout.SOUTH);

		this.table.setHistoryGroup(null);
		this.addAction.setEnabled(false);
		this.removeAction.setEnabled(false);
	}

	private void initializeActions() {

		this.addAction = new AbstractAction("", ImageUtils.getResourceImage(
				"add.png", 16, 16)) {

			@Override
			public void actionPerformed(ActionEvent evt) {
				TUHistoryDialog dialog = new TUHistoryDialog(true,
						Translations.getString("general.history.history"));
				dialog.setVisible(true);

				if (dialog.isCancelled())
					return;

				/*
				 * TUTableProperties<HistoryItem> tableprops = new
				 * TUTableProperties<HistoryItem>(
				 * TaskHistoriesColumnList.getInstance(), "taskHistories",
				 * false); TaskHistoriesTable table = new
				 * TaskHistoriesTable(tableprops);
				 */

				String comments = dialog.getTextFieldComments().getText() == null ? ""
						: dialog.getTextFieldComments().getText();
				String status = (String) dialog.getComboStatus()
						.getSelectedItem();

				Calendar cal = dialog.getTextFieldDate().getCalendar() == null ? null
						: dialog.getTextFieldDate().getCalendar();
				
				String date = DBUtilsSave.convertCalendarToString(cal);
				

				String hrsWorked = dialog.getTextFieldHoursWorked().getText() == null ? ""
						:dialog.getTextFieldHoursWorked().getText();

				/*String bugNo = dialog.getBugTrackNumber().getText() == null ? ""
						: dialog.getBugTrackNumber().getText();*/

				HistoryList hl = TaskHistoriesPanel.this.table
						.getHistoryGroup();
				HistoryItem item = new HistoryItem(comments, status, date,
						hrsWorked);
				//TaskHistoriesPanel.this.table.getHistoryGroup().add(item);
				TaskHistoriesPanel.this.table.setHistoryGroup(hl);
				TaskHistoriesPanel.this.table.getHistoryGroup().add(
						new HistoryItem(comments, status, date, hrsWorked));
			}

		};

		this.removeAction = new AbstractAction("", ImageUtils.getResourceImage(
				"remove.png", 16, 16)) {

			@Override
			public void actionPerformed(ActionEvent evt) {
				HistoryItem[] items = TaskHistoriesPanel.this.table
						.getSelectedHistoryItems();

				TaskHistoriesPanel.this.table.commitChanges();
				TaskHistoriesPanel.this.table.getSelectionModel()
						.clearSelection();

				for (HistoryItem item : items)
					TaskHistoriesPanel.this.table.getHistoryGroup()
							.remove(item);
			}

		};
	}

	@Override
	public void modelSelectionChange(ModelSelectionChangeEvent event) {
		Model[] models = event.getSelectedModels();

		if (models.length != 1 || !(models[0] instanceof Task)) {
			this.table.setHistoryGroup(null);
			this.addAction.setEnabled(false);
			this.removeAction.setEnabled(false);
			return;
		}

		Task task = (Task) models[0];

		this.table.setHistoryGroup(task.getHistories());
		this.addAction.setEnabled(true);
		this.removeAction.setEnabled(true);
	}

}
