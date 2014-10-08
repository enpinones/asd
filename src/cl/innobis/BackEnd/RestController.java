package cl.innobis.backend;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/*
 * Clase encargada del manejo de la conexión al servidor
 * Todo esto está basado en la librería Volley
 * Gran parte del código fue obtenido de tutoriales.
 * http://www.androidhive.info/2014/05/android-working-with-volley-library-1/
 *  http://arnab.ch/blog/2013/08/asynchronous-http-requests-in-android-using-volley/
 *  
 *  Autor : Cristian Lehuede

 */
public class RestController {

	public static final String TAG = "VolleyPatterns";

	private RequestQueue mRequestQueue;
	private final Context contexto;
	/**
	 * Vamos a usar singleton ya que Volley funciona usando colas Autor :
	 * Cristian Lehuede
	 * 
	 */
	private static RestController sInstance;

	private RestController(Context c) {
		this.contexto = c;

	}

	public static synchronized RestController getInstance(Context c) {
		if (sInstance == null)
			sInstance = new RestController(c);

		return sInstance;

	}

	public RequestQueue getRequestQueue() {
		// Creamos la cola de request.
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(contexto);
		}

		return mRequestQueue;
	}

	/*
	 * Agregamos a la cola los request que vayan llegando. La gracia de usar
	 * Volley es que todo funciona de manera async Autor : Cristian Lehuede
	 */

	public <T> void addToRequestQueue(Request<T> req, String tag) {

		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		VolleyLog.d("Adding request to queue: %s", req.getUrl());

		getRequestQueue().add(req);
	}

	/**
	 * Agrega el request mencionado usando el tag default. Autor: Cristian
	 * Lehuede
	 * 
	 * @param req
	 * 
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);

		getRequestQueue().add(req);
	}

	/**
	 * Cancela todos los request con el tag mencionado. Es clave mandar el tag
	 * ya que es la unica forma de encontrarlo en la cola Autor: Cristian
	 * Lehuede
	 * 
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

}
