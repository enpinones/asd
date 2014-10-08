package cl.innobis.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cl.innobis.model.Tasks;

/**
 * @author enpinones
 *
 */
public class FilterTools {

	
	/**
	 * Calcula la diferencia en días entre la fecha entregada y la de hoy
	 * @author enpinones
	 * @param date Fecha a la cual se quiere calcular
	 * @return Diferencia en días
	 */
	public static long GetDaysDifference(Date date) 
	{
		Calendar endDateCalendar = Calendar.getInstance();
		Calendar startDateCalendar = Calendar.getInstance();
		
		startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
		endDateCalendar.setTime(date);
		
		//En caso que la diferencia sea negativa hay que convertir su complemento y luego pasarlo a negativo
	    long end = endDateCalendar.getTimeInMillis();
	    long start = startDateCalendar.getTimeInMillis();
	    long difference = end-start;
	    if(difference>0)
	    	return TimeUnit.MILLISECONDS.toDays(Math.abs(difference));
	    else
	    {
	    	difference*=-1;
	    	return -TimeUnit.MILLISECONDS.toDays(Math.abs(difference));
	    }
	}
	
	
	/**
	 * Filtra las actividades que tienen como fecha de termino la de hoy
	 * @author enpinones
	 * @param currentTasks Lista de tasks orignales
	 * @return Lista de tasks filtrados
	 */
	public static List<Tasks> GetTodayTasks(List<Tasks> currentTasks)
	{
		List<Tasks> filteredTasks = new ArrayList<Tasks>();
		for(Tasks task : currentTasks)
		{
			if(GetDaysDifference(task.getExpected_end_date())==0)
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
	
	/**
	 * Filtra las actividades que tienen como fecha de termino de hoy a una semana más
	 * @author enpinones
	 * @param currentTasks Lista de tasks orignales
	 * @return Lista de tasks filtrados
	 */
	public static List<Tasks> GetOneWeekTasks(List<Tasks> currentTasks)
	{
		List<Tasks> filteredTasks = new ArrayList<Tasks>();
		for(Tasks task : currentTasks)
		{
			if(GetDaysDifference(task.getExpected_end_date())<=7 && GetDaysDifference(task.getExpected_end_date())>=0)
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
	
	/**
	 * Filtra las actividades que tienen como fecha de termino de hoy a tres semanas
	 * @author enpinones
	 * @param currentTasks Lista de tasks orignales
	 * @return Lista de tasks filtrados
	 */
	public static List<Tasks> GetThreeWeekTasks(List<Tasks> currentTasks)
	{
		List<Tasks> filteredTasks = new ArrayList<Tasks>();
		for(Tasks task : currentTasks)
		{
			if(GetDaysDifference(task.getExpected_end_date())<=21 && GetDaysDifference(task.getExpected_end_date())>=0)
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
	
	
	/**
	 * Devuelve todas las task que están expiradas
	 * @author enpinones
	 * @param currentTasks Listado de task a filtrar
	 * @return Lista de tasks filtrados
	 */
	public static List<Tasks> GetExpiredTasks(List<Tasks> currentTasks)
	{
		List<Tasks> filteredTasks = new ArrayList<Tasks>();
		for(Tasks task : currentTasks)
		{
			if(GetDaysDifference(task.getExpected_end_date())<0 && task.getProgress()!=100)
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
	
	
	/**
	 * Obtiene todos los task inactivos
	 * @author enpinones
	 * @param currentTasks Listado de task a filtrar
	 * @return Lista de tasks filtrados
	 */
	public static List<Tasks> GetInactiveTasks(List<Tasks> currentTasks)
	{
		List<Tasks> filteredTasks = new ArrayList<Tasks>();
		for(Tasks task : currentTasks)
		{
			if(task.getProgress()==0)
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
	/**
	 * Obtiene todos los task en progreso
	 * @author enpinones
	 * @param currentTasks Listado de task a filtrar
	 * @return Lista de tasks filtrados
	 */
	public static List<Tasks> GetInProgress(List<Tasks> currentTasks)
	{
		List<Tasks> filteredTasks = new ArrayList<Tasks>();
		for(Tasks task : currentTasks)
		{
			if(task.getProgress()>0 && task.getProgress() <100)
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
	
	
	/**
	 * Obtiene todos los task terminados
	 * @author enpinones
	 * @param currentTasks Listado de task a filtrar
	 * @return Lista de tasks filtrados
	 */
	public static List<Tasks> GetFinishedTasks(List<Tasks> currentTasks)
	{
		List<Tasks> filteredTasks = new ArrayList<Tasks>();
		for(Tasks task : currentTasks)
		{
			if(task.getProgress() == 100)
				filteredTasks.add(task);
		}
		return filteredTasks;
	}
	
}
