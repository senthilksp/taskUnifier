package com.leclercb.taskunifier.gui.main.main;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelStatus;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.Timer;
import com.leclercb.taskunifier.api.models.beans.ContextBean;
import com.leclercb.taskunifier.api.models.beans.FolderBean;
import com.leclercb.taskunifier.api.models.beans.GoalBean;
import com.leclercb.taskunifier.api.models.beans.HistoryListBean;
import com.leclercb.taskunifier.api.models.beans.HistoryListBean.HistoryItemBean;
import com.leclercb.taskunifier.api.models.beans.LocationBean;
import com.leclercb.taskunifier.api.models.beans.ModelBean;
import com.leclercb.taskunifier.api.models.beans.ModelBeanList;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskRepeatFrom;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.main.DBConstants;
import com.leclercb.taskunifier.gui.resources.Resources;

public class DBUtilsSelect<OM extends Model, OMB extends ModelBean, M extends Model, MB extends ModelBean> {

	public static PreparedStatement ps = null;

	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Properties props = new Properties();
			props.load(Resources.class
					.getResourceAsStream(DBConstants.MADRONE_PROP_FILE));
			if (props != null)
				System.out.println("pros not null--------");
			Class.forName(props.getProperty(DBConstants.DB_DRIVER));
			dbConnection = DBUtils.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	private static ModelBean getModelValues(ResultSet rs, ModelBean model)
			throws SQLException {
		ModelId id = new ModelId(rs.getString("modelId"));
		ModelStatus status = ModelStatus.valueOf(rs.getString("modelstatus"));
		model.setModelId(id);
		model.setModelStatus(status);

		Calendar cal = Calendar.getInstance();
		cal.setTime(rs.getDate("modelcreationdate"));
		model.setModelCreationDate(cal);

		cal.setTime(rs.getDate("modelupdatedate"));
		model.setModelUpdateDate(cal);

		model.setTitle(rs.getString("title"));
		model.setOrder(rs.getInt("order_id"));
		return model;
	}

	public List<MB> fetchTaskData() throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		List<MB> taskList = new ArrayList<MB>();
		Calendar cal = Calendar.getInstance();

		try {
			conn = getDBConnection();
			String context = findContextIdByUser(conn);

			ps = DBConstants.adminUser.equalsIgnoreCase(context) ? conn
					.prepareStatement(DBConstants.selectTasks_ALL) : conn
					.prepareStatement(DBConstants.selectTasks_BYUSER);

			if (!DBConstants.adminUser.equalsIgnoreCase(context)) {
				ps.setString(1, context);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				TaskBean bean = new TaskBean();
				bean = (TaskBean) getModelValues(rs, bean);
				String sql = DBConstants.adminUser.equalsIgnoreCase(context) ? DBConstants.selectContextsByTask_ALL
						: DBConstants.selectContextsByTask_BYUSER;

				ModelBeanList contextList = fetchTaskAssociations(ps, conn,
						bean.getModelId().getId(), sql, "context_id");
				bean.setContexts(contextList);

				String folder = fetchFolder(ps, conn,
						bean.getModelId().getId(),
						DBConstants.selectFolderFromTaskTable, "folder_id");
				if (folder != null) {
					bean.setFolder(new ModelId(folder));
				}

				ModelBeanList goalList = fetchTaskAssociations(ps, conn, bean
						.getModelId().getId(), DBConstants.selectGoalsByTask,
						"goal_id");
				bean.setGoals(goalList);

				ModelBeanList locList = fetchTaskAssociations(ps, conn, bean
						.getModelId().getId(),
						DBConstants.selectLocationsByTask, "location_id");
				bean.setLocations(locList);

				ModelBeanList tagList = fetchTaskAssociations(ps, conn, bean
						.getModelId().getId(), DBConstants.selectFoldersByTask,
						"tag_id");

				/*
				 * ModelBeanList contactList =
				 * fetchTaskAssociations(ps,conn,bean
				 * .getModelId().getId(),DBConstants.selectContactsByTask);
				 * bean.setContacts(contactList);
				 */

				/*
				 * ModelBeanList folderList = fetchTaskAssociations(ps, conn,
				 * bean .getModelId().getId(), DBConstants.selectFoldersByTask,
				 * "folder_id"); bean.setFolder((folderList);
				 * 
				 * ModelBeanList tagList = fetchTaskAssociations(ps, conn, bean
				 * .getModelId().getId(), DBConstants.selectFoldersByTask,
				 * "tag_id"); bean.setTags(tags)
				 */

				Timer t = new Timer();
				t.setValue(rs.getLong("timer"));

				bean.setProgress(rs.getDouble("progress"));
				bean.setCompleted(rs.getBoolean("completed"));
				bean.setCompletedOn(null);
				if (rs.getDate("startdate") == null) {
					bean.setStartDate(null);
				} else {
					cal.setTime(rs.getDate("startdate"));
					bean.setStartDate(cal);
				}

				bean.setStartDateReminder(rs.getInt("start_date_reminder"));
				if (rs.getDate("startdate") == null) {
					bean.setStartDate(null);
				} else {
					cal.setTime(rs.getDate("startdate"));
					bean.setStartDate(cal);
				}
				bean.setDueDateReminder(rs.getInt("due_date_remainder"));
				bean.setRepeat(rs.getString("repeat"));
				bean.setRepeatFrom(TaskRepeatFrom.valueOf(rs
						.getString("repeat_from")));
				bean.setStatus(rs.getString("status"));
				bean.setClient(rs.getString("client_id"));
				bean.setIssueId(rs.getString("issue_id"));
				bean.setLength(rs.getInt("length"));
				bean.setTimer(t);
				bean.setPriority(TaskPriority.valueOf(rs.getString("priority")));
				bean.setStar(rs.getBoolean("star"));
				bean.setNote(rs.getString("note"));

				bean.setHistories(fetchHistories(ps, conn, bean.getModelId()
						.getId(), DBConstants.selectHistoriesByTask, "task_id"));

				taskList.add((MB) bean);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			rs.close();
			conn.close();
		}
		return taskList;
	}

	private static String findContextIdByUser(Connection con)
			throws SQLException {
		String userName = null;
		String contextId = null;

		try {
			FileReader reader = new FileReader(Main.getInitSettingsFile());
			Properties properties = new Properties();
			properties.load(reader);
			userName = properties.getProperty("userName");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (userName.equalsIgnoreCase(DBConstants.adminUserSystem)) {
			return DBConstants.adminUser;
		}

		ps = con.prepareStatement(DBConstants.selectContextIdBySystemName);
		ps.setString(1, userName);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			contextId = rs.getString(1);
		}

		return contextId;
	}

	private ModelBeanList fetchTaskAssociations(PreparedStatement ps,
			Connection conn, String taskId, String sql, String caller)
			throws SQLException {

		String context = findContextIdByUser(conn);
		ModelBeanList modelBeanList = new ModelBeanList();
		ModelId modelId = null;

		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, taskId);
			if ("context_id".equals(caller)) {
				if (!DBConstants.adminUser.equalsIgnoreCase(context)) {
					ps.setString(2, context);
				}
			}
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				modelId = new ModelId(rs.getString(caller.trim()));
				modelBeanList.add(modelId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return modelBeanList;
	}

	private String fetchFolder(PreparedStatement ps, Connection conn,
			String taskId, String sql, String caller) {

		String folder = null;

		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, taskId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				folder = rs.getString(caller.trim());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return folder;

	}

	private HistoryListBean fetchHistories(PreparedStatement ps,
			Connection conn, String taskId, String sql, String caller) {

		HistoryListBean histBeanList = new HistoryListBean();
		HistoryItemBean bean = null;
		Calendar cal = Calendar.getInstance();

		try {

			ps = conn.prepareStatement(sql);
			ps.setString(1, taskId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bean = new HistoryItemBean();
				bean.setComment(rs.getString("comments"));
				// bean.setBugTrackNumber(rs.getString("bug_track_no"));
				if (rs.getDate("creation_date") == null) {
					bean.setDate(null);
				} else {
					cal.setTime(rs.getDate("creation_date"));
					bean.setDate(DBUtilsSave.convertCalendarToString(cal));
				}
				bean.setHoursWorked(rs.getString("hours_worked"));
				bean.setStatus(rs.getString("status"));
				histBeanList.add(bean);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return histBeanList;

	}

	public List<MB> fetchContextData() throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		List<MB> contextList = new ArrayList<MB>();

		try {
			conn = getDBConnection();
			String context = findContextIdByUser(conn);

			ps = DBConstants.adminUser.equalsIgnoreCase(context) ? conn
					.prepareStatement(DBConstants.selectContexts_ALL) : conn
					.prepareStatement(DBConstants.selectContexts_BYUSER);
			if (!DBConstants.adminUser.equalsIgnoreCase(context)) {
				ps.setString(1, context);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				ContextBean bean = new ContextBean();
				bean = (ContextBean) getModelValues(rs, bean);
				contextList.add((MB) bean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			rs.close();
			conn.close();
		}

		return contextList;
	}

	public List<MB> fetchLocationData() throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		List<MB> locationList = new ArrayList<MB>();

		try {
			conn = getDBConnection();
			ps = conn.prepareStatement(DBConstants.selectLocations);
			rs = ps.executeQuery();

			while (rs.next()) {
				LocationBean bean = new LocationBean();
				bean = (LocationBean) getModelValues(rs, bean);
				locationList.add((MB) bean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			rs.close();
			conn.close();
		}

		return locationList;
	}

	public List<MB> fetchGoalData() throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		List<MB> goalList = new ArrayList<MB>();

		try {
			conn = getDBConnection();
			ps = conn.prepareStatement(DBConstants.selectGoals);
			rs = ps.executeQuery();

			while (rs.next()) {
				GoalBean bean = new GoalBean();
				bean = (GoalBean) getModelValues(rs, bean);
				goalList.add((MB) bean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			rs.close();
			conn.close();
		}

		return goalList;
	}

	public List<MB> fetchTagData() throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		List<MB> tagList = new ArrayList<MB>();
		ModelBean bean = (ModelBean) new Tag(null);

		try {
			conn = getDBConnection();
			ps = conn.prepareStatement(DBConstants.selectTags);
			rs = ps.executeQuery();

			while (rs.next()) {
				bean = getModelValues(rs, (ModelBean) bean);
				tagList.add((MB) bean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			rs.close();
			conn.close();
		}

		return tagList;
	}

	public List<MB> fetchFolderData() throws SQLException {
		ResultSet rs = null;
		Connection conn = null;
		List<MB> folderList = new ArrayList<MB>();

		try {
			conn = getDBConnection();
			ps = conn.prepareStatement(DBConstants.selectFolders);
			rs = ps.executeQuery();

			while (rs.next()) {
				FolderBean bean = new FolderBean();
				bean = (FolderBean) getModelValues(rs, bean);
				folderList.add((MB) bean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		finally {
			rs.close();
			conn.close();
		}

		return folderList;
	}

}
