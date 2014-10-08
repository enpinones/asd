package cl.innobis.adapters;

import java.util.List;

import cl.innobis.model.Tasks;
import cl.innobis.utils.FilterTools;
import innobis.kanban.cl.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author enpinones
 *
 */
public class TaskAdapter extends BaseAdapter
{
	//Context y lista de task
	Context context;
	List<Tasks> data;
	private static LayoutInflater inflater = null;

	public TaskAdapter(Context context, List<Tasks> data) 
	{
		//Constructor para Adapter
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	//Toma el número de items
	@Override
	public int getCount() {
		return data.size();
	}

	//Sacar un item en specifico
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	//Sacar el id de un item
	@Override
	public long getItemId(int position) {
		return position;
	}

	//Generar vista para una fila
	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View vi = convertView;
		if (vi == null)
			vi = inflater.inflate(R.layout.task_row, null);
		//Identificamos las vistas necesarias
		TextView taskNameTextview = (TextView) vi.findViewById(R.id.task_name_textview);
		TextView taskProgressTextview = (TextView) vi.findViewById(R.id.task_progress_textview);
		TextView taskTimeTextview = (TextView) vi.findViewById(R.id.task_time_textview);
		TextView taskNotificationsTextview = (TextView) vi.findViewById(R.id.task_notifications_textview);

		//Hacemos conversiones
		long daysDeadline = FilterTools.GetDaysDifference(data.get(position).getExpected_end_date());
		//Llenamos según corresponda
		taskNameTextview.setText(data.get(position).getName());
		taskProgressTextview.setText(""+data.get(position).getProgress()+"%");
		if(data.get(position).getProgress()!=100)
			taskTimeTextview.setText(daysDeadline+" días");
		else
			taskTimeTextview.setText("");
		taskNotificationsTextview.setText("");

		//Cambios en visualizaciones (colores y tamaños)
		int gray = Color.rgb(141, 147, 161);
		int light_blue = Color.rgb(84, 156, 230);
		int light_red = Color.rgb(236, 106, 90);
		int light_green = Color.rgb(22, 147, 105);
		int white = Color.WHITE;
		LinearLayout taskViewLayout=(LinearLayout)vi.findViewById(R.id.task_view_layout);
		ImageButton viewTaskImagebutton = (ImageButton)vi.findViewById(R.id.view_task_imagebutton);

		//Cambiamos fondo según los días restantes (icono y color)
		if(data.get(position).getProgress()==100)
		{
			viewTaskImagebutton.setBackgroundResource(R.drawable.ticket_icon);
			taskViewLayout.setBackgroundColor(light_green);
		}
		else if(FilterTools.GetDaysDifference(data.get(position).getExpected_end_date()) == 0)
		{
			viewTaskImagebutton.setBackgroundResource(R.drawable.task_icon);
			taskViewLayout.setBackgroundColor(light_blue);
		}
		else if(FilterTools.GetDaysDifference(data.get(position).getExpected_end_date()) < 0)
		{
			viewTaskImagebutton.setBackgroundResource(R.drawable.task_icon);
			taskViewLayout.setBackgroundColor(light_red);
		}
		else
		{
			viewTaskImagebutton.setBackgroundResource(R.drawable.task_icon);
			taskViewLayout.setBackgroundColor(gray);
		}
		
		//Cambiamos el color de las letras
		taskTimeTextview.setTextColor(white);
		taskNameTextview.setTextColor(gray);
		taskProgressTextview.setTextColor(gray);
		
		//DB. Agregamos Listener para el botón de reportar
		//TODO
		
//		final int element = position;
//		Button reportButton = (Button)convertView.findViewById(R.id.report_task_imagebutton);
//
//		reportButton.setOnClickListener(new OnClickListener() 
//		{ 
//		       @Override
//		       public void onClick(View v) 
//		       {
//					Intent intent = new Intent(context,TaskReport.class);
//					intent.putExtra("selected_task_id", data.get(element).getId()); //TODO Agregar id de task seleccionada
//					context.startActivity(intent);
//		       }
//
//		});
		
		return vi;
	}





}
