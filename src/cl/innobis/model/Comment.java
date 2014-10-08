package cl.innobis.model;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

//Modelo que va a guardar todos los comentarios que se hagan
// en las actividades
//Autor: Cristian Lehuede

public class Comment {
	@DatabaseField(generatedId = true)
	int id;
	@DatabaseField
	int task_id;
	@DatabaseField
	String descripcion;
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	byte[] imagen;
	@DatabaseField
	Date fechaCreacion;

	public Comment(int task_id, String descripcion) {
		super();
		this.task_id = task_id;
		this.descripcion = descripcion;
		fechaCreacion = new Date();

	}

	public Comment() {

	}

	@Override
	public String toString() {
		return "Comment [task_id=" + task_id + ", "
				+ (descripcion != null ? "descripcion=" + descripcion : "")
				+ "]";
	}

	public int getTask_id() {
		return task_id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

}
