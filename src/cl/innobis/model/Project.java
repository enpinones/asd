package cl.innobis.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

//Modelo que va a guardar todos los proyectos de un usuario
//Por ahora la vamos a dejar creada
//Autor: Cristian Lehuede
public class Project {

	@DatabaseField(id = true)
	int id;
	@DatabaseField
	String name;
	@DatabaseField
	Date start_date;
	@DatabaseField
	Date end_date;
	@DatabaseField
	double progress;
	@DatabaseField
	Boolean deleted;

	public Project(int id, String name, Date start_date, Date end_date,
			double progress, Boolean deleted) {
		super();
		this.id = id;
		this.name = name;
		this.start_date = start_date;
		this.end_date = end_date;
		this.progress = progress;
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", "
				+ (name != null ? "name=" + name + ", " : "")
				+ (start_date != null ? "start_date=" + start_date + ", " : "")
				+ (end_date != null ? "end_date=" + end_date + ", " : "")
				+ "progress=" + progress + ", "
				+ (deleted != null ? "deleted=" + deleted : "") + "]";
	}

	public Project() {

	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Date getStart_date() {
		return start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public double getProgress() {
		return progress;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
