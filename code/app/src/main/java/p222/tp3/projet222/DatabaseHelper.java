package p222.tp3.projet222;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.Date;


public class DatabaseHelper extends SQLiteOpenHelper {

        private static final String NOM_DB = "projet.db";

        // Field.s communs
        private static final String ID      = "ID";
        private static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";

        // Table user
        private static final String NOM_TABLE_USER = "table_users";

        private static final String NAME = "NAME";
        private static final String TYPE_NAME = "VARCHAR(30) NOT NULL";

        private static final String ID_USER = "ID_USER";

        // Table images
        private static final String NOM_TABLE_IMAGES = "table_images";

        private static final String FILENAME = "FILENAME";
        private static final String TYPE_FILENAME = "VARCHAR(30) NOT NULL";

        private static final String ENC_FILENAME = "ENC_FILENAME";
        private static final String TYPE_ENC_FILENAME = "VARCHAR(50)";

        private static final String UPLOADED_AT = "UPLOADED_AT";
        private static final String TYPE_UPLOADED_AT = "DATE NOT NULL";

        private static final String PATH = "PATH";
        private static final String TYPE_PATH = "VARCHAR(50)";

        private static final String TYPE_ID_USER = "FOREIGN KEY("+ID_USER+") REFERENCES "+NOM_TABLE_USER+"("+ID_USER+")";


        private static final Couple<String, String>[] tableQueries = new Couple[]{
                new Couple<String, String>(NOM_TABLE_IMAGES,
                        ID + " " +TYPE_ID+", "
                                +               FILENAME + " " +TYPE_FILENAME + ", "
                                +               ENC_FILENAME + " " + TYPE_ENC_FILENAME + ", "
                                +               UPLOADED_AT + " " + TYPE_UPLOADED_AT + ", "
                                +               PATH + " " + TYPE_PATH + ", "
                                +               ID_USER + " " + TYPE_ID_USER)
                ,
                new Couple<String, String>(NOM_TABLE_USER,
                          ID_USER + " " +TYPE_ID+", "
                        + NAME + " " + TYPE_NAME

                )
        };

    /*
    table_images:
    ------------------------------------------------------------------------------------------------------
    ID          FILENAME        ENC_FILENAME        UPLOADED_AT         PATH            ID_USER
    <INTEGER>   <VARCHAR>       <VARCHAR|NULL>      <DATETIME>          <VARCHAR>       <INT FOREIGN KEY>

    table_users:
    ------------------------------------------------------------------------------------------------------
     */


        private static final String UPLOAD_PATH = "/uploads/";




        public DatabaseHelper(Context c) {
            super(c, NOM_DB, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createStatement = "CREATE TABLE %s"
                                   + "( %s )"
                                   + ";";
            String specs = "";
            String tableName = "";
            for (Couple c: tableQueries) {
                tableName = c.getKey().toString();
                specs     = c.getValue().toString();

                String fStatement = String.format(createStatement, tableName, specs);
                System.out.println(fStatement);
                /*db.execSQL(
                        String.format(createStatement, tableName, specs)
                );*/
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String delStatement = "DROP TABLE IF EXISTS %s";
            for (Couple c: tableQueries) {
                String fStatement = String.format(delStatement, c.getKey().toString());
                db.execSQL(fStatement);
            }
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

        public Couple getCoupleByKey(Object key) {
            Couple rVal = new Couple<null,null>(null,null);
            for (Couple c: tableQueries) {
                if (c.getKey().equals(key)) {
                    rVal = c;
                }
            }
            return rVal;
        }

        public boolean coupleExists(Object key) {
            return !(getCoupleByKey(key) == null);
        }

        public boolean addFichier(TypeFichier f) {
            // TODO : Besoin de récupérer l'utilisateur identifié afin de savoir où déplacer le fichier / l'user_id
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv = fillFields(cv, f);
            long errcode = db.insert(NOM_TABLE_IMAGES, null, cv);

            // -1 si mauvaise insertion
            if (errcode == -1) {
                return false;
            }
            return true;
        }

        public boolean delFichier(int user_id, int fichier_id) {
            boolean retVal = false;
            String getStatement = "SELECT PATH FROM "+NOM_TABLE_IMAGES
                                + "WHERE "+ID+" = " +fichier_id
                                + "AND " +ID_USER+" = "+user_id+";";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor data = db.rawQuery(getStatement, new String[0]);
            if (data != null) {
                // Path = data.getString(0);
                File fichier = new File(data.getString(0));
                retVal = fichier.delete(); // renvoie true si le fichier a été supprimé
            }
            return retVal;
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

        public Cursor getData(String tableName) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (!coupleExists(tableName)) return null;

            String query = "SELECT * FROM "+tableName;
            Cursor data = db.rawQuery(query, new String[0]);
            return data;
        }
    }

}
