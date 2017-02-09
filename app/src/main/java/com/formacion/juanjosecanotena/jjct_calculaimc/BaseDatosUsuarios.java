package com.formacion.juanjosecanotena.jjct_calculaimc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juanjosecanotena on 31/1/17.
 */

public class BaseDatosUsuarios extends SQLiteOpenHelper {

    private final String sqlCreacionTablaUsuarios =
            "CREATE TABLE USUARIOS (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "usuario TEXT, " +
                    "password TEXT, " +
                    "nombre TEXT, " +
                    "apellidos TEXT, " +
                    "ultimoPeso REAL, " +
                    "altura REAL)";

    public BaseDatosUsuarios(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreacionTablaUsuarios);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void cerrarBaseDatos(SQLiteDatabase database){

        database.close();
    }

    public int insertarUsuario(Usuario usuario){

        int idUsuario=0;
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("INSERT INTO USUARIOS (usuario, password, nombre, apellidos, ultimoPeso, altura)" +
                " VALUES (" +
                "'"+usuario.getUsuario()+"',"+
                "'"+usuario.getPassword()+"',"+
                "'"+usuario.getNombre()+"',"+
                "'"+usuario.getApellidos()+"',"+
                Float.toString(usuario.getUltimoPeso())+","+
                Float.toString(usuario.getAltura())+")");

        idUsuario=seleccionarUsuario(usuario.getUsuario()).getId();

        this.cerrarBaseDatos(database);

        return idUsuario;
    }

    public void actualizarUsuario(Usuario usuario){

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE USUARIOS SET " +
                "usuario='"+usuario.getUsuario()+"',"+
                "password='"+usuario.getPassword()+"',"+
                "nombre='"+usuario.getNombre()+"',"+
                "apellidos='"+usuario.getApellidos()+"',"+
                "ultimoPeso="+Float.toString(usuario.getUltimoPeso())+","+
                "altura="+Float.toString(usuario.getAltura())+
                " WHERE id="+Integer.toString(usuario.getId()));
        this.cerrarBaseDatos(database);

    }

    public Usuario seleccionarUsuario(int idUsuario){

        Usuario usuario=new Usuario();
        String consulta = "SELECT id,usuario,password,nombre,apellidos,ultimoPeso,altura FROM USUARIOS WHERE id="+Integer.toString(idUsuario);
        SQLiteDatabase basedatos = this.getReadableDatabase();
        Cursor cursor = basedatos.rawQuery(consulta,null);

        if (cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            usuario.setId(cursor.getInt(0));
            usuario.setUsuario(cursor.getString(1));
            usuario.setPassword(cursor.getString(2));
            usuario.setNombre(cursor.getString(3));
            usuario.setApellidos(cursor.getString(4));
            usuario.setUltimoPeso(cursor.getFloat(5));
            usuario.setAltura(cursor.getFloat(6));
            this.cerrarBaseDatos(basedatos);
            return usuario;
        }
        else {
            this.cerrarBaseDatos(basedatos);
            return null;
        }
    }

    public Usuario seleccionarUsuario(String usuarioLogin){

        Usuario usuario=new Usuario();
        String consulta = "SELECT id,usuario,password,nombre,apellidos,ultimoPeso,altura FROM USUARIOS WHERE usuario='"+usuarioLogin+"'";
        SQLiteDatabase basedatos = this.getReadableDatabase();
        Cursor cursor = basedatos.rawQuery(consulta,null);

        if (cursor != null && cursor.getCount() >0){

            cursor.moveToFirst();
            usuario.setId(cursor.getInt(0));
            usuario.setUsuario(cursor.getString(1));
            usuario.setPassword(cursor.getString(2));
            usuario.setNombre(cursor.getString(3));
            usuario.setApellidos(cursor.getString(4));
            usuario.setUltimoPeso(cursor.getFloat(5));
            usuario.setAltura(cursor.getFloat(6));
            this.cerrarBaseDatos(basedatos);
            return usuario;
        }
        else{
            this.cerrarBaseDatos(basedatos);
            return null;
        }
    }

    public void bajaUsuario(int idUsuario){

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM USUARIOS WHERE id="+Integer.toString(idUsuario));
        this.cerrarBaseDatos(database);
    }

}
