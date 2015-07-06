package com.example.minsal_ecosf;

import org.apache.http.impl.cookie.BasicDomainHandler;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.core.graphics.Bitmap;

import com.fichafamiliar.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import static android.provider.BaseColumns._ID;

public class Handler_sqlite extends SQLiteOpenHelper{
	private Context ctx;
	private MapView mapView;
	private DBHelper BD;
	public Handler_sqlite(Context ctx,MapView mapView)
	{
		super(ctx, "MINSAL", null,1);
		this.ctx = ctx;
		this.mapView = mapView;
		
		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
		String query = "CREATE TABLE FICHA ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+"latitud REAL, longitud REAL);";	
		db.execSQL(query);
		
	}	
	@Override
	public void onUpgrade(SQLiteDatabase db,int v_anterior, int v_nueva)
	{
		
		db.execSQL("DROP TABLE FICHA IF EXISTS");
		onCreate(db);
		
	}
	
	public void insertarFicha(int id, double latitud, double longitud)
	{
		
		ContentValues valores = new ContentValues();
		valores.put("latitud", latitud);
		valores.put("longitud", longitud);
		this.getWritableDatabase().insert("FICHA", null, valores);
		
	}
	
	public String[] leer()
	{
		
		String result[] = new String[3];
		String columnas[]= {_ID,"latitud","longitud"};
		Cursor c = this.getReadableDatabase().query("FICHA", columnas, null, null, null,null, null);
		
		int id,ilat,ilong;
		id = c.getColumnIndex(_ID);
		ilat = c.getColumnIndex("latitud");
		ilong = c.getColumnIndex("longitud");
		
		int contador=0;
//		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
//		{
//			
//			result[contador] = c.getString(id)+ "  " + c.getString(ilat)+ "  " + c.getString(ilong) + "\n";
//			Bitmap pointer = AndroidGraphicFactory.convertToBitmap(ctx.getResources().getDrawable(R.drawable.green_house));
//			String bubbleContent = "Id de ficha: 097896-456-12\nJefe de Familia: Pedro Perez";
//			//MyMarker marker = new MyMarker(ctx, new LatLong(Double.parseDouble(c.getString(ilat)),Double.parseDouble(c.getString(ilong))), pointer, 0, 0, mapView, bubbleContent, false, false, depto,municipio,area + ctn_bar_col + zona + num_vivienda + num_familia);
//			//mapView.getLayerManager().getLayers().add(marker);
//		}
//		
//		result = c.getString(id)+ "  " + c.getString(ilat)+ "  " + c.getString(ilong) + "\n";
		return result;
				
	}
	
	
	public Cursor infoFicha(){
		
		BD = new DBHelper(ctx);
		BD.open();		
		
		//Extrayendo la información de la BD
		Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, " +
				"familia.tipo_riesgofamiliar, situacionvivienda.codigosituacion,  " +
				"(familia.codigo_departamento || familia.codigo_municipio || familia.codigo_area || familia.codigo_canton || familia.codigo_zona || familia.numerovivienda || familia.numerofamilia) AS CodigoFamilia,  " +
				"(integrante.primernombre || \" \" || integrante.segundonombre || \" \" || integrante.primerapellido || \" \" || integrante.segundoapellido) AS JefeFamilia " +
				"FROM	familia, situacionvivienda, integrante " +
				"WHERE familia.codigo_sit_vivienda = situacionvivienda.codigosituacion " +
				"AND familia.longitud_vivienda NOTNULL " +
				"AND familia.latitud_vivienda NOTNULL AND familia.tipo_riesgofamiliar NOTNULL " +
				"AND familia.idfamilia = integrante.id_familia " +
				"AND integrante.numerocorrelativo = \"01\"",null);
		
		return c;
		
	}
	
public Cursor numExpediente(){
		
		BD = new DBHelper(ctx);
		BD.open();		
		
		//Extrayendo la información de la BD
		Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, " +
				"familia.tipo_riesgofamiliar, situacionvivienda.codigosituacion, " +
				"familia.codigo_departamento, familia.codigo_municipio, familia.codigo_area, familia.codigo_canton," +
				"familia.codigo_zona, familia.numerovivienda, familia.numerofamilia, " +
				"(integrante.primernombre || \" \" || integrante.segundonombre || \" \" || integrante.primerapellido || \" \" ||integrante.segundoapellido) AS JefeFamilia " +
				"FROM familia, situacionvivienda, integrante " +
				"WHERE familia.codigo_sit_vivienda = situacionvivienda.codigosituacion " +
				"AND familia.longitud_vivienda NOTNULL " +
				"AND familia.latitud_vivienda NOTNULL " +
				"AND familia.tipo_riesgofamiliar NOTNULL " +
				"AND familia.idfamilia = integrante.id_familia " +
				"AND integrante.numerocorrelativo = \"01\"" +
				"AND familia.codigo_canton NOTNULL",null);
		
		return c;
		
	}

public Cursor menuNivel1(){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT descripciondescriptor, iddescriptor " +
			"FROM descriptor, variable " +
			"WHERE variable.tipo_referente ='c'  AND variable.fechafin IS NULL " +
			"AND descriptor.iddescriptor = variable.id_descriptor " +
			"AND descriptor.descripciondescriptor IS NOT \"Valores Si y No\"	" +
			"ORDER BY descriptor.descripciondescriptor ASC", null);
	return c;
}

public Cursor menuNivel2(String id){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT descripcion, idvalor  " +
			"FROM descriptor, valordescriptor, variable " +
			"WHERE descriptor.iddescriptor = valordescriptor.id_descriptor " +
			"AND variable.tipo_referente ='c' " +
			"AND descriptor.iddescriptor = variable.id_descriptor " +
			"AND descriptor.iddescriptor = " + id +
			" ORDER BY valordescriptor.descripcion ASC", null);
	return c;
}


public Cursor manejoDeAguasGrises (){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, familia_variable.valor " +
			"FROM familia, familia_variable, descriptor, variable " +
			"WHERE familia.idfamilia = familia_variable.id_familia " +
			"AND familia_variable.id_variable = variable.idvariable " +
			"AND variable.id_descriptor = descriptor.iddescriptor " +
			"AND descriptor.iddescriptor =22 AND familia.longitud_vivienda NOTNULL " +
			"AND familia.latitud_vivienda NOTNULL", null);
	
	return c;
			
		
	
}
	
public Cursor aCieloAbiertoAlSolar (){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, familia_variable.valor " +
			"FROM familia, familia_variable, descriptor, variable " +
			"WHERE familia.idfamilia = familia_variable.id_familia " +
			"AND familia_variable.id_variable = variable.idvariable " +
			"AND variable.id_descriptor = descriptor.iddescriptor " +
			"AND descriptor.iddescriptor =22 AND familia.longitud_vivienda NOTNULL " +
			"AND familia.latitud_vivienda NOTNULL AND familia.latitud_vivienda NOTNULL AND familia_variable.valor = 3", null);
	
	return c;
			
		
	
}
	
	
public Cursor aLaCalle (){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, familia_variable.valor " +
			"FROM familia, familia_variable, descriptor, variable " +
			"WHERE familia.idfamilia = familia_variable.id_familia " +
			"AND familia_variable.id_variable = variable.idvariable " +
			"AND variable.id_descriptor = descriptor.iddescriptor " +
			"AND descriptor.iddescriptor =22 AND familia.longitud_vivienda NOTNULL " +
			"AND familia.latitud_vivienda NOTNULL AND familia.latitud_vivienda NOTNULL AND familia_variable.valor = 4", null);
	
	return c;		
	
}
	
	
public Cursor eliminacionAAlcantarillado(){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, familia_variable.valor " +
			"FROM familia, familia_variable, descriptor, variable " +
			"WHERE familia.idfamilia = familia_variable.id_familia " +
			"AND familia_variable.id_variable = variable.idvariable " +
			"AND variable.id_descriptor = descriptor.iddescriptor " +
			"AND descriptor.iddescriptor =22 AND familia.longitud_vivienda NOTNULL " +
			"AND familia.latitud_vivienda NOTNULL AND familia.latitud_vivienda NOTNULL AND familia_variable.valor = 1", null);
	
	return c;		
}	

public Cursor porPozoResumidero(){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, familia_variable.valor " +
			"FROM familia, familia_variable, descriptor, variable " +
			"WHERE familia.idfamilia = familia_variable.id_familia " +
			"AND familia_variable.id_variable = variable.idvariable " +
			"AND variable.id_descriptor = descriptor.iddescriptor " +
			"AND descriptor.iddescriptor =22 AND familia.longitud_vivienda NOTNULL " +
			"AND familia.latitud_vivienda NOTNULL AND familia.latitud_vivienda NOTNULL AND familia_variable.valor = 2", null);
	
	return c;	
}	
	
	
public Cursor quebradasORios(){
	BD = new DBHelper(ctx);
	BD.open();
	
	Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda, familia_variable.valor " +
			"FROM familia, familia_variable, descriptor, variable " +
			"WHERE familia.idfamilia = familia_variable.id_familia " +
			"AND familia_variable.id_variable = variable.idvariable " +
			"AND variable.id_descriptor = descriptor.iddescriptor " +
			"AND descriptor.iddescriptor =22 AND familia.longitud_vivienda NOTNULL " +
			"AND familia.latitud_vivienda NOTNULL AND familia.latitud_vivienda NOTNULL AND familia_variable.valor = 5", null);
	
	return c;		
	
}
	
	public Cursor viviendasDeshabitadas(){
		BD = new DBHelper(ctx);
		BD.open();		
		
		//Extrayendo la información de la BD
		Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.longitud_vivienda, familia.latitud_vivienda " +
				"FROM familia " +
				"WHERE familia.codigo_sit_vivienda = \"02\" " +
				"AND familia.longitud_vivienda NOTNULL AND familia.latitud_vivienda NOTNULL",null);
		return c;
	}
	
	
	public Cursor informacionFamilia(double lon, double lat){

		BD = new DBHelper(ctx);
		BD.open();		
		
		String longitud = Double.toString(lon);
		String latitud = Double.toString(lat);
		
		//Extrayendo la información de la BD
		Cursor c = BD.getReadableDatabase().rawQuery("SELECT familia.codigo_departamento, familia.codigo_municipio, " +
				"familia.codigo_area, familia.codigo_canton," +
				"familia.codigo_zona, familia.numerovivienda, familia.numerofamilia, " +
				"(integrante.primernombre || \" \" || integrante.segundonombre || \" \" || integrante.primerapellido || \" \" ||integrante.segundoapellido) AS JefeFamilia " +
				"FROM familia, situacionvivienda, integrante " +
				"WHERE familia.codigo_sit_vivienda = situacionvivienda.codigosituacion " +
				"AND familia.longitud_vivienda NOTNULL " +
				"AND familia.latitud_vivienda NOTNULL " +
				"AND familia.tipo_riesgofamiliar NOTNULL " +
				"AND familia.idfamilia = integrante.id_familia " +
				"AND integrante.numerocorrelativo = \"01\"" +
				"AND familia.codigo_canton NOTNULL AND familia.longitud_vivienda = \"" +longitud+ "\""+
				" AND familia.latitud_vivienda = \""+latitud+"\"",null);
		
		return c;
	}
	
	
	public void abrir()
	{
		
		this.getWritableDatabase();
		
	}
	public void cerrar()
	{
		
		this.close();
		
	}
	
	
	
	
}
