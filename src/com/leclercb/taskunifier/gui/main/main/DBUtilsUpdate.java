package com.leclercb.taskunifier.gui.main.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactList;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.HistoryList;
import com.leclercb.taskunifier.api.models.HistoryList.HistoryItem;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.ModelList;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
import com.leclercb.taskunifier.gui.translations.Translations;

public class DBUtilsUpdate {
	
	public static PreparedStatement ps = null;

	public static void updateTask(Task t, Connection conn)  {
		try {
			if (conn != null && t != null) {
				ps = conn.prepareStatement(DBConstants.updateTask);
				
				ps.setString(1, t.getModelStatus().name());
				ps.setDate(2, DBUtilsSave.convertUtilDateIntoSqlDate(t.getModelCreationDate()
						.getTime()));
				ps.setDate(3, DBUtilsSave.convertUtilDateIntoSqlDate(t.getModelUpdateDate()
						.getTime()));
				ps.setString(4, t.getTitle());
				ps.setInt(5, t.getOrder());
				ps.setString(6, t.getParent() == null ? "" : t.getParent()
						.getModelId().getId());
				ps.setDouble(7, t.getProgress());
				ps.setBoolean(8, t.isCompleted());

				if (t.getStartDate() != null) {
					ps.setDate(9, DBUtilsSave.convertUtilDateIntoSqlDate(t.getStartDate()
							.getTime()));
				} else {
					ps.setDate(9, null);
				}

				ps.setInt(10, t.getStartDateReminder());

				if (t.getDueDate() != null) {
					ps.setDate(11, DBUtilsSave.convertUtilDateIntoSqlDate(t.getDueDate()
							.getTime()));
				} else {
					ps.setDate(11, null);
				}

				ps.setInt(12, t.getDueDateReminder());
				ps.setString(13, t.getRepeat() == null ? "" : t.getRepeat());
				ps.setString(14, t.getRepeatFrom() == null ? "" : t.getRepeatFrom()
						.name());
				ps.setString(15, t.getStatus() == null ? "" : t.getStatus());
				ps.setInt(16, t.getLength());
				ps.setLong(17, t.getTimer().getValue());
				ps.setString(18, t.getPriority() == null ? "" : t.getPriority()
						.toString());
				ps.setBoolean(19, t.isStar());
				ps.setString(20, t.getNote());
				ps.setString(21, t.getClient());
				ps.setString(22,  t.getIssueId().trim());
				//ps.setInt(23,  DBConstants.UserId);
				ps.setString(23, t.getModelId().getId());
				ps.executeUpdate();
				
				ContactList list = t.getContacts();
				ps = conn.prepareStatement(DBConstants.deleteTask_Contacts);
				ps.setString(1, t.getModelId().getId());
				ps.executeUpdate();
				
				for(ContactItem c:list) {
					ps = conn.prepareStatement(DBConstants.insertTask_Contacts);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, c.getContact().getModelId().getId());
					ps.executeUpdate();
					
				}
				
				ModelList<Context> cxlist = t.getContexts();
				ps = conn.prepareStatement(DBConstants.deleteTask_Contexts);
				ps.setString(1, t.getModelId().getId());
				ps.executeUpdate();
				
				for(Context cx:cxlist) {
					ps = conn.prepareStatement(DBConstants.insertTask_Contexts);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, cx.getModelId().getId());
					ps.executeUpdate();
					
					ps = conn.prepareStatement(DBConstants.updateTaskTableContext);
					ps.setString(1, cx.getModelId().getId());
					ps.setString(2, t.getModelId().getId());
					ps.executeUpdate();
				} 
				
				
				ModelList<Location> loclist = t.getLocations();
				ps = conn.prepareStatement(DBConstants.deleteTask_Locations);
				ps.setString(1, t.getModelId().getId());
				ps.executeUpdate();
				
				for(Location l:loclist) {
					ps = conn.prepareStatement(DBConstants.insertTask_Locations);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, l.getModelId().getId());
					ps.executeUpdate();
				}
				
				ModelList<Goal> glist = t.getGoals();
				ps = conn.prepareStatement(DBConstants.deleteTask_Goals);
				ps.setString(1, t.getModelId().getId());
				ps.executeUpdate();
				
				for(Goal g:glist) {
					ps = conn.prepareStatement(DBConstants.insertTask_Goals);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, g.getModelId().getId());
					ps.executeUpdate();
				}
				
				
				TagList tagList = t.getTags();
				ps = conn.prepareStatement(DBConstants.deleteTask_tags);
				ps.setString(1, t.getModelId().getId());
				ps.executeUpdate();
				
				for(Tag tag:tagList) {
					ps = conn.prepareStatement(DBConstants.insertTask_tags);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, tag.toString());
					ps.executeUpdate();
				}
				
				Folder f = t.getFolder();
				ps = conn.prepareStatement(DBConstants.updateTaskTableFolder);
				if(f!=null) {
					ps.setString(1, f.getModelId().getId());
					ps.setString(2, t.getModelId().getId());
					ps.executeUpdate();
				}
				
				
				HistoryList historyList = t.getHistories();
				ps = conn.prepareStatement(DBConstants.deleteTask_Histories);
				ps.setString(1, t.getModelId().getId());
				ps.executeUpdate();
				
				for(HistoryItem h:historyList) {
					try {
						ps = conn.prepareStatement(DBConstants.insertTask_histories);
						ps.setString(1, t.getModelId().getId());
						ps.setString(2, h.getComment());
						ps.setString(3, h.getStatus());
						
						Calendar cal = DBUtilsSave.convertStringToCalendar(h.getDate());
						ps.setDate(4, DBUtilsSave.convertUtilDateIntoSqlDate(cal.getTime()));
						
						ps.setInt(5, Integer.valueOf(h.getHoursWorked()));
						ps.setString(6, t.getIssueId());
						ps.executeUpdate();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(),
								Translations.getString("general.error"),
								JOptionPane.ERROR_MESSAGE);
					}
				}
				
				System.out.println("Rows Updated -in tasks_ table");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					e.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}

		
	}

	public static void updateContact(Contact c, Connection conn) throws SQLException {
		if (conn != null && c != null) {
			ps = conn.prepareStatement(DBConstants.updatecontact);
			
			ps.setString(1, c.getModelStatus().name());
			ps.setDate(2, DBUtilsSave.convertUtilDateIntoSqlDate(c.getModelCreationDate()
					.getTime()));
			ps.setDate(3, DBUtilsSave.convertUtilDateIntoSqlDate(c.getModelUpdateDate()
					.getTime()));
			ps.setString(4, c.getTitle());
			ps.setInt(5, c.getOrder());
			ps.setString(6, c.getFirstName() == null ? "" : c.getFirstName());
			ps.setString(7, c.getLastName() == null ? "" : c.getLastName());
			ps.setString(8, c.getEmail() == null ? "" : c.getEmail());
			ps.setString(9, c.getModelId().getId());

			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS Updated IN contacts_-- TABLE");
			ps.close();
		}
		
	}

	public static void updateContext(Context cx, Connection conn) throws SQLException {
		if (conn != null && cx != null) {
			ps = conn.prepareStatement(DBConstants.updateContext);
			
			ps.setString(1, cx.getModelStatus().name());
			ps.setDate(2, DBUtilsSave.convertUtilDateIntoSqlDate(cx.getModelCreationDate()
					.getTime()));
			ps.setDate(3, DBUtilsSave.convertUtilDateIntoSqlDate(cx.getModelUpdateDate()
					.getTime()));
			ps.setString(4, cx.getTitle());
			ps.setInt(5, cx.getOrder());
			ps.setString(6, cx.getModelId().getId());
			
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS Updated IN contexts_-- TABLE");
		}

		
	}

	public static void updateFolder(Folder fl, Connection conn) throws SQLException{
		if (conn != null && fl != null) {
			ps = conn.prepareStatement(DBConstants.updateFolder);
			
			ps.setString(1, fl.getModelStatus().name());
			ps.setDate(2, DBUtilsSave.convertUtilDateIntoSqlDate(fl.getModelCreationDate()
					.getTime()));
			ps.setDate(3, DBUtilsSave.convertUtilDateIntoSqlDate(fl.getModelUpdateDate()
					.getTime()));
			ps.setString(4, fl.getTitle());
			ps.setInt(5, fl.getOrder());
			ps.setBoolean(6, fl.isArchived());
			
			ps.setString(7, fl.getModelId().getId());
			
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS Updated IN folders_-- TABLE");
		}
		
	}

	public static void updateGoal(Goal g, Connection conn) throws SQLException {
		if (conn != null && g != null) {
			ps = conn.prepareStatement(DBConstants.updateGoal);
			
			ps.setString(1, g.getModelStatus().name());
			ps.setDate(2, DBUtilsSave.convertUtilDateIntoSqlDate(g.getModelCreationDate()
					.getTime()));
			ps.setDate(3, DBUtilsSave.convertUtilDateIntoSqlDate(g.getModelUpdateDate()
					.getTime()));
			ps.setString(4, g.getTitle());
			ps.setInt(5, g.getOrder());
			ps.setString(6, g.getLevel().toString());
			ps.setBoolean(7, g.isArchived());
			ps.setString(8, "");
			ps.setString(9, g.getModelId().getId());
			
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS Updated IN goals_-- TABLE");
		}
		
	}

	public static void updateLocation(Location l, Connection conn) throws SQLException{
		if (conn != null && l != null) {
			ps = conn.prepareStatement(DBConstants.updateLocation);

			ps.setString(1, l.getModelStatus().name());
			ps.setDate(2, DBUtilsSave.convertUtilDateIntoSqlDate(l.getModelCreationDate()
					.getTime()));
			ps.setDate(3, DBUtilsSave.convertUtilDateIntoSqlDate(l.getModelUpdateDate()
					.getTime()));
			ps.setString(4, l.getTitle());
			ps.setInt(5, l.getOrder());
			ps.setString(6, "");
			ps.setDouble(7,l.getLatitude());
			ps.setDouble(8, l.getLongitude());
			ps.setString(9, l.getModelId().getId());
			
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS Updated IN locations_-- TABLE");
		}
		
	}

	public static void updateNotes(Note n, Connection conn) throws SQLException {
		if (conn != null && n != null) {
			System.out.println("inside contacts function...");
			ps = conn.prepareStatement(DBConstants.updateNote);
			ps.setString(1, n.getModelStatus().name());
			ps.setDate(2, DBUtilsSave.convertUtilDateIntoSqlDate(n.getModelCreationDate()
					.getTime()));
			ps.setDate(3, DBUtilsSave.convertUtilDateIntoSqlDate(n.getModelUpdateDate()
					.getTime()));
			ps.setString(4, n.getTitle());
			ps.setInt(5, n.getOrder());
			ps.setString(6, n.getModelId().getId());
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS Updated IN NOTES_-- TABLE");
		}
		
	}


}
