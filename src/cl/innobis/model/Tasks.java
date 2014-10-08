package cl.innobis.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

//Clase creada por Cristian Lehuede
//Esta clase contiene el modelo de Tasks.
// Se esta usando ORMLite
public class Tasks implements Comparable {

	@DatabaseField(id = true)
	int id;
	@DatabaseField
	String name;
	@DatabaseField
	Date expected_start_date;
	@DatabaseField
	Date expected_end_date;
	@DatabaseField
	Date start_date;
	@DatabaseField
	Date end_date;
	@DatabaseField
	boolean sync;
	@DatabaseField
	int parent_id;
	@DatabaseField
	int progress;
	@DatabaseField
	String description;
	@DatabaseField
	int project_id;

	// Metodo constructor
	// Autor Cristian Lehuede
	public Tasks(int id, String name, Date expected_start_date,
			Date expected_end_date, Date start_date, Date end_date,
			int parent_id, int progress, String description, int project_id) {
		super();
		this.id = id;
		this.name = name;
		this.expected_start_date = expected_start_date;
		this.expected_end_date = expected_end_date;
		this.start_date = start_date;
		this.end_date = end_date;
		this.parent_id = parent_id;
		this.progress = progress;
		this.description = description;
		this.project_id = project_id;
		this.sync = true;
	}

	public Tasks() {

	}

	public void setId(int id) {
		this.id = id;
	}

	// Metodo toString , imprime los valores si es que no
	// son nulos.
	// Autor Cristian Lehuede
	@Override
	public String toString() {
		return "Tasks ["
				+ (name != null ? "name=" + name + ", " : "")
				+ (expected_start_date != null ? "expected_start_date="
						+ expected_start_date + ", " : "")
				+ (expected_end_date != null ? "expected_end_date="
						+ expected_end_date + ", " : "")
				+ (start_date != null ? "start_date=" + start_date + ", " : "")
				+ (end_date != null ? "end_date=" + end_date + ", " : "")
				+ "parent_id="
				+ parent_id
				+ ", progress="
				+ progress
				+ ", "
				+ (description != null ? "description=" + description + ", "
						: "") + "project_id=" + project_id + "]";
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getExpected_start_date() {
		return expected_start_date;
	}

	public Date getExpected_end_date() {
		return expected_end_date;
	}

	public int getID() {
		return id;
	}

	public Date getStart_date() {
		return start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public int getParent_id() {
		return parent_id;
	}

	public int getProgress() {
		return progress;
	}

	public String getDescription() {
		return description;
	}

	public int getProject_id() {
		return project_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExpected_start_date(Date expected_start_date) {
		this.expected_start_date = expected_start_date;
	}

	public void setExpected_end_date(Date expected_end_date) {
		this.expected_end_date = expected_end_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public boolean getSyncStatus() {
		return sync;
	}

	@Override
	public int compareTo(Object another) {
		// TODO Auto-generated method stub
		Tasks comparar = (Tasks) another;
		if (progress > comparar.getProgress())
			return 1;
		else if (progress < comparar.getProgress())
			return -1;
		else
			return 0;

	}

}
