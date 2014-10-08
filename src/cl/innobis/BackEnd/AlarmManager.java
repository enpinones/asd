package cl.innobis.backend;

import java.util.GregorianCalendar;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/*
 * Al igual que las otras clases. Utilizamos el patron de diseño Singleton
 * Esto es clave ya que solo una clase es la encargada del manejo de alarmas
 * lo mismo que con la base de datos y el envio al servidor
 * Autor : Cristian Lehuede
 */
public class AlarmManager {

	private final Context contexto;
	private static AlarmManager classInstance;

	private AlarmManager(Context c) {
		this.contexto = c;
	}

	/*
	 * Obtiene una instancia de esta clase para poder llamar al metodo
	 * scheduleAlarm
	 */
	public static AlarmManager getInstance(Context appContext) {
		if (classInstance == null)
			classInstance = new AlarmManager(appContext);

		return classInstance;
	}

	/*
	 * Activa la alarma para 1 minutos mas
	 */
	public void scheduleAlarm() {
		// Ponemos la alarma para 5 minutos mas. recordar que esta en
		// milisegundos
		Log.d("KANBANTT", "Alarma setead!");
		Long time = new GregorianCalendar().getTimeInMillis() + 1 * 60 * 1000;

		Intent intentAlarm = new Intent(contexto, AlarmReceiver.class);

		// instanciamos un alarmmanager
		android.app.AlarmManager alarmManager = (android.app.AlarmManager) contexto
				.getSystemService(Context.ALARM_SERVICE);

		// seteamos la alarma.

		alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, time,
				PendingIntent.getBroadcast(contexto, 1, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));

	}
}
