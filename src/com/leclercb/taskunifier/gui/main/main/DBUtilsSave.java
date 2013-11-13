package com.leclercb.taskunifier.gui.main.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactList;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
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
import com.leclercb.taskunifier.gui.resources.Resources;
import com.leclercb.taskunifier.gui.translations.Translations;

public class DBUtilsSave {

	public static PreparedStatement ps = null;

	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Properties props = new Properties();
			props.load(Resources.class
					.getResourceAsStream(DBConstants.MADRONE_PROP_FILE));
			Class.forName(props.getProperty(DBConstants.DB_DRIVER));
			dbConnection = DBUtils.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	public static void saveData(List<Task> taskList, List<Contact> contactList,
			List<Context> contextList, List<Folder> folderList,
			List<Goal> goalList, List<Location> locList, List<Note> noteList) {

		try {
			Connection con = getDBConnection();
			con.setAutoCommit(false);
			// To Find which user is using the system. eg. Thiyaga or senthil or
			// doraraj etc.
			// Based on that tasks are filtered.

			for (Contact c : contactList) {
				if (checkDataExists(c.getModelId().getId(), con,
						DBConstants.selectContactsIdOnly)) {
					DBUtilsUpdate.updateContact(c, con);
				} else {
					insertContactData(c, con);
				}
			}

			for (Context cx : contextList) {
				if (checkDataExists(cx.getModelId().getId(), con,
						DBConstants.selectContextsIdOnly)) {
					DBUtilsUpdate.updateContext(cx, con);
				} else {
					insertContextData(cx, con);
				}
			}

			for (Folder fl : folderList) {
				if (checkDataExists(fl.getModelId().getId(), con,
						DBConstants.selectFoldersIdOnly)) {
					DBUtilsUpdate.updateFolder(fl, con);
				} else {
					insertFolderData(fl, con);
				}
			}

			for (Goal g : goalList) {
				if (checkDataExists(g.getModelId().getId(), con,
						DBConstants.selectGoalsIdOnly)) {
					DBUtilsUpdate.updateGoal(g, con);
				} else {
					insertGoalData(g, con);
				}
			}

			for (Location l : locList) {
				if (checkDataExists(l.getModelId().getId(), con,
						DBConstants.selectLocationsIdOnly)) {
					DBUtilsUpdate.updateLocation(l, con);
				} else {
					insertLocationData(l, con);
				}
			}

			for (Note n : noteList) {
				if (checkDataExists(n.getModelId().getId(), con,
						DBConstants.selectNotesIdOnly)) {
					DBUtilsUpdate.updateNotes(n, con);
				} else {
					insertNoteData(n, con);
				}
			}
			con.commit();

			for (Task t : taskList) {
				if (checkDataExists(t.getModelId().getId(), con,
						DBConstants.selectTasksIdOnly)) {
					DBUtilsUpdate.updateTask(t, con);
				} else {
					insertTaskData(t, con);
				}
			}
			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static boolean checkDataExists(String modelId, Connection conn,
			String sql) throws SQLException {
		boolean taskExists = false;
		ps = conn.prepareStatement(sql);
		ps.setString(1, modelId);
		ResultSet rs = ps.executeQuery();
		taskExists = rs.next() ? true : false;
		rs.close();
		return taskExists;

	}

	// 1
	public static void insertTaskData(Task t, Connection conn) {
		try {
			if (conn != null && t != null) {

				ps = conn.prepareStatement(DBConstants.insertTask);
				ps.setString(1, t.getModelId().getId());
				ps.setString(2, t.getModelStatus().name());
				ps.setDate(3, convertUtilDateIntoSqlDate(t
						.getModelCreationDate().getTime()));
				ps.setDate(4, convertUtilDateIntoSqlDate(t.getModelUpdateDate()
						.getTime()));
				ps.setString(5, t.getTitle());
				ps.setInt(6, t.getOrder());
				ps.setString(7, t.getParent() == null ? "" : t.getParent()
						.getModelId().getId());
				ps.setDouble(8, t.getProgress());
				ps.setBoolean(9, t.isCompleted());

				if (t.getStartDate() != null) {
					ps.setDate(10, convertUtilDateIntoSqlDate(t.getStartDate()
							.getTime()));
				} else {
					ps.setDate(10, null);
				}

				ps.setInt(11, t.getStartDateReminder());

				if (t.getDueDate() != null) {
					ps.setDate(12, convertUtilDateIntoSqlDate(t.getDueDate()
							.getTime()));
				} else {
					ps.setDate(12, null);
				}

				ps.setInt(13, t.getDueDateReminder());
				ps.setString(14, t.getRepeat() == null ? "" : t.getRepeat());
				ps.setString(15, t.getRepeatFrom() == null ? "" : t
						.getRepeatFrom().name());
				ps.setString(16, t.getStatus() == null ? "" : t.getStatus());
				ps.setInt(17, t.getLength());
				ps.setLong(18, t.getTimer().getValue());
				ps.setString(19, t.getPriority() == null ? "" : t.getPriority()
						.toString());
				ps.setBoolean(20, t.isStar());
				ps.setString(21, t.getNote());
				ps.setString(22, t.getClient());
				ps.setString(23, t.getIssueId());
				// ps.setInt(24, DBConstants.UserId);
				ps.executeUpdate();

				ContactList list = t.getContacts();
				for (ContactItem c : list) {
					ps = conn.prepareStatement(DBConstants.insertTask_Contacts);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, c.getContact().getModelId().getId());
					ps.executeUpdate();

				}

				ModelList<Context> cxlist = t.getContexts();
				for (Context cx : cxlist) {
					ps = conn.prepareStatement(DBConstants.insertTask_Contexts);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, cx.getModelId().getId());
					ps.executeUpdate();

					ps = conn
							.prepareStatement(DBConstants.updateTaskTableContext);
					ps.setString(1, cx.getModelId().getId());
					ps.setString(2, t.getModelId().getId());
					ps.executeUpdate();

				}

				ModelList<Location> loclist = t.getLocations();
				for (Location l : loclist) {
					ps = conn
							.prepareStatement(DBConstants.insertTask_Locations);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, l.getModelId().getId());
					ps.executeUpdate();
				}

				ModelList<Goal> glist = t.getGoals();
				for (Goal g : glist) {
					ps = conn.prepareStatement(DBConstants.insertTask_Goals);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, g.getModelId().getId());
					ps.executeUpdate();
				}

				TagList tagList = t.getTags();
				for (Tag tag : tagList) {
					ps = conn.prepareStatement(DBConstants.insertTask_tags);
					ps.setString(1, t.getModelId().getId());
					ps.setString(2, tag.toString());
					ps.executeUpdate();
				}

				Folder f = t.getFolder();
				if(f!=null) {
					ps = conn.prepareStatement(DBConstants.updateTaskTableFolder);
					ps.setString(1, f.getModelId().getId());
					ps.setString(2, t.getModelId().getId());
					ps.executeUpdate();
				}
				

				HistoryList historyList = t.getHistories();
				for (HistoryItem h : historyList) {
					try {
						ps = conn
								.prepareStatement(DBConstants.insertTask_histories);
						ps.setString(1, t.getModelId().getId());
						ps.setString(2, h.getComment());
						ps.setString(3, h.getStatus());

						Calendar cal = convertStringToCalendar(h.getDate());
						ps.setDate(4, convertUtilDateIntoSqlDate(cal.getTime()));
						ps.setInt(5, Integer.valueOf(h.getHoursWorked()));

						ps.setString(6, t.getIssueId());
						ps.executeUpdate();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(),
								Translations.getString("general.error"),
								JOptionPane.ERROR_MESSAGE);
					}
				}

				ps.close();
				conn.commit();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, ex.getMessage(),
					Translations.getString("general.error"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static java.sql.Date convertUtilDateIntoSqlDate(
			java.util.Date utilDate) {
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}

	public static Calendar convertStringToCalendar(String stringDate) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		try {
			cal.setTime(formatter.parse(stringDate));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return cal;
	}

	// 2
	public static void insertContactData(Contact c, Connection conn)
			throws SQLException {

		if (conn != null && c != null) {
			System.out.println("inside contacts function...");
			ps = conn.prepareStatement(DBConstants.insertContacts);

			ps.setString(1, c.getModelId().getId());
			ps.setString(2, c.getModelStatus().name());
			ps.setDate(3, convertUtilDateIntoSqlDate(c.getModelCreationDate()
					.getTime()));
			ps.setDate(4, convertUtilDateIntoSqlDate(c.getModelUpdateDate()
					.getTime()));
			ps.setString(5, c.getTitle());
			ps.setInt(6, c.getOrder());
			ps.setString(7, c.getFirstName() == null ? "" : c.getFirstName());
			ps.setString(8, c.getLastName() == null ? "" : c.getLastName());
			ps.setString(9, c.getEmail() == null ? "" : c.getEmail());

			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS INSERTED IN CONTACT-- TABLE");
			ps.close();
		}

	}

	// 3
	public static void insertContextData(Context c, Connection conn)
			throws SQLException {

		if (conn != null && c != null) {
			System.out.println("inside contacts function...");
			ps = conn.prepareStatement(DBConstants.insertContexts);

			ps.setString(1, c.getModelId().getId());
			ps.setString(2, c.getModelStatus().name());
			ps.setDate(3, convertUtilDateIntoSqlDate(c.getModelCreationDate()
					.getTime()));
			ps.setDate(4, convertUtilDateIntoSqlDate(c.getModelUpdateDate()
					.getTime()));
			ps.setString(5, c.getTitle());
			ps.setInt(6, c.getOrder());
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS INSERTED IN Context-- TABLE");
		}

	}

	// 4
	private static void insertNoteData(Note n, Connection conn)
			throws SQLException {

		if (conn != null && n != null) {
			System.out.println("inside contacts function...");
			ps = conn.prepareStatement(DBConstants.insertNotes);

			ps.setString(1, n.getModelId().getId());
			ps.setString(2, n.getModelStatus().name());
			ps.setDate(3, convertUtilDateIntoSqlDate(n.getModelCreationDate()
					.getTime()));
			ps.setDate(4, convertUtilDateIntoSqlDate(n.getModelUpdateDate()
					.getTime()));
			ps.setString(5, n.getTitle());
			ps.setInt(6, n.getOrder());
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS INSERTED IN NOTE_-- TABLE");
		}
	}

	// 5
	private static void insertGoalData(Goal g, Connection conn)
			throws SQLException {

		if (conn != null && g != null) {
			System.out.println("inside contacts function...");
			ps = conn.prepareStatement(DBConstants.insertGoals);

			ps.setString(1, g.getModelId().getId());
			ps.setString(2, g.getModelStatus().name());
			ps.setDate(3, convertUtilDateIntoSqlDate(g.getModelCreationDate()
					.getTime()));
			ps.setDate(4, convertUtilDateIntoSqlDate(g.getModelUpdateDate()
					.getTime()));
			ps.setString(5, g.getTitle());
			ps.setInt(6, g.getOrder());
			ps.setString(7, g.getLevel().toString());
			ps.setBoolean(8, g.isArchived());
			ps.setString(9, "");
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS INSERTED IN Goal_-- TABLE");
		}

	}

	// 6
	private static void insertFolderData(Folder fl, Connection conn)
			throws SQLException {
		if (conn != null && fl != null) {
			ps = conn.prepareStatement(DBConstants.insertFolders);

			ps.setString(1, fl.getModelId().getId());
			ps.setString(2, fl.getModelStatus().name());
			ps.setDate(3, convertUtilDateIntoSqlDate(fl.getModelCreationDate()
					.getTime()));
			ps.setDate(4, convertUtilDateIntoSqlDate(fl.getModelUpdateDate()
					.getTime()));
			ps.setString(5, fl.getTitle());
			ps.setInt(6, fl.getOrder());
			ps.setBoolean(7, fl.isArchived());
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS INSERTED IN Folder_-- TABLE");
		}

	}

	// 7
	private static void insertLocationData(Location l, Connection conn)
			throws SQLException {
		if (conn != null && l != null) {
			System.out.println("inside contacts function...");
			ps = conn.prepareStatement(DBConstants.insertLocations);

			ps.setString(1, l.getModelId().getId());
			ps.setString(2, l.getModelStatus().name());
			ps.setDate(3, convertUtilDateIntoSqlDate(l.getModelCreationDate()
					.getTime()));
			ps.setDate(4, convertUtilDateIntoSqlDate(l.getModelUpdateDate()
					.getTime()));
			ps.setString(5, l.getTitle());
			ps.setInt(6, l.getOrder());
			ps.setString(7, "");
			ps.setDouble(8, l.getLatitude());
			ps.setDouble(9, l.getLongitude());
			ps.executeUpdate();
			GuiLogger.getLogger().log(Level.SEVERE,
					"ROWS INSERTED IN Location_-- TABLE");
		}

	}

	public static String convertCalendarToString(Calendar cal) {
		final String pattern = "dd/MM/yyyy";
		SimpleDateFormat sdfDate = new SimpleDateFormat(pattern);

		String returnString = "";
		Date d1 = cal.getTime();
		returnString = sdfDate.format(d1);

		return returnString;

	}

}
