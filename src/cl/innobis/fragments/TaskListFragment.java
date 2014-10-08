package cl.innobis.fragments;

import java.util.List;

import cl.innobis.adapters.TaskAdapter;
import cl.innobis.backend.Database;
import cl.innobis.model.Project;
import cl.innobis.model.Tasks;
import cl.innobis.utils.FilterTools;
import innobis.kanban.cl.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;


@SuppressLint("NewApi") public class TaskListFragment extends Fragment {

	private Context context;
	private List<Tasks> allTasks;
	private List<Tasks> filteredTasks;
	private ListView taskListView;
	private Spinner dateSpinner;
	private Spinner progressSpinner;

	public TaskListFragment(){}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_tasklist, container, false);


		//Sacamos una instancia de la base de datos
		Database database = Database.getDatabase(context);
		// Sacamos todos los task y los filtramos
		allTasks = database.getAllTasks();
		filteredTasks = FilterTools.GetTodayTasks(allTasks);

		// Llenamos con todos los task usando adapter propio
		taskListView = (ListView) rootView.findViewById(R.id.task_listview);
		UpdateAdapter();

		// Filtro por progreso
		progressSpinner = (Spinner) rootView.findViewById(R.id.progressSpinner);
		progressSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				FilterDateSpinner(allTasks);
				FilterProgressSpinner(filteredTasks);
				UpdateAdapter();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		// Filtro por fechas
		dateSpinner = (Spinner) rootView.findViewById(R.id.dateSpinner);
		dateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				FilterProgressSpinner(allTasks);
				FilterDateSpinner(filteredTasks);
				UpdateAdapter();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		//Ponemos el nombre del proyecto 
		//TODO Esto es momentaneo hasa que este bien
		TextView topMenuTextview = (TextView)rootView.findViewById(R.id.top_menu_text_view);
		List<Project> projects = database.getAllProjects();
		if(projects.size()>0)
			topMenuTextview.setText(projects.get(0).getName());
		else
			topMenuTextview.setText("Un proyecto");
		return rootView;
	}



	/**
	 * Filtra segï¿½n la fecha de un task
	 * 
	 * @author enpinones
	 * @param currentTasks
	 *            Listado de task a filtrar
	 */
	private void FilterDateSpinner(List<Tasks> currentTasks) {
		if (dateSpinner.getSelectedItemPosition() == 0) {
			filteredTasks = FilterTools.GetTodayTasks(currentTasks);
		} 
		else if (dateSpinner.getSelectedItemPosition() == 1) {
			filteredTasks = FilterTools.GetOneWeekTasks(currentTasks);
		} 
		else if (dateSpinner.getSelectedItemPosition() == 2) {
			filteredTasks = FilterTools.GetThreeWeekTasks(currentTasks);
		}
		else if(dateSpinner.getSelectedItemPosition() == 3)
		{
			filteredTasks = FilterTools.GetExpiredTasks(currentTasks);
		}
		else if(dateSpinner.getSelectedItemPosition() == 4)
		{
			filteredTasks=allTasks;
		}
	}

	/**
	 * Filtra segï¿½n el progeso de una actividad
	 * 
	 * @author enpinones
	 * @param currentTasks
	 *            Listado de task a filtrar
	 */
	private void FilterProgressSpinner(List<Tasks> currentTasks) {
		// Todas
		if (progressSpinner.getSelectedItemPosition() == 0) {
			filteredTasks = currentTasks;
		}
		// Inactivas
		else if (progressSpinner.getSelectedItemPosition() == 1) {
			filteredTasks = FilterTools.GetInactiveTasks(currentTasks);
		}
		// En proceso
		else if (progressSpinner.getSelectedItemPosition() == 2) {
			filteredTasks = FilterTools.GetInProgress(currentTasks);
		}
		// Terminadas
		else if (progressSpinner.getSelectedItemPosition() == 3) {
			filteredTasks = FilterTools.GetFinishedTasks(currentTasks);
		}
	}

	private void UpdateAdapter() {
		taskListView.setAdapter(new TaskAdapter(context, filteredTasks));

		taskListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// Creamos el fragmento y la transaction
				Fragment fragment = new TaskViewFragment();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				// Hacemos replace del fragment actual
				transaction.replace(R.id.frame_container, fragment);
				transaction.addToBackStack(null);
				// Agregamos información extra del task seleccionado
				Bundle bundle = new Bundle();
				bundle.putInt("selected_task_id", filteredTasks.get(position).getId());
				fragment.setArguments(bundle);
				// Hacemos commit de la transacción
				transaction.commit();


			}
		});


	}


}
