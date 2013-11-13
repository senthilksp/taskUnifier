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
package com.leclercb.taskunifier.gui.main.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.HistoryFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.actions.ActionResetGeneralSearchers;
import com.leclercb.taskunifier.gui.api.rules.TaskRuleFactory;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.coders.NoteSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.api.searchers.coders.TaskSearcherFactoryXMLCoder;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.main.main.DBUtilsSelect;
import com.leclercb.taskunifier.gui.translations.Translations;

public final class MainLoadFiles {
	
	private MainLoadFiles() {
		
	}
	
	public static boolean loadFolder(String f) throws Exception {
		File folder = new File(f);
		if (!folder.exists()) {
			if (!folder.mkdir())
				throw new Exception(String.format(
						"Error while creating folder \"%1s\"",
						f));
			
			try {
				folder.setExecutable(true, true);
				folder.setReadable(true, true);
				folder.setWritable(true, true);
			} catch (Throwable t) {
				GuiLogger.getLogger().log(
						Level.SEVERE,
						String.format(
								"Cannot change folder permissions \"%1s\"",
								f),
						t);
			}
			
			return true;
		} else if (!folder.isDirectory()) {
			throw new Exception(String.format("\"%1s\" is not a folder", f));
		}
		
		return false;
	}
	
	public static void loadAllData(String folder) {
		loadAllData(folder, Constants.DEFAULT_SUFFIX);
	}
	
	public static void loadAllData(String folder, String suffix) {
		loadModels(folder, suffix);
		loadTaskRules(folder, suffix);
		loadTaskTemplates(folder, suffix);
		loadTaskSearchers(folder, suffix);
		loadNoteSearchers(folder, suffix);
	}
	
	private static void loadModels(String folder, String suffix) {
		DBUtilsSelect dbselect = new DBUtilsSelect(); // senthil
		
		if (suffix == null)
			suffix = "";
		
		try {
			ContactFactory.getInstance().deleteAll();
			ContextFactory.getInstance().deleteAll();
			FolderFactory.getInstance().deleteAll();
			GoalFactory.getInstance().deleteAll();
			NoteFactory.getInstance().deleteAll();
			TaskFactory.getInstance().deleteAll();
			LocationFactory.getInstance().deleteAll();
			
			TaskFactory.getInstance().decodeFromDBTables(dbselect.fetchTaskData());
			ContextFactory.getInstance().decodeFromDBTables(dbselect.fetchContextData());
			LocationFactory.getInstance().decodeFromDBTables(dbselect.fetchLocationData());
			GoalFactory.getInstance().decodeFromDBTables(dbselect.fetchGoalData());
			FolderFactory.getInstance().decodeFromDBTables(dbselect.fetchFolderData());
			
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading data",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
	private static void loadTaskRules(String folder, String suffix) {
		if (suffix == null)
			suffix = "";
		
		try {
			TaskRuleFactory.getInstance().deleteAll();
			
			TaskRuleFactory.getInstance().decodeFromXML(
					new FileInputStream(folder
							+ File.separator
							+ "task_rules"
							+ suffix
							+ ".xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading task rules",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void loadTaskTemplates(String folder, String suffix) {
		if (suffix == null)
			suffix = "";
		
		try {
			TaskTemplateFactory.getInstance().deleteAll();
			
			TaskTemplateFactory.getInstance().decodeFromXML(
					new FileInputStream(folder
							+ File.separator
							+ "task_templates"
							+ suffix
							+ ".xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Exception e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading task templates",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void loadTaskSearchers(String folder, String suffix) {
		if (suffix == null)
			suffix = "";
		
		try {
			TaskSearcherFactory.getInstance().deleteAll();
			
			new TaskSearcherFactoryXMLCoder().decode(new FileInputStream(folder
					+ File.separator
					+ "task_searchers"
					+ suffix
					+ ".xml"));
		} catch (FileNotFoundException e) {
			ActionResetGeneralSearchers.resetGeneralSearchers();
		} catch (Throwable e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading task searchers",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void loadNoteSearchers(String folder, String suffix) {
		if (suffix == null)
			suffix = "";
		
		try {
			NoteSearcherFactory.getInstance().deleteAll();
			
			new NoteSearcherFactoryXMLCoder().decode(new FileInputStream(folder
					+ File.separator
					+ "note_searchers"
					+ suffix
					+ ".xml"));
		} catch (FileNotFoundException e) {
			
		} catch (Throwable e) {
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"Error while loading note searchers",
					e);
			
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
