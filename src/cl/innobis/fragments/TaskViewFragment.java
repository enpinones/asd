package cl.innobis.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.innobis.adapters.DataAdapter;
import cl.innobis.backend.Database;
import cl.innobis.model.Project;
import cl.innobis.model.Tasks;
import cl.innobis.utils.TaskMenuHover;
import cl.innobis.utils.VisualTools;
import innobis.kanban.cl.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi") public class TaskViewFragment extends Fragment {

	// Diego Barriga
	// Vista de los datos de cada Task

	private int taskId;
	private Tasks task;
	private ListView taskDataView;
	private TextView projectNameView;
	private String projectName;
	private List<String[]> data;
	private Database database;
	private Activity activity;

	public TaskViewFragment(){}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity=activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_taskview, container, false);

		//Esteban Piñones
		//Modificado para conectar a la base de datos y sacar la task.
		database = Database.getDatabase(activity);

		Bundle bundle = this.getArguments();
		taskId = bundle.getInt("selected_task_id", 0);
		task = database.getTask(taskId);

		//Esteban Piñones
		//Menu de arriba, colores
		VisualTools.TaskMenuSetup(rootView,TaskMenuHover.TASK);
		

		// DB. Completo la vista con los datos de la task.
		// Se cambia el name view con el nombre real del proyecto
		projectName = database.getTask(task.getProject_id()).getName();
		projectNameView = (TextView) rootView.findViewById(R.id.top_menu_text_view);
		//Ponemos el nombre del proyecto 
		//TODO Esto es momentaneo hasa que este bien
		List<Project> projects = database.getAllProjects();
		if(projects.size()>0)
			projectName = projects.get(0).getName();
		else
			projectName = "Un proyecto";

		projectNameView.setText(projectName);

		data = generate_list();

		taskDataView = (ListView) rootView.findViewById(R.id.task_dataview);
		taskDataView.setAdapter(new DataAdapter(activity, data));

		taskDataView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Abre dialogo para reportar avance.
				if (position == 1) {	
					
					// Creamos el nuevo fragment y el transaction
					Fragment fragment = new TaskReportFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					// Cambiamos el fragment
					transaction.replace(R.id.frame_container, fragment);
					transaction.addToBackStack(null);
					// Agregamos info extra
					Bundle bundle = new Bundle();
					bundle.putInt("reported_task_id", task.getId());
					fragment.setArguments(bundle);
					// Hacemos commit del transaction
					transaction.commit();
				}
				// Abre vista de actividad padre
				else if(position == 4){
//					
					
					// Creamos el nuevo fragment y el transaction
					Fragment fragment = new TaskViewFragment();
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					// Cambiamos el fragment
					transaction.replace(R.id.frame_container, fragment);
					transaction.addToBackStack(null);
					// Agregamos info extra
					Bundle bundle = new Bundle();
					bundle.putInt("selected_task_id", task.getParent_id());
					fragment.setArguments(bundle);
					// Hacemos commit del transaction
					transaction.commit();
				}

			}
		});

		//@author: Esteban Piñones
		//Listeners para los botones del menu superior
		//Boton de ver actividad
		Button taskMenuReportButton= (Button) rootView.findViewById(R.id.task_menu_report_button);
		taskMenuReportButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(activity, TaskReport.class);
//				intent.putExtra("reported_task_id", task.getId());
//				startActivity(intent);
			}
		});

		//Boton de comentarios
		Button taskMenuCommentButton= (Button) rootView.findViewById(R.id.task_menu_comment_button);
		taskMenuCommentButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Falta la redirección a comentario
			}
		});

		return rootView;
	}

	@SuppressLint("SimpleDateFormat") private List<String[]> generate_list() {
		// Crea la lista que contiene los datos que irán en la vista de la
		// actividad.

		//TODO Se duplica esta asignación de task, para que al hacer update, se reconozcan los nuevos datos.
		task = database.getTask(taskId);

		List<String[]> list = new ArrayList<String[]>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		String[][] listAux = new String[7][2];
		String nameAux = "";

		listAux[0][0] = "Nombre";
		listAux[0][1] = task.getName();

		listAux[1][0] = "Avance";
		listAux[1][1] = String.valueOf(task.getProgress()) + "%";

		listAux[2][0] = "Inicio";
		listAux[2][1] = sdf.format(task.getExpected_start_date());// +sdf.format(task.getStart_date());

		listAux[3][0] = "Término";
		listAux[3][1] = sdf.format(task.getExpected_end_date());// +sdf.format(task.getEnd_date());

		listAux[4][0] = "Actividad Padre";
		nameAux = database.getTask(task.getParent_id()).getName(); 

		if(nameAux.length()>15)
			nameAux = nameAux.substring(0, 12)+"...";
		listAux[4][1] = nameAux;

		//TODO Implementar ver actividades predecesoras
		listAux[5][0] = "Actividades Predecesoras";
		listAux[5][1] = ">";

		listAux[6][0] = "Descripción";
		listAux[6][1] = task.getDescription();

		for(int i=0;i<listAux.length;i++){
			list.add(listAux[i]);
		}

		return list;
	}
}
