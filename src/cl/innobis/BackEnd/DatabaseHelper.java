package cl.innobis.backend;

import innobis.kanban.cl.R;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import cl.innobis.model.Comment;
import cl.innobis.model.Project;
import cl.innobis.model.Tasks;
import cl.innobis.model.User;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME_STRING = "kanbantt.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Tasks, Integer> tasksDao = null;
	private RuntimeExceptionDao<Tasks, Integer> tasksRuntimeDao = null;

	private Dao<Comment, Integer> commentDao = null;
	private RuntimeExceptionDao<Comment, Integer> commentRuntimeDao = null;

	private Dao<User, Integer> userDao = null;
	private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;

	private Dao<Project, Integer> projectDao = null;
	private RuntimeExceptionDao<Project, Integer> projectRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME_STRING, null, DATABASE_VERSION,
				R.raw.ormlite_config);
		// R.raw.ormlite_config es un archivo creado por ORmLite NO EN RUN TIME
		// para optimizar la creacion de la
		// base de datos.
	}

	// Metodo para crear la base de datos.
	// Se crea la tabla Tasks por ahora solamente
	// Autor: Cristian Lehuede
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {
		// TODO Auto-generated method stub
		try {
			// Crea la tabla mencionada
			TableUtils.createTable(connectionSource, Tasks.class);
			TableUtils.createTable(connectionSource, Comment.class);
			TableUtils.createTable(connectionSource, Project.class);
			TableUtils.createTable(connectionSource, User.class);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	// Metodo que se llama cuando la version de la base de datos cambia.
	// Autor: Cristian Lehuede
	@Override
	public void onUpgrade(SQLiteDatabase arg0,
			ConnectionSource connectionSource, int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, Tasks.class, true);
			TableUtils.dropTable(connectionSource, Project.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Comment.class, true);
			onCreate(arg0, connectionSource);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Dao<Tasks, Integer> getDaoTasks() {
		if (tasksDao == null) {
			try {
				tasksDao = getDao(Tasks.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tasksDao;
	}

	public Dao<User, Integer> getDaoUser() {
		if (userDao == null) {
			try {
				userDao = getDao(User.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userDao;
	}

	public Dao<Comment, Integer> getDaoComment() {
		if (commentDao == null) {
			try {
				commentDao = getDao(Comment.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return commentDao;
	}

	public Dao<Project, Integer> getDaoProject() {
		if (projectDao == null) {
			try {
				projectDao = getDao(Project.class);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return projectDao;
	}

	public RuntimeExceptionDao<Tasks, Integer> getTasksRuntimeExceptionDao() {
		if (tasksRuntimeDao == null) {
			tasksRuntimeDao = getRuntimeExceptionDao(Tasks.class);
		}
		return tasksRuntimeDao;
	}

	public RuntimeExceptionDao<Comment, Integer> getCommentRuntimeExceptionDao() {
		if (commentRuntimeDao == null) {
			commentRuntimeDao = getRuntimeExceptionDao(Comment.class);
		}
		return commentRuntimeDao;
	}

	public RuntimeExceptionDao<Project, Integer> getProjectRuntimeExceptionDao() {
		if (projectRuntimeDao == null) {
			projectRuntimeDao = getRuntimeExceptionDao(Project.class);
		}
		return projectRuntimeDao;
	}

	public RuntimeExceptionDao<User, Integer> getUserRuntimeExceptionDao() {
		if (userRuntimeDao == null) {
			userRuntimeDao = getRuntimeExceptionDao(User.class);
		}
		return userRuntimeDao;
	}

}
