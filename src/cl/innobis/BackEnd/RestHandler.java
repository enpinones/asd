package cl.innobis.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import cl.innobis.model.Tasks;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.splunk.mint.Mint;

public class RestHandler {
	/*
	 * Clase que se va a llamar desde el frontend para la comunicacion con el
	 * servidor Esta clase se conecta directamente con RestController, es por
	 * eso que pido como parametro Context. Autor : Cristián Lehuedé
	 */
	private final RestController controlador;
	private final String URL_UPDATE_TASK = "http://especial4.ing.puc.cl/api/projects/";
	private final Context context;
	private final Database db;

	public RestHandler(Context c) {

		controlador = RestController.getInstance(c);
		this.context = c;
		db = Database.getDatabase(context);
	}

	/*
	 * Metodo encargado de enviar los updates de Task al servidor. Autor :
	 * Cristian Lehuede
	 */
	public void sendTask(final Tasks taskToUpdate) {
		if (!isNetworkAvailable(context)) {
			// Aca debo llamar al alarm manager y que intente nuevamente en 5
			// minutos. Debe revisar toda la base de datos
			// y buscar lo que aun no se ha actualizado con el servidor.
			AlarmManager.getInstance(context).scheduleAlarm();
			Log.d("KANBANTT", "No hay internet");
			return;
		}
		String URL = URL_UPDATE_TASK + taskToUpdate.getProject_id() + "/tasks/"
				+ taskToUpdate.getID();

		StringRequest patchRequest = new StringRequest(Request.Method.PUT, URL,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						// Revisar lo que me dijo el servidor

						try {
							JSONObject taskFromServer = new JSONObject(response);
							JSONObject task = taskFromServer
									.getJSONObject("task");
							int progreso = task.getInt("progress");
							if (progreso == taskToUpdate.getProgress()) {
								// Se hizo todo OK y se actualizo en el servidor
								taskToUpdate.setSync(true);
								db.forceUpdateTask(taskToUpdate);
								Log.d("KANBANTT",
										"Task correctamente actualizado en el servidor");
							} else {
								// Fueron distintos y no se actualizo
								// correctamente
								Log.d("KANBANTT",
										"El progreso de la tarea remota es "
												+ progreso);
								Log.d("KANBANTT",
										"El progreso de la tarea local es "
												+ taskToUpdate.getProgress());
								AlarmManager.getInstance(context)
										.scheduleAlarm();

							}
						} catch (Exception e) {
							// Error en el parseo, claramente hay un error en la
							// conexion al servidor.
							// No actualizo el task para volver a intentar
							// nuevamente
							// Llamo a Mint
							AlarmManager.getInstance(context).scheduleAlarm();
							Mint.logException(e);
							Log.d("KANBANTT", "Exception thrown");
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error en el servidor. lo logeo a mint para que me
						// llegue al mail
						// Tambien debo llamar al alarm manager para que vuelva
						// a tratar en un rato mas
						AlarmManager.getInstance(context).scheduleAlarm();

						Log.d("Error.Response", error.getMessage().toString());
						Mint.logException(error);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("progress",
						String.valueOf(taskToUpdate.getProgress()));
				return params;
			}
		};
		// add the request object to the queue to be executed
		patchRequest
				.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 20, 1.7f));
		controlador.addToRequestQueue(patchRequest);
	}

	public static boolean isNetworkAvailable(Context context) {
		return ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo() != null;
	}

	public boolean syncWithServer() {
		// Debo recorrer todas los tasks que tengan sync en false. Eso significa
		// que han sido modificados de manera local
		// Pero en el servidor no
		controlador.cancelPendingRequests("TAG");
		List<Tasks> taskList = db.getAllTasks();
		for (Tasks tasks : taskList) {
			if (!tasks.getSyncStatus()) {
				// Debo sincronizar este task con el servidor
				sendTask(tasks);
				// Si por alguna razon llega a fallar el envio del task,
				// automaticamente el metodo
				// sendTask va a llamar al alarmManager y setear nuevamente la
				// alarma en 1 minuto
				// Todo esto hasta que se logre la sincronizacion total

			}
		}

		return true;

	}

}
