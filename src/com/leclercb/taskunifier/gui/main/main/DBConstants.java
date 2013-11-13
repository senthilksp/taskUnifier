package com.leclercb.taskunifier.gui.main.main;

public class DBConstants {

        public static final String DB_DRIVER            = "DB_DRIVER";
        public static final String DB_CONNECTION        = "DB_CONNECTION";
        public static final String DB_USER              = "DB_USER";
        public static final String DB_PASSWORD          = "DB_PASSWORD";
       

        public static final String insertTask = "insert into tasks_ "
                     + "(modelid,modelstatus,modelcreationdate, modelupdatedate,title,order_id,parent,progress,completed,startdate,"
                     + "start_date_reminder,duedate,due_date_remainder,repeat, repeat_from, status, length, timer , priority , star , note,client_id,issue_id) "
                     + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;

        public static final String insertContacts = "insert into contacts_ (modelid,modelstatus,modelcreationdate,modelupdatedate,title,order_id,firstname,lastname,email) "
                     + "values (?,?,?,?,?,?,?,?,?)";

        public static final String insertContexts = "insert into contexts_ (modelid,modelstatus,modelcreationdate,modelupdatedate,title,order_id) "
                     + "values (?,?,?,?,?,?)";

        public static final String insertNotes = "insert into notes_ (modelid,modelstatus,modelcreationdate,modelupdatedate,title,order_id) "
                     + "values (?,?,?,?,?,?)";

        public static final String insertGoals = "insert into goals_ (modelid,modelstatus,modelcreationdate,modelupdatedate,title,order_id, level,archived,color) "
                     + "values (?,?,?,?,?,?,?,?,?)";

        public static final String insertFolders = "insert into folders_ (modelid,modelstatus,modelcreationdate,modelupdatedate,title,order_id,archived) "
                     + "values (?,?,?,?,?,?,?)";

        public static final String insertLocations = "insert into locations_(modelid,modelstatus,modelcreationdate,modelupdatedate, "
                     + "title,order_id,level,latitude,longitude) values(?,?,?,?,?,?,?,?,?)";
       
        public static String insertTask_Contacts    = "insert into task_contacts_(task_id,contact_id) values(?,?);";
        public static String insertTask_Contexts    = "insert into task_contexts_(task_id,context_id) values(?,?);";
        public static String insertTask_Locations   = "insert into task_locations_(task_id,location_id) values(?,?)";
        public static String insertTask_Goals       = "insert into task_goals_(task_id,goal_id) values(?,?)";
        public static String insertTask_tags        = "insert into task_tags_(task_id,tag_id) values(?,?)";
        public static String insertTask_histories   = "insert into task_history_(task_id,comments,status,creation_date,hours_worked,bug_track_no) values(?,?,?,?,?,?)";
       
        public static String updateTaskTableContext = "update tasks_ set context_id = ? where modelid = ?";
        public static String updateTaskTableFolder  = "update tasks_ set folder_id = ? where modelid = ?";
       
       
        public static final String updateTask = "update tasks_ set modelstatus=?,modelcreationdate=?, modelupdatedate=?,title=?," +
                      "order_id=?,parent=?,progress=?,completed=?,startdate=?,start_date_reminder=?,duedate=?,due_date_remainder=?,repeat=?,repeat_from=?," +
                      "status=?,length=?,timer=?,priority=?,star=?,note=?, client_id=?, issue_id=? where modelid = ?";
       
        public static final String updatecontact = "update contacts_ set modelstatus=?,modelcreationdate=?, modelupdatedate=?,title=?," +
                      "order_id=?,firstname=?,lastname=?,email=? where modelid = ?";
       
        public static String updateContext= "update contexts_ set modelstatus=?,modelcreationdate=?, modelupdatedate=?,title=?," +
                      "order_id=? where modelid = ?" ;
       
        public static String updateFolder= "update folders_ set modelstatus=?,modelcreationdate=?," +
                      "modelupdatedate=?,title=?,order_id=?,archived=? where modelid=?";
       
        public static String updateGoal= "update goals_ set modelstatus=?, modelcreationdate=?, modelupdatedate=?, " +
                      "title=?, order_id=?, level=?, archived=?, color=? where modelid=?";
       
        public static String updateLocation = "update locations_ set modelstatus=?, modelcreationdate=?, modelupdatedate=?, " +
                      "title=?, order_id=?, level=?, latitude=?, longitude=? where modelid=?";
       
        public static String updateNote = "update notes_ set modelstatus=?, modelcreationdate=?, modelupdatedate=?, title=?, order_id=? where modelid=?";
       
       
        public static String deleteTask_Contacts    = "delete from task_contacts_ where task_id=?";
        public static String deleteTask_Contexts    = "delete from task_contexts_ where task_id=?";
        public static String deleteTask_Locations   = "delete from task_locations_ where task_id=?";
        public static String deleteTask_Goals       = "delete from task_goals_ where task_id=?";
        public static String deleteTask_tags        = "delete from task_tags_ where task_id=?";
        public static String deleteTask_Histories   = "delete from task_history_ where task_id=?";
		
		
       
       
        public static final String selectTasksIdOnly      = "select modelid from tasks_ where modelid=? ";
        
        public static final String selectContactsIdOnly  = "select modelid from contacts_ where modelid=?";
        public static final String selectContextsIdOnly  = "select modelid from contexts_ where modelid=?";
        public static final String selectGoalsIdOnly     = "select modelid from goals_ where modelid=?";
        public static final String selectFoldersIdOnly   = "select modelid from folders_ where modelid=?";
        public static final String selectNotesIdOnly     = "select modelid from notes_ where modelid=?";
        public static final String selectLocationsIdOnly = "select modelid from locations_ where modelid=?";
        public static final String selectClientsIdOnly   = "select client_id from clients_ where client_id=?";
       
       
        public static final String selectTasks_ALL        = "select * from tasks_  where modelstatus = 'TO_UPDATE'" ;
        public static final String selectTasks_BYUSER     = "select * from tasks_  where modelstatus = 'TO_UPDATE' and context_id = ?";
        
        
        public static final String selectContacts        = "select * from contacts_ where modelstatus = 'TO_UPDATE'";
       
        public static final String selectContexts_ALL    = "select * from contexts_ where modelstatus = 'TO_UPDATE'";
        public static final String selectContexts_BYUSER = "select * from contexts_ where modelstatus = 'TO_UPDATE' and modelid = ?";
       
        public static final String selectGoals           = "select * from goals_  where modelstatus = 'TO_UPDATE'";
        public static final String selectFolders         = "select * from folders_ where modelstatus = 'TO_UPDATE'";
        public static final String selectNotes           = "select * from notes_ where modelstatus = 'TO_UPDATE'";
        public static final String selectLocations       = "select * from locations_ where modelstatus = 'TO_UPDATE'";
        public static final String selectClients         = "select * from clients_ where modelstatus = 'TO_UPDATE'";
        public static final String selectTags            = "select * from tags_ where modelstatus = 'TO_UPDATE'";
       
       
        public static final String selectContactsByTask        = "select * from task_contacts_ where task_id=?";
        
        public static final String selectContextsByTask_ALL    = "select * from task_contexts_ where task_id=?";
        public static final String selectContextsByTask_BYUSER = "select * from task_contexts_ where task_id=? and context_id =?";
        
        
        public static final String selectGoalsByTask           = "select * from task_goals_ where task_id=?";
        public static final String selectLocationsByTask       = "select * from task_locations_ where task_id=?";
        public static final String selectFoldersByTask         = "select * from task_folders_ where task_id=?";
        public static final String selectHistoriesByTask       = "select * from task_history_ where task_id=?";
       
        public static final String selectContextIdBySystemName = "select modelid from contexts_ where system_login=?";
        public static final String selectFolderFromTaskTable   = "select folder_id from tasks_ where modelid=?";
       

        public static final String MADRONE_PROP_FILE  = "madrone.properties";
        public static final String adminUserSystem 	  = "Kumaravel"; 
        public static final String adminUser 		  = "ADMIN_USER";
        public static final String userName		      = "UserName";
     
        
}

