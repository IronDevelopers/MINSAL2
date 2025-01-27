package com.example.minsal_ecosf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	 
	//Ruta por defecto de las bases de datos en el sistema Android
	private static String DB_PATH = "/data/data/com.fichafamiliar/databases/";
	 
	private static String DB_NAME = "fichaFamAndroid.db";
	 
	private SQLiteDatabase myDataBase;
	 
	private final Context myContext;
	 
	/**
	* Constructor
	* Toma referencia hacia el contexto de la aplicaci�n que lo invoca para poder acceder a los 'assets' y 'resources' de la aplicaci�n.
	* Crea un objeto DBOpenHelper que nos permitir� controlar la apertura de la base de datos.
	* @param context
	*/
	public DBHelper(Context context) {
	 
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	 
	}
	 
	/**
	* Crea una base de datos vac�a en el sistema y la reescribe con nuestro fichero de base de datos.
	* */
	public void createDataBase() throws IOException{
	 
		boolean dbExist = checkDataBase();
		 
		if(dbExist){
		//la base de datos existe y no hacemos nada.
		}else{
		//Llamando a este m�todo se crea la base de datos vac�a en la ruta por defecto del sistema
		//de nuestra aplicaci�n por lo que podremos sobreescribirla con nuestra base de datos.
		this.getReadableDatabase();
		 
		try {
		 
		copyDataBase();
		 
			} catch (IOException e) {
			throw new Error("Error copiando Base de Datos");
			}
		}
	 
	}
	 
	/**
	* Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicaci�n.
	* @return true si existe, false si no existe
	*/
	private boolean checkDataBase(){
	 
		SQLiteDatabase checkDB = null;
	 
		try{
		 
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		 
		}catch(SQLiteException e){
		 
		//si llegamos aqui es porque la base de datos no existe todav�a.
		 
		}
		if(checkDB != null){
	 
			checkDB.close();
		 
		}
	return checkDB != null ? true : false;
	}
	 
	/**
	* Copia nuestra base de datos desde la carpeta assets a la reci�n creada
	* base de datos en la carpeta de sistema, desde d�nde podremos acceder a ella.
	* Esto se hace con bytestream.
	* */
	private void copyDataBase() throws IOException{
	 
		//Abrimos el fichero de base de datos como entrada
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		 
		//Ruta a la base de datos vac�a reci�n creada
		String outFileName = DB_PATH + DB_NAME;
		 
		//Abrimos la base de datos vac�a como salida
		OutputStream myOutput = new FileOutputStream(outFileName);
		 
		//Transferimos los bytes desde el fichero de entrada al de salida
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
	 
		//Liberamos los streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	 
	}
	 
	public void open() throws SQLException{
	 
		//Abre la base de datos
		try {
			createDataBase();
		} catch (IOException e) {
			throw new Error("Ha sido imposible crear la Base de Datos");
		}
		 
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		 
	}
	 
	@Override
	public synchronized void close() {
		if(myDataBase != null){
			myDataBase.close();
		}
		super.close();
	}
	 
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}
	 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	 
	}
	
	
}