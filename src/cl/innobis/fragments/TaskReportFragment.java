package cl.innobis.fragments;

import java.util.ArrayList;
import java.util.List;

import cl.innobis.backend.Database;
import cl.innobis.model.Project;
import cl.innobis.model.Tasks;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("NewApi") public class TaskReportFragment extends Fragment {

	private int taskId;
	private Tasks task;
	private Database database;
	private TextView projectNameView;
	private Context context;
	private View currentRootView;

	public TaskReportFragment(){}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_taskreport, container, false);
		currentRootView = rootView;
		//Esteban Piñones
		//Menu de arriba
		//VisualTools.TaskMenuSetup(this,TaskMenuHover.REPORT);

		// Se obtiene el nombre del task y luego se obtiene el objeto task.
		TextView nameView = (TextView) rootView.findViewById(R.id.taskName);

		database = Database.getDatabase(context);
		Bundle bundle = this.getArguments();
		taskId = bundle.getInt("reported_task_id", 0);
		task = database.getTask(taskId);

		nameView.setText(task.getName());

		// Se cambia el name view con el nombre real del proyecto
		//TODO Esto es momentaneo hasa que este bien
		String projectName = "";
		projectNameView = (TextView) rootView.findViewById(R.id.top_menu_text_view);

		//Ponemos el nombre del proyecto 
		List<Project> projects = database.getAllProjects();
		if(projects.size()>0)
			projectName = projects.get(0).getName();
		else
			projectName = "Un proyecto";

		projectNameView.setText(projectName);

		// Cambio de nombre a TextView de comentarios
		TextView commentsView = (TextView) rootView.findViewById(R.id.task_report_comments_text_view);
		commentsView.setText("Comentarios (Opcional)");

		// Genera lista solo con porcentajes mayores al avance actual
		// TODO Reubicar declaración de variables. 
		List<String> percentagesList = new ArrayList<String>();
		int progress = task.getProgress();		

		for(int i=0;i<=10;i++){
			if(progress<=(i*10)){
				percentagesList.add((i*10)+"%");
			}
		}

		// Se agregan los porcentajes al spinner
		Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, percentagesList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		//Esteban Piñones
		//Selecciona un valor en el spinner dependiendo del progreso que ya tenía la actividad
		spinner.setSelection(0);

		// Listener cuando se hace click en Aceptar
		Button accept = (Button) rootView.findViewById(R.id.button1);
		accept.setText("Aceptar");

		accept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Que hacer cuando le hacen click

				// Captura seleccion de spinner
				Spinner spinner = (Spinner) currentRootView.findViewById(R.id.spinner1);
				Object selectedItem = spinner.getSelectedItem();
				//int  newProgress = (Integer) selectedItem;
				int newProgress =Integer.parseInt(selectedItem.toString().replace("%", ""));
				// Captura comentario ingresado
				EditText comment_view = (EditText) currentRootView.findViewById(R.id.editText1);
				String comments = comment_view.getText().toString();

				setProgress(newProgress, comments);
				
				// Creamos el nuevo fragment 
				Fragment fragment = new TaskViewFragment();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				// Cambiamos el fragment por el nuevo
				transaction.replace(R.id.frame_container, fragment);
				transaction.addToBackStack(null);
				// Agregamos la información extra
				Bundle bundle = new Bundle();
				bundle.putInt("selected_task_id", task.getId());
				fragment.setArguments(bundle);
				// Hacemos commit del transaction
				transaction.commit();
				
			}

			private void setProgress(int newProgress, String comments) {
				// Enviar reporte con nuevo nivel de avance
				if (newProgress > task.getProgress()) {
					task.setProgress(newProgress);

					//TODO Verificar que update esté OK
					database.updateTask(task);
				}
			}
		});
		
		return rootView;
	}
}
