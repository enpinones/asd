package cl.innobis.frontend;

import innobis.kanban.cl.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cl.innobis.backend.Database;
import cl.innobis.backend.RestController;
import cl.innobis.model.User;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.splunk.mint.Mint;

public class Login extends Activity {
	private ProgressDialog pDialog;
	private RestController controlador;
	private final String URLProject = "http://especial4.ing.puc.cl/api/projects";
	private final String uRLTasksString = "http://especial4.ing.puc.cl/api/projects/1/tasks";
	private final String urlLogin = "http://especial4.ing.puc.cl/api/users/login";
	private Context c;
	private Database db;
	private EditText mail;
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(Login.this, "302f2589");
		Mint.enableDebug();
		setContentView(R.layout.activity_login);
		controlador = RestController.getInstance(this);
		this.c = this;
		db = Database.getDatabase(c);
		// Momentaneo para redirigir a la lista de actividades
		Button button = (Button) findViewById(R.id.buton_login);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Antes de llamar al metodo debo primero revisar que los datos
				// ingresados sean correcto

				// HARDCODE DE LOGIN
				EditText userEditText = (EditText) findViewById(R.id.e_username);
				EditText passwordEditText = (EditText) findViewById(R.id.e_password);
				userEditText.setText("arturo@gmail.com");
				passwordEditText.setText("grupo4grupo4");
				// AQUI TERMINA

				Boolean datosValidos = validarDatos();
				if (datosValidos)
					Sincronizar();

			}

		});

	}

	private Boolean validarDatos() {
		// TODO Auto-generated method stub
		Boolean correcion = true;
		mail = (EditText) findViewById(R.id.e_username);
		password = (EditText) findViewById(R.id.e_password);
		if (!isValidEmail(mail.getText().toString())) {
			mail.setError("Mail invalido");
			correcion = false;
		}
		return correcion;

	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	/**
	 * Este es el metodo encargado de conectarse al servidor y descargar todos
	 * los datos necesarios para la base de datos Previamente debe ocurrir el
	 * login que no existe por ahora. Autor: Cristian Lehuede
	 */
	private void Sincronizar() {
		pDialog = new ProgressDialog(this);
		pDialog.setTitle("Sincronizando");
		pDialog.setMessage("Espere mientras nos conectamos al servidor");
		login(mail.getText().toString(), password.getText().toString());
	}

	/**
	 * Este metodo se va a conectar al servidor y traer los proyectos Autor:
	 * Cristian Lehuede
	 */
	private void getProjects() {
		JsonArrayRequest req = new JsonArrayRequest(URLProject,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							VolleyLog.v("Response:%n %s", response.toString(4));
							db.addProjectsJSON(response);
							getTasks();
						} catch (JSONException e) {
							e.printStackTrace();
							// Ocurrió un error en el proceso.
							Toast.makeText(c, "Error conectando",
									Toast.LENGTH_LONG).show();
							pDialog.hide();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Log.d("KANBANTT", error.getMessage());
						Toast.makeText(c, "Error conectando", Toast.LENGTH_LONG)
								.show();
						pDialog.hide();
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("httpuserid", "2");
				return headers;
			}
		};
		controlador.addToRequestQueue(req);
	}

	/**
	 * 
	 * 
	 * Este metodo se llama despues de haber llamado a get projects. Es el que
	 * va a traer todos los tasks Autor : Cristian Lehuede
	 */
	private void getTasks() {
		JsonArrayRequest req = new JsonArrayRequest(uRLTasksString,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							VolleyLog.v("Response:%n %s", response.toString(4));
							db.addTasksJSON(response);
							pDialog.hide();
							
							Intent i = new Intent(Login.this, MainActivity.class);
							//Intent i = new Intent(Login.this, TaskList.class);
							startActivity(i);
							
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Error: ", error.getMessage());
						Log.d("KANBANTT", error.getMessage());
					}
				}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("httpuserid", "2");
				return headers;
			}
		};
		controlador.addToRequestQueue(req);
	}

	private void login(final String username, final String password) {
		pDialog.show();

		StringRequest postRequest = new StringRequest(Request.Method.POST,
				urlLogin, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						// Revisar lo que me dijo el servidor
						if (response.length() == 0) {
							// Significa que esta mal el user o password
							Toast.makeText(c,
									"Usuario o contraseña incorrecto",
									Toast.LENGTH_SHORT).show();
							pDialog.hide();
						} else {
							try {
								JSONObject respuesta = new JSONObject(response);
								db.addUser(respuesta, password);
								getProjects();
							} catch (Exception e) {

								e.printStackTrace();
							}

						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						pDialog.hide();
						// error de conexion, veamos si el usuario ya ha entrado
						// antes
						Log.d("Error.Response", error.getMessage().toString());
						List<User> getAllUsers = db.getUser();
						if (getAllUsers.size() == 0) // Nunca nadie ha iniciado
														// sesion.
							return;
						// Si estoy aca es porque hay un usuario ya registrado
						User userLogged = getAllUsers.get(0);
						String email = userLogged.getMail();
						String passwordHashed = userLogged.getPassword();
						Log.d("KANBANTT", "El email guardado es " + email);
						Log.d("KANBANTT", "El password guardado es "
								+ passwordHashed);
						Log.d("KANBANTT",
								"El password que paso es "
										+ Database.sha256(password));
						Log.d("KANBANTT", userLogged.toString());

						if (email.equalsIgnoreCase(username)
								&& passwordHashed.equalsIgnoreCase(Database
										.sha256(password))) {
							// El usuario se conecto sin internet, demosle
							// acceso igual
							new AlertDialog.Builder(c)
									.setTitle("Acceso Offline")
									.setMessage(
											"Por falta de conexión, se ha iniciado sesión de manera offline. Todo el trabajo se guardará y se enviará cuando la conexión retorne")
									.setPositiveButton(
											"Aceptar",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													// Cargamos acceso a la
													// proxima ventana
													Intent i = new Intent(
															Login.this,
															MainActivity.class);
													startActivity(i);

												}
											})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();

						} else {
							// No le damos acceso
							Toast.makeText(
									c,
									"Problemas de conexion, intentar nuevamente",
									Toast.LENGTH_SHORT).show();
						}

					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("email", username);
				params.put("password", password);
				return params;
			}
		};
		controlador.addToRequestQueue(postRequest);

	}

}
