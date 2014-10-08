package cl.innobis.backend;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import cl.innobis.model.Comment;
import cl.innobis.model.Project;
import cl.innobis.model.Tasks;
import cl.innobis.model.User;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class Database extends OrmLiteBaseActivity<OrmLiteSqliteOpenHelper> {

	// Esta es la clase que va a ser llamada desde el frontend para obtener los
	// datos necesarios
	// Autor : Cristian Lehuede
	// Voy a usar un singleton para asegurar la consistencia de los datos.
	private DatabaseHelper helper;
	private static Database singleton;
	private final Context context;

	private Database(Context c) {
		this.context = c;
	}

	public static Database getDatabase(Context c) {
		if (singleton == null)
			singleton = new Database(c);

		return singleton;
	}

	/*
	 * Este metodo retorna todos los tasks que estan cargados en la base de
	 * datos Autor : Cristian Lehuede
	 */
	public List<Tasks> getAllTasks() {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Tasks, Integer> tasksDao = helper
				.getTasksRuntimeExceptionDao();
		List<Tasks> allTasks = tasksDao.queryForAll();
		Collections.sort(allTasks);
		return allTasks;

	}

	/*
	 * Retorna el usuario que esta loggeado en la aplicacion Solo hay un usuario
	 * por dispositivo Autor: Cristian Lehuede
	 */

	/**
	 * @author Esteban Pi�ones
	 * @return Retorna un task seg�n un id determinado
	 */
	public Tasks getTask(int id) {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Tasks, Integer> tasksDao = helper
				.getTasksRuntimeExceptionDao();
		return tasksDao.queryForId(id);
	}

	/*
	 * Este metodo retorna todos los projectos Autor : Cristian Lehuede
	 */
	public List<Project> getAllProjects() {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Project, Integer> projectsDao = helper
				.getProjectRuntimeExceptionDao();
		return projectsDao.queryForAll();

	}

	public Project getProject(int id) {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Project, Integer> projectsDao = helper
				.getProjectRuntimeExceptionDao();
		return projectsDao.queryForId(id);

	}

	/*
	 * Retorna todos los comentarios que hay en la base de datos Por ahora este
	 * metodo no va a ser utilizado ya que los comentarios aun no se pueden
	 * agregar Autor: Cristian Lehuede
	 */
	public List<Comment> getComments() {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Comment, Integer> commentsDao = helper
				.getCommentRuntimeExceptionDao();
		return commentsDao.queryForAll();

	}

	/*
	 * Retorna todos los usuarios de la base de datos Autor: Cristian Lehuede
	 */
	public List<User> getUser() {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<User, Integer> userDao = helper
				.getUserRuntimeExceptionDao();
		return userDao.queryForAll();
	}

	/*
	 * 
	 */
	/**
	 * 
	 * Metodo que actualiza un task dado. Autor : Cristian Lehuede
	 * 
	 * @param Task
	 * @return
	 */
	public Boolean updateTask(Tasks task) {
		task.setSync(false); // De esta manera guardo en la base de datos que el
								// task no ha sido actualizado con el servidor.
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Tasks, Integer> tasksDao = helper
				.getTasksRuntimeExceptionDao();

		int valor = tasksDao.update(task);
		// Tengo que llamar al servidor y actualizar el nuevo valor de Task
		RestHandler instancia = new RestHandler(context);
		instancia.sendTask(task);

		if (valor == 1)
			return true;

		return false;

	}

	/*
	 * Java no sopota default parameters, por lo que voy a hacer otro metodo
	 * para recibir los task que ya fueron actualizados conectandose al servidor
	 * No llamar a este metodo, solo si se esta seguro que ya se actualizo!
	 * Autor : Cristian lehuede
	 */
	public Boolean forceUpdateTask(Tasks task) {
		task.setSync(true); // De esta manera guardo en la base de datos que el
							// task no ha sido actualizado con el servidor.
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Tasks, Integer> tasksDao = helper
				.getTasksRuntimeExceptionDao();

		int valor = tasksDao.update(task);
		if (valor == 1)
			return true;

		return false;

	}

	/**
	 * Agrega una lista de task a la base de datos Autor: Cristian Lehuede
	 * 
	 * @param listaTasks
	 */
	public void addTasks(List<Tasks> listaTasks) {

		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Tasks, Integer> tasksDao = helper
				.getTasksRuntimeExceptionDao();

		for (Tasks hTasks : listaTasks) {
			tasksDao.createIfNotExists(hTasks);
		}

	}

	/**
	 * Agrega una lista de proyectos a la base de datos Autor : Cristian Lehuede
	 * 
	 * @param listaProjectos
	 */
	public void addProjects(List<Project> listaProjectos) {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Project, Integer> projectsDao = helper
				.getProjectRuntimeExceptionDao();

		for (Project hProject : listaProjectos) {
			projectsDao.createOrUpdate(hProject);
		}

	}

	/*
	 * Este metodo es el encargado de agregar a la base de datos el usuario que
	 * acaba de hacer inicio de sesion Autor : Cristian Lehuede
	 */
	public void addUser(JSONObject usuario, String password) {

		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<User, Integer> usersDao = helper
				.getUserRuntimeExceptionDao();

		try {
			JSONObject usuarioAgregar = usuario.getJSONObject("user");
			int id = usuarioAgregar.getInt("id");
			String nombre = usuarioAgregar.getString("name");
			String role = usuarioAgregar.getString("role");
			String mail = usuarioAgregar.getString("email");
			String pwdHashed = sha256(password);
			User nuevo = new User(id, nombre, pwdHashed, role, mail);
			usersDao.createIfNotExists(nuevo);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Agrega una lista de comentrios a la base de datos
	 * 
	 * Autor : Cristian Lehuede
	 * 
	 * @param listaComentario
	 */
	public void addComment(List<Comment> listaComentario) {
		helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		RuntimeExceptionDao<Comment, Integer> commentsDao = helper
				.getCommentRuntimeExceptionDao();

		for (Comment h : listaComentario) {
			commentsDao.createIfNotExists(h);
		}
	}

	/*
	 * Metodo encargado de agregar los proyectos recibidos en formato JSON A la
	 * base de datos Autor : Cristian Lehuede
	 */

	public void addProjectsJSON(JSONArray proyectos) {
		List<Project> listaProyectos = new ArrayList<Project>();
		for (int a = 0; a < proyectos.length(); a++) {
			// Recorro toda la lista de proyectos y lo saco a un JSONObject
			try {
				JSONObject proyecto = proyectos.getJSONObject(a);
				int id = Integer.parseInt(proyecto.getString("id"));
				String nombre = proyecto.getString("name");
				String fechaInicio = proyecto.getString("start_date");
				String fechaTermino = proyecto.getString("end_date");
				double progreso = proyecto.getDouble("progress");
				Date fechaInicioReal = getDate(fechaInicio);
				Date fechaTerminoReal = getDate(fechaTermino);
				Project nuevoProject = new Project(id, nombre, fechaInicioReal,
						fechaTerminoReal, progreso, false);
				listaProyectos.add(nuevoProject);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// No debería pasar nunca
				// En un futuro agregar que esto devuelva un bool.
				// Si el bool es falso, hacer exponential backoff.

			}

		}

		addProjects(listaProyectos);

	}

	public void addTasksJSON(JSONArray tasks) {
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		Date currentTime = localCalendar.getTime();

		List<Tasks> listaTareas = new ArrayList<Tasks>();

		for (int a = 0; a < tasks.length(); a++) {

			try {
				JSONObject tarea = tasks.getJSONObject(a);
				int idTarea = tarea.getInt("id");
				String name = tarea.getString("name");
				Date expected_start_date = getDate(tarea
						.getString("expected_start_date"));
				Date expected_end_date = getDate(tarea
						.getString("expected_end_date"));
				Date start_date = expected_start_date;
				Date end_Date = expected_end_date;
				int progress = tarea.getInt("progress");
				int project_id = tarea.getInt("project_id");
				Tasks nuevaTarea = new Tasks(idTarea, name,
						expected_start_date, expected_end_date, start_date,
						end_Date, 1, progress, "Descripcion Generica",
						project_id);
				listaTareas.add(nuevaTarea);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		addTasks(listaTareas);

	}

	private Date getDate(String parseDate) {

		Date parseado = null;
		parseDate = parseDate.substring(0, parseDate.indexOf('T'));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			parseado = sdf.parse(parseDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parseado;
	}

	public static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
