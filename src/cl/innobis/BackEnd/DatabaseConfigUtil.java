package cl.innobis.backend;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

//Esta clase se ocupa para crear la configuracion de la base de datos.
//Todo esto se encarga ORMLite.
//Autor Cristian Lehuede

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		writeConfigFile("ormlite_config.txt");

	}

}
