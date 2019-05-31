package com.appmoviles.andres.matchicesi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.appmoviles.andres.matchicesi.model.Message;

import java.util.ArrayList;
import java.util.UUID;

public class DBHandler extends SQLiteOpenHelper {

    private static DBHandler instance = null;


    public static final String DB_NAME = "MatchICESI";
    public static final int DB_VERSION = 4;
    public static final String TABLE_CHATS = "chats";
    public static final String ID = "id";
    public static final String IDCHAT = "idChat";
    public static final String IDMENSAJE = "idMensaje";
    public static final String CONTENIDO = "Contenido";
    public static final String CREATE_CHAT_TABLE = "CREATE TABLE " + TABLE_CHATS + " (" + ID + " TEXT PRIMARY KEY, " + IDCHAT + " TEXT, " + IDMENSAJE + " TEXT, " + CONTENIDO + " TEXT)";

    //TABLA AMIGOS
    public static synchronized DBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler(context);
        }
        return instance;
    }

    private DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);
        onCreate(db);
    }

    public void createMensaje(Message mensaje, String idChat) {
        String uid = UUID.randomUUID().toString();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_CHATS + " (" + ID + ", " + IDCHAT + ", " + IDMENSAJE + ", " + CONTENIDO + ") VALUES ('" + uid + "','" + idChat + "','" + mensaje.getId() + "','" + mensaje.getText() + "')");
        db.close();
    }

    /**
     * Este metodo sirve para devolver todos los mensajes que ha tenido el chat con cierta persona.
     *
     * @param idChat el id del chat con la persona
     * @return
     */
    public ArrayList<Message> getAllMensajesUserWithIdChat(String idChat) {
        ArrayList<Message> respuesta = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHATS + " WHERE " + IDCHAT + "='" + idChat + "'", null);
        Log.e(">>>", "Amigos");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Message mensaje = new Message();
                mensaje.setId(cursor.getString(cursor.getColumnIndex(IDMENSAJE)));
                mensaje.setText(cursor.getString(cursor.getColumnIndex(CONTENIDO)));
                respuesta.add(mensaje);
                Log.e(">>>", mensaje.getId());

            } while (cursor.moveToNext());
        }
        db.close();

        return respuesta;
    }

    public void deleteMessagesChat(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CHATS + " WHERE " + IDCHAT + "='" + uid + "'");
        db.close();
    }

}
