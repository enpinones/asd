package cl.innobis.backend;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Este metodo es llamado cuando se activa la alarma.
		// Tengo el context por lo que puedo desde aca realizar el manejo
		// directo con el RestHandler
		Log.d("KANBANTT", "Alarma llamada");
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// Tengo que ver si es que hay conexión, de lo contrario hago otra
		// alarma para 1 minuto mas
		if (!RestHandler.isNetworkAvailable(context)) {
			// No hay conexion todavia
			cl.innobis.backend.AlarmManager.getInstance(context)
					.scheduleAlarm(); // Seteo otra alarma para 1 minuto más y
										// retorno
			return;
		} else {
			// Tengo conexion, sincronizemos todo con el servidor
			RestHandler handlerConexion = new RestHandler(context);
			boolean response = handlerConexion.syncWithServer();
			if (response) {
				// Todo salio bien, no vuelvo a activar la alarma.
				return;
			}
			// Hubo problemas en el envio al servidor, probablemente mala señal.
			// vuelvo a llamar a la alarma en un minuto mas
			cl.innobis.backend.AlarmManager.getInstance(context)
					.scheduleAlarm();

		}

	}

}
