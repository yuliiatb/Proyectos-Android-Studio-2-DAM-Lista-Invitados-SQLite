package com.example.reto2.dao

import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.reto2.database.DBHelper
import com.example.reto2.model.Invitado

class InvitadoDAO (var context: Context) {

    fun insertInvitado(invitado: Invitado) {
         if (!nombreExiste(invitado.name)) { //comprobar si el nombre existe
            val database: SQLiteDatabase = DBHelper(context, "invitados_db", null, 1).writableDatabase
            //parametrizar
            val content: ContentValues = ContentValues()
            content.put("name", invitado.name)
            database.insert("elementTable", null, content)
         }
         else {
            Log.v("","Este invitado ya existe.")
        }
    }

    fun getInvitados(): String {
        val database: SQLiteDatabase = DBHelper(context, "invitados_db", null, 1).readableDatabase
        var listaInvitados: String = ""
        val resultado: Cursor = database.query("elementTable", arrayOf("name"), null, null, null, null, null)
        while (resultado.moveToNext()){
            val name = resultado.getString(resultado.getColumnIndexOrThrow("name"))
            listaInvitados += "$name\n" //añadir el nombre a la lista y el salto de linea
        }
        return listaInvitados
    }

    fun editarInvitados(nombreAntiguo: String, nombreEditado: String)  {
        if (nombreExiste(nombreAntiguo)) { //comprobar si el nombre existe
            val database: SQLiteDatabase = DBHelper(context, "invitados_db", null, 1).writableDatabase
            val content: ContentValues = ContentValues()
            content.put("name", nombreEditado)
            database.update("elementTable", content, "name=?", arrayOf(nombreAntiguo))
         }
        else {
            Log.v("","Error.")
        }
    }

    fun eliminarInvitado(invitado: Invitado) {
        if (nombreExiste(invitado.name)) { //comprobar si el nombre existe
            val database: SQLiteDatabase = DBHelper(context, "invitados_db", null, 1).writableDatabase
            database.delete("elementTable", "name=?", arrayOf(invitado.name))
        }
        else {
            Log.v("","Error.")
        }
    }


    //función para comprobar si el nombre ya existe para poder realizar otras operaciones
    fun nombreExiste(name: String): Boolean {
        val database: SQLiteDatabase = DBHelper(context, "invitados_db", null, 1).readableDatabase
        val resultado: Cursor = database.query("elementTable", arrayOf("name"), "name = ?", arrayOf(name), null, null, null)

        val existe: Boolean
        if (resultado.count > 0) {
            existe = true
        } else {
            existe = false
        }
        return existe
    }


}