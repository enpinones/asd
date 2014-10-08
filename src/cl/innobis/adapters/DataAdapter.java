package cl.innobis.adapters;

import innobis.kanban.cl.R;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/* DB.
 * Adapter para datos de task 
 */

public class DataAdapter extends BaseAdapter
{
	//Context y lista de task
	Context context;
	List<String[]> data;
	private static LayoutInflater inflater = null;
		
	public DataAdapter(Context context, List<String[]> data){
		//Constructor para Adapter
		this.context = context;
		this.data = data;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		if (vi == null || position!=6)
			vi = inflater.inflate(R.layout.data_row, null);
		
		//Tomamos otro xml para la fila de la descripción
		if(position==6){
			vi = inflater.inflate(R.layout.data_row_description, null);
		}
				
		//Identificamos las vistas necesarias
		TextView row_title = (TextView) vi.findViewById(R.id.row_title);
		TextView row_content = (TextView) vi.findViewById(R.id.row_content);
		
		//Llenamos los datos en la vista
		//Indice 0 es el título de la fila y 1 es el contenido (nombre, fecha, etc.)
		row_title.setText(data.get(position)[0]);
		row_content.setText(data.get(position)[1]);

		return vi;
	}
	
}
