package p222.tp3.projet222;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String NOM_TABLE = "table_images";

    private static final String ID      = "ID";
    private static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";

    private static final String FILENAME = "FILENAME";
    private static final String TYPE_FILENAME = "VARCHAR(30) NOT NULL";

    private static final String ENC_FILENAME = "ENC_FILENAME";
    private static final String TYPE_ENC_FILENAME = "VARCHAR(50)";

    private static final String UPLOADED_AT = "UPLOADED_AT";
    private static final String TYPE_UPLOADED_AT = "DATETIME('now') NOT NULL";

    private static final String PATH = "PATH";
    private static final String TYPE_PATH = "VARCHAR(50)";

    /*
    table_images:
    -----------------------------------------------------------------------------
    ID          FILENAME        ENC_FILENAME        UPLOADED_AT         PATH
    <INTEGER>   <VARCHAR>       <VARCHAR|NULL>      <DATETIME>          <VARCHAR>
     */


    private static final String UPLOAD_PATH = "/uploads/";




    public DatabaseHelper(Context c) {
        super(c, NOM_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+NOM_TABLE
                +            "("
                +               ID + " " +TYPE_ID+" "
                +               FILENAME + " " +TYPE_FILENAME + " "
                +               ENC_FILENAME + " " + TYPE_ENC_FILENAME + " "
                +               UPLOADED_AT + " " + TYPE_UPLOADED_AT + " "
                +               PATH + " " + TYPE_PATH
                +            ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                "DROP TABLE IF EXISTS "+NOM_TABLE
        );
        onCreate(db);
    }

    /*
    Retourne le nom de l'utilisateur pour avoir un path
    tel que: ~/nom_utilisateur
    où est relatif à l'endroit où les données sont sensées être stockées
     */
    public String getUserBasePath() {
        return "";
    }

    public boolean addFichier(TypeFichier f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv = fillFields(cv, f);
        long errcode = db.insert(NOM_TABLE, null, cv);

        // -1 si mauvaise insertion
        if (errcode == -1) {
            return false;
        }
        return true;
    }

    private ContentValues fillFields(ContentValues cv, TypeFichier f) {
        String filename = f.getNom();
        if (f.isEncrypted()) {
            filename = f.getEncName();
            cv.put(ENC_FILENAME, filename);

        }
        // TODO: avoir un moyen de manage/connaître le dossier où upload, avec un potentiel manageur de fichiers
        //this.getUserBasePath() + this.UPLOAD_PATH + filename;
        filename = "/path/to/upload/dir/" + filename;


        cv.put(FILENAME, f.getNom());
        cv.put(PATH, filename);
        cv.put(UPLOADED_AT, (new Date()).toString());
        return cv;
    }

    private Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+NOM_TABLE;
        Cursor data = db.rawQuery(query);
        return data;
    }
}
