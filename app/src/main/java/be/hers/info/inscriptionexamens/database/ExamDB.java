package be.hers.info.inscriptionexamens.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.os.Build;
import android.renderscript.ScriptIntrinsicYuvToRGB;


import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import be.hers.info.inscriptionexamens.model.Cours;
import be.hers.info.inscriptionexamens.model.Examen;
import be.hers.info.inscriptionexamens.model.TypeExamen;
import be.hers.info.inscriptionexamens.model.Utilisateur;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ExamDB extends SQLiteOpenHelper {

    //Info DB-------------------------------------------------------------------------------------
    private final static String dbName = "ExamDB";
    private final static int dbVersion = 1;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    //Constructeur--------------------------------------------------------------------------------
    public ExamDB(Context context) {
        super(context,dbName, null, dbVersion);
    }

    //Table Utilisateur----------------------------------------------------------------------------
    private static final String TABLE_UTILISATEUR = "utilisateur";
    private static final String UTILISATEUR_ID = "_id";  
    //"_id" obligatoire pour les PK sinon certaines fonctions d'android risquent de ne pas aller
    private static final String UTILISATEUR_MATRICULE = "matricule";
    private static final String UTILISATEUR_MDP = "mdp";
    private static final String UTILISATEUR_PRENOM = "prenom";
    private static final String UTILISATEUR_NOM = "nom";
    private static final String UTILISATEUR_ESTPROF = "estProf";
    private static final String UTILISATEUR_LISTEEXAMENS = "listeExamens";

    private static final String CREATE_TABLE_UTILISATEUR =  "create table " + TABLE_UTILISATEUR + " ("
            +UTILISATEUR_ID + " integer primary key autoincrement, "
            +UTILISATEUR_MATRICULE + " text not null, "
            +UTILISATEUR_MDP + " text not null, "
            +UTILISATEUR_PRENOM + " text not null, "
            +UTILISATEUR_NOM + " text not null, "
            +UTILISATEUR_ESTPROF +" boolean not null, "
            +UTILISATEUR_LISTEEXAMENS +" text not null);";

    //Table Examen----------------------------------------------------------------------------
    private static final String TABLE_EXAMEN = "examen";
    private static final String EXAMEN_ID = "_id";
    //"_id" obligatoire pour les PK sinon certaines fonctions d'android risquent de ne pas aller
    private static final String EXAMEN_COURS = "cours";
    private static final String EXAMEN_TYPE = "type";
    private static final String EXAMEN_DESCRIPTION = "description";
    private static final String EXAMEN_DATE = "date";
    private static final String EXAMEN_DUREE = "duree";

    private static final String CREATE_TABLE_EXAMEN =  "create table " + TABLE_EXAMEN + " ("
            +EXAMEN_ID + " integer primary key autoincrement, "
            +EXAMEN_COURS + " int not null, "
            +EXAMEN_TYPE + " text not null, "
            +EXAMEN_DESCRIPTION + " text not null, "
            +EXAMEN_DATE + " date not null, "
            +EXAMEN_DUREE +" int not null);";

    // Table Jointure----------------------------------------------------------------------------
    private static final String TABLE_UTIL_EXAM = "utilisateur_examen";
    private static final String UTIL_EXAM_REFEXAMEN = "refexamen";
    private static final String UTIL_EXAM_REFUTILISATEUR = "refutilisateur";

    private static final String CREATE_TABLE_UTILISATEUR_EXAM = "create table " + TABLE_UTIL_EXAM + " ("
            + "_id integer primary key autoincrement, "
            + UTIL_EXAM_REFUTILISATEUR + " integer not null, "
            + UTIL_EXAM_REFEXAMEN + " integer not null, "
            + "CONSTRAINT unqInscr UNIQUE ("+UTIL_EXAM_REFUTILISATEUR+", "+UTIL_EXAM_REFEXAMEN+")"
            + " );";

    // Table AnneeEtud----------------------------------------------------------------------------
    private static final String TABLE_ANNEE_ETUD = "annee_etud";
    private static final String ANNEE_ETUD_REFETUDIANT = "refEtudiant";
    private static final String ANNEE_ETUD_ANNEE = "année";

    private static final String CREATE_TABLE_ANNEE_ETUD = "create table " + TABLE_ANNEE_ETUD + " ("
            + "_id integer primary key autoincrement, "
            + ANNEE_ETUD_REFETUDIANT + " integer not null, "
            + ANNEE_ETUD_ANNEE + " integer not null, "
            + "CONSTRAINT unqInscr UNIQUE ("+ANNEE_ETUD_REFETUDIANT+", "+ANNEE_ETUD_ANNEE+")"
            + " );";

    // Table Notifications----------------------------------------------------------------------------
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String NOTIF_REFEXAMEN = "refEXAMEN";

    private static final String CREATE_TABLE_NOTIFICATION = "create table " + TABLE_NOTIFICATION + " ("
            + "_id integer primary key autoincrement, "
            + NOTIF_REFEXAMEN + " integer not null, "
            + "CONSTRAINT unqInscr UNIQUE ("+NOTIF_REFEXAMEN+")"
            + " );";

    //Table Cours----------------------------------------------------------------------------
    private static final String TABLE_COURS = "cours";
    private static final String COURS_ID = "_id";
    //"_id" obligatoire pour les PK sinon certaines fonctions d'android risquent de ne pas aller
    private static final String COURS_NOM = "nom";
    private static final String COURS_QUADRIMESTTRE = "quadrimestre";
    private static final String COURS_ANNEE = "annee";

    private static final String CREATE_TABLE_COURS =  "create table " + TABLE_COURS + " ("
            +COURS_ID + " integer primary key autoincrement, "
            +COURS_NOM + " String not null, "
            +COURS_ANNEE + " int not null, "
            +COURS_QUADRIMESTTRE +" int not null, "
            +"CONSTRAINT unqCours UNIQUE ("+COURS_NOM+", "+COURS_ANNEE+", "+COURS_QUADRIMESTTRE+") );";
    
    
    //Création de la DB---------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(CREATE_TABLE_UTILISATEUR);
            db.execSQL(CREATE_TABLE_EXAMEN);
            db.execSQL(CREATE_TABLE_COURS);
            db.execSQL(CREATE_TABLE_UTILISATEUR_EXAM);
            db.execSQL(CREATE_TABLE_ANNEE_ETUD);
            db.execSQL(CREATE_TABLE_NOTIFICATION);
        }
        catch(SQLException e) { e.printStackTrace(); }

        System.out.println("Created");
    }

    //Upgrade de la DB----------------------------------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_UTILISATEUR+";");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EXAMEN+";");
        onCreate(db);
    }

    /**
     * Check if the database exist and can be read.
     * @return true if it exists and can be read, false if it doesn't
     */
    public boolean checkDataBase()
    {
        SQLiteDatabase checkDB = getWritableDatabase();
        int n = -1;
        try
        {
            String count = "SELECT count(*) FROM " + TABLE_UTILISATEUR;
            Cursor cursor = checkDB.rawQuery(count, null);
            cursor.moveToFirst();

            n = cursor.getInt(0);
        }
        catch (Exception e) { e.printStackTrace(); }
        finally
        {
            checkDB.close();
        }
        return (n > 0);
    }

    //********************************************************************************************
    //**************************** METHODES UTILISATEUR ******************************************
    //********************************************************************************************
    //Ajouter un utilisateur-----------------------------------------------------------------------
    public void addUtilisateur(Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            StringBuilder str = new StringBuilder();
            ArrayList<String> listeExam = utilisateur.getListeExamens();

            for(int i=0; i<listeExam.size(); i++){
                str.append(listeExam.get(i) + "|");
            }

            String resListe = str.toString();

            values.put(UTILISATEUR_MATRICULE, utilisateur.getMatricule());
            values.put(UTILISATEUR_MDP, utilisateur.getMdp());
            values.put(UTILISATEUR_PRENOM, utilisateur.getPrenom());
            values.put(UTILISATEUR_NOM, utilisateur.getNom());
            values.put(UTILISATEUR_ESTPROF, utilisateur.getEstProf());
            values.put(UTILISATEUR_LISTEEXAMENS, resListe);

            db.insert(TABLE_UTILISATEUR, null, values);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.close();
        }

    }

    //Récupérer un utilisateur--------------------------------------------------------------------
    public Utilisateur getUtilisateur(String matricule) {
        SQLiteDatabase db = this.getReadableDatabase();

        try{
            Cursor cursor = db.query(
                    TABLE_UTILISATEUR,
                    new String[] {
                            UTILISATEUR_ID,
                            UTILISATEUR_MATRICULE,
                            UTILISATEUR_MDP,
                            UTILISATEUR_PRENOM,
                            UTILISATEUR_NOM,
                            UTILISATEUR_ESTPROF,
                            UTILISATEUR_LISTEEXAMENS
                    },
                    UTILISATEUR_MATRICULE + "=?",
                    new String[] { String.valueOf(matricule) },
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
            }

            Utilisateur utilisateur = new Utilisateur(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5) != 0
            );

            String str = cursor.getString(6);
            String[] separation = str.split("|");
            ArrayList<String> listeExam = new ArrayList<>();

            for(int i=0; i < separation.length; i++){
                listeExam.add(separation[i]);
            }

            utilisateur.setId(cursor.getInt(0));
            System.out.println("ID USER ICI : "+utilisateur.getId());

            return utilisateur;

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    //Récupérer un utilisateur--------------------------------------------------------------------
    public Utilisateur getUtilisateurByID(int refUtilisateur) {
        SQLiteDatabase db = this.getReadableDatabase();

        try{
            Cursor cursor = db.query(
                    TABLE_UTILISATEUR,
                    new String[] {
                            UTILISATEUR_ID,
                            UTILISATEUR_MATRICULE,
                            UTILISATEUR_MDP,
                            UTILISATEUR_PRENOM,
                            UTILISATEUR_NOM,
                            UTILISATEUR_ESTPROF,
                            UTILISATEUR_LISTEEXAMENS
                    },
                    UTILISATEUR_ID + "=?",
                    new String[] { String.valueOf(refUtilisateur) },
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
            }

            Utilisateur utilisateur = new Utilisateur(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5) != 0
            );

            String str = cursor.getString(6);
            String[] separation = str.split("|");
            ArrayList<String> listeExam = new ArrayList<>();

            for(int i=0; i < separation.length; i++){
                listeExam.add(separation[i]);
            }

            System.out.println("ID USER ICI : "+utilisateur.getId());
            utilisateur.setId(cursor.getInt(0));
            return utilisateur;

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    //Update utilisateur
    public int updateUtilisateur(Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            StringBuilder str = new StringBuilder();
            ArrayList<String> listeExam = utilisateur.getListeExamens();

            for(int i=0; i<listeExam.size(); i++){
                str.append(listeExam.get(i) + "|");
            }

            String resListe = str.toString();

            values.put(UTILISATEUR_MATRICULE, utilisateur.getMatricule());
            values.put(UTILISATEUR_MDP, utilisateur.getMdp());
            values.put(UTILISATEUR_PRENOM, utilisateur.getPrenom());
            values.put(UTILISATEUR_NOM, utilisateur.getNom());
            values.put(UTILISATEUR_ESTPROF, utilisateur.getEstProf());
            values.put(UTILISATEUR_LISTEEXAMENS, resListe);

            return db.update(
                    TABLE_UTILISATEUR, values, UTILISATEUR_ID + " = ?",
                    new String[] {
                            String.valueOf(utilisateur.getId())
                    }
            );
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            db.close();
        }
        return 0;
    }

    //Comparer MDP -------------------------------------------------------------------------------
    public Boolean comparerMDP(String matricule, String mdp){

        //System.out.println("USER : " + matricule + "/" + mdp);
        Utilisateur x = getUtilisateur(matricule);

        //System.out.println("X : " + x.getMatricule() + "/" + x.getMdp());
        if (mdp.equals(x.getMdp())) {
            return true;
        }
        return false;
    }

    //Check si prof ou eleve ---------------------------------------------------------------------
    public Boolean verifierEstProf(String matricule){
        Utilisateur x = getUtilisateur(matricule);
        if(x.getEstProf()){
            return true;
        }
        return false;
    }

    public Boolean inscrireUtilisateurAExamen(String matricule, int id_exam)
    {
        Utilisateur x = getUtilisateur(matricule);

        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues values = new ContentValues();
            values.put(UTIL_EXAM_REFUTILISATEUR, x.getId());
            values.put(UTIL_EXAM_REFEXAMEN, id_exam);

            // TODO la contrainte ne lance ni erreur ni exception, alors que ça fait bient un print stack
            db.insert(TABLE_UTIL_EXAM, null, values);

            // Si aucune contraintes
            return true;
        }
        catch(Exception e){ e.printStackTrace(); }
        catch(Error error){ error.printStackTrace(); }
        finally{ db.close(); }

        return false;
    }

    public boolean desinscrireUtilisateurAExamen(String matricule, int id_exam) {

        Utilisateur x = getUtilisateur(matricule);

        SQLiteDatabase db = this.getWritableDatabase();

        try{

            Cursor cursor = db.query(
                    TABLE_UTIL_EXAM,
                    new String[]{
                            UTIL_EXAM_REFUTILISATEUR,
                            UTIL_EXAM_REFEXAMEN
                    },
                    UTIL_EXAM_REFUTILISATEUR + "=? AND " + UTIL_EXAM_REFEXAMEN + "=?",
                    new String[]{String.valueOf(x.getId()), String.valueOf(id_exam)},
                    null,
                    null,
                    null,
                    null
            );

            // Si le curseur existe
            if (cursor != null)
            {
                db.delete(
                        TABLE_UTIL_EXAM, UTIL_EXAM_REFUTILISATEUR + "=? AND " + UTIL_EXAM_REFEXAMEN + "=?",
                        new String[] {
                                String.valueOf(x.getId()), String.valueOf(id_exam),
                        }
                );
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return false;
    }


    //********************************************************************************************
    //**************************** METHODES EXAMEN ******************************************
    //********************************************************************************************
    //Ajouter un examen-----------------------------------------------------------------------
    public int addExamen(Examen exam)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int n = -1;

        try{

            ContentValues values = new ContentValues();
            values.put(EXAMEN_COURS, exam.refCours);
            values.put(EXAMEN_DATE, (exam.date).format(formatter));
            // values.put(EXAMEN_DATE, formatter.format(exam.date));
            values.put(EXAMEN_TYPE, exam.typeExam.toString());
            values.put(EXAMEN_DESCRIPTION, exam.description);
            values.put(EXAMEN_DUREE, exam.dureeMinute);

            db.insert(TABLE_EXAMEN, null, values);


            // Last id
            String lastExam = "SELECT MAX(_id) FROM " + TABLE_EXAMEN;
            Cursor cursor = db.rawQuery(lastExam, null);
            cursor.moveToFirst();

            n = cursor.getInt(0);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.close();
        }

        // id
        return n;
    }

    //Récupérer un examen--------------------------------------------------------------------
    public Examen getExamenByID(int refExamen) {
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(
                    TABLE_EXAMEN,
                    new String[]{
                            EXAMEN_ID,
                            EXAMEN_COURS,
                            EXAMEN_TYPE,
                            EXAMEN_DESCRIPTION,
                            EXAMEN_DATE,
                            EXAMEN_DUREE,
                    },
                    EXAMEN_ID + "=?",
                    new String[]{String.valueOf(refExamen)},
                    null,
                    null,
                    null,
                    null
            );

            if (cursor != null)
                cursor.moveToFirst();

            Examen exam = new Examen(

                    cursor.getInt(1),
                    TypeExamen.valueOf(cursor.getString(2)), // String vers -> enum
                    cursor.getString(3),
                    cursor.getInt(5)
            );

            String str_d = cursor.getString(4);
            LocalDateTime date = LocalDateTime.parse(str_d, formatter);
            //LocalDateTime date = LocalDateTime.parse(str_d);
            exam.date = date;

            System.out.println("COMOESTA : "+cursor.getInt(0));

            exam.setId(cursor.getInt(0));
            return exam;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.close();
        }
        return null;
    }

    /**
     * récupère les examens aux quels un utilisateur est inscrit
     * @param refUtilisateur id de l'utilisateur
     * @return liste d'examen lié à l'utilisateur
     */
    public List<Examen> getExamenByInscription(int refUtilisateur)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        LinkedList<Examen> listeExamens = new LinkedList<Examen>();

        try
        {
            // Sub querry
            String rawQuery = "SELECT " + EXAMEN_ID + ", " + EXAMEN_COURS + ", " + EXAMEN_TYPE + ", "
                    + EXAMEN_DESCRIPTION + ", " + EXAMEN_DATE + ", " +EXAMEN_DUREE
                    + " FROM " + TABLE_EXAMEN
                    + " WHERE " + EXAMEN_ID + " IN ("
                        + " SELECT "+ UTIL_EXAM_REFEXAMEN
                        + " FROM "+ TABLE_UTIL_EXAM
                        + " WHERE " + UTIL_EXAM_REFUTILISATEUR + " =?"
                    +" ) ";

            Cursor cursor = db.rawQuery(rawQuery, new String[]{String.valueOf(refUtilisateur)});

            // Si le curseur existe
            if (cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    // Add all examens
                    do {
                        Examen exam = new Examen
                                (
                                        cursor.getInt(1),
                                        TypeExamen.valueOf(cursor.getString(2)),
                                        cursor.getString(3),
                                        cursor.getInt(5)
                                );

                        String str_d = cursor.getString(4);
                        LocalDateTime date = LocalDateTime.parse(str_d, formatter);
                        // LocalDateTime date = LocalDateTime.parse(str_d);
                        exam.date = date;

                        exam.setId(cursor.getInt(0));
                        listeExamens.add(exam);
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){e.printStackTrace();}
        finally
        {
            db.close();
        }

        return listeExamens;
    }

    /**
     * Liste les examens aux quels un utilisateur n'est PAS inscrit
     * @param refUtilisateur id de l'utilisateur
     * @return examens aux quels il est possible de s'inscrire
     */
    public List<Examen> getExamenNonInscrit(int refUtilisateur, Set<String> data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        LinkedList<Examen> listeExamens = new LinkedList<Examen>();
        String inStatement = String.join(", ", data);

        try
        {
            // Inner join
            String rawQuery = "SELECT " + EXAMEN_ID + ", " + EXAMEN_COURS + ", " + EXAMEN_TYPE + ", "
                    + EXAMEN_DESCRIPTION + ", " + EXAMEN_DATE + ", " +EXAMEN_DUREE
                    + " FROM " + TABLE_EXAMEN
                    + " WHERE " + EXAMEN_ID + " NOT IN ("
                        + " SELECT "+ UTIL_EXAM_REFEXAMEN
                        + " FROM "+ TABLE_UTIL_EXAM
                        + " WHERE " + UTIL_EXAM_REFUTILISATEUR + " =?"
                    +" ) AND "
                    + EXAMEN_COURS + " IN ( "
                        + " SELECT "+ COURS_ID
                        + " FROM " + TABLE_COURS
                        + " WHERE " + COURS_ANNEE + " IN ( "+ inStatement  +" )"
                    + " )";

            Cursor cursor = db.rawQuery(rawQuery, new String[]{String.valueOf(refUtilisateur)});

            // Si le curseur existe
            if (cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    // Add all examens
                    do {
                        Examen exam = new Examen
                                (
                                        cursor.getInt(1),
                                        TypeExamen.valueOf(cursor.getString(2)),
                                        cursor.getString(3),
                                        cursor.getInt(5)
                                );

                        String str_d = cursor.getString(4);
                        LocalDateTime date = LocalDateTime.parse(str_d, formatter);
                        // LocalDateTime date = LocalDateTime.parse(str_d);
                        exam.date = date;

                        exam.setId(cursor.getInt(0));
                        listeExamens.add(exam);
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){e.printStackTrace();}
        finally
        {
            db.close();
        }

        return listeExamens;
    }

    //Update Examen
    public int updateExamen(int id_exam, Examen exam) {
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();

            values.put(EXAMEN_COURS, exam.refCours);
            values.put(EXAMEN_TYPE, exam.typeExam.toString());
            values.put(EXAMEN_DESCRIPTION, exam.description);
            values.put(EXAMEN_DATE, (exam.date).format(formatter));
            values.put(EXAMEN_DUREE, exam.dureeMinute);

            int n = db.update(
                    TABLE_EXAMEN, values, EXAMEN_ID + " = ?",
                    new String[] {
                            String.valueOf(id_exam)
                    }
            );
            return n;

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            db.close();
        }
        return -1;
    }

    /**
     * récupère une liste d'élèves inscrits à un examen
     * @return listeEleves
     */
    public ArrayList<Integer> listerInscrits(int refExam) {
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            //Utilisation de deux listes pour davantage de clarté
            ArrayList<Integer> listeUtilisateurs = new ArrayList<>();
            ArrayList<Integer> listeEtudiants = new ArrayList<>();

            Cursor cursor = db.query(
                    TABLE_UTIL_EXAM,
                    new String[]{
                            UTIL_EXAM_REFUTILISATEUR
                    },
                    UTIL_EXAM_REFEXAMEN + "=?",
                    new String[]{String.valueOf(refExam)},
                    null,
                    null,
                    null,
                    null
            );

            // Si le curseur existe
            if (cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    // Add les références des utilisateurs inscrits à cet examen
                    do {
                        listeUtilisateurs.add(cursor.getInt(0));
                    }
                    while (cursor.moveToNext());
                }

                for(int i = 0; i < listeUtilisateurs.size(); i++){
                    Utilisateur utilisateur = getUtilisateurByID(listeUtilisateurs.get(i));
                    if(!utilisateur.getEstProf()){
                        listeEtudiants.add(utilisateur.getId());
                    }
                }
            }
            return listeEtudiants;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    //Ajouter un utilisateur-----------------------------------------------------------------------
    public void addExamModifie(int refExam) {
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();

            values.put(NOTIF_REFEXAMEN, refExam);

            db.insert(TABLE_NOTIFICATION, null, values);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.close();
        }
    }

    /**
     *
     * @param refUtilisateur
     * @return
     */
    public List<Examen> getExamModifies(int refUtilisateur)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        LinkedList<Examen> listeExamens = new LinkedList<>();

        try
        {
            // Inner join
            String rawQuery = "SELECT " + EXAMEN_ID + ", " + EXAMEN_COURS + ", " + EXAMEN_TYPE + ", "
                    + EXAMEN_DESCRIPTION + ", " + EXAMEN_DATE + ", " +EXAMEN_DUREE
                    + " FROM " + TABLE_EXAMEN
                    + " WHERE " + EXAMEN_ID + " IN ( "
                        + " SELECT "+ NOTIF_REFEXAMEN
                        + " FROM " + TABLE_NOTIFICATION
                    + " ) AND " + EXAMEN_ID + " IN ("
                        + " SELECT "+ UTIL_EXAM_REFEXAMEN
                        + " FROM "+ TABLE_UTIL_EXAM
                        + " WHERE " + UTIL_EXAM_REFUTILISATEUR + " =?"
                    +" ) ";

            Cursor cursor = db.rawQuery(rawQuery, new String[]{String.valueOf(refUtilisateur)});

            // Si le curseur existe
            if (cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    // Add all examens
                    do {
                        Examen exam = new Examen
                                (
                                        cursor.getInt(1),
                                        TypeExamen.valueOf(cursor.getString(2)),
                                        cursor.getString(3),
                                        cursor.getInt(5)
                                );

                        String str_d = cursor.getString(4);
                        LocalDateTime date = LocalDateTime.parse(str_d, formatter);
                        // LocalDateTime date = LocalDateTime.parse(str_d);
                        exam.date = date;

                        exam.setId(cursor.getInt(0));
                        listeExamens.add(exam);
                    } while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){e.printStackTrace();}
        finally
        {
            db.close();
        }

        return listeExamens;
    }


    //********************************************************************************************
    //**************************** METHODES COURS ******************************************
    //********************************************************************************************
    //Ajouter un cours-----------------------------------------------------------------------
    public void addCours(Cours cours) {
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();

            values.put(COURS_NOM, cours.getNom());
            values.put(COURS_ANNEE, cours.getAnnee());
            values.put(COURS_QUADRIMESTTRE, cours.getQuadrimestre());

            db.insert(TABLE_COURS, null, values);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.close();
        }

    }

    //Récupérer un cours--------------------------------------------------------------------
    public Cours getCours(int refCours) {
        SQLiteDatabase db = this.getReadableDatabase();

        try{
            Cursor cursor = db.query(
                    TABLE_COURS,
                    new String[] {
                            COURS_ID,
                            COURS_NOM,
                            COURS_ANNEE,
                            COURS_QUADRIMESTTRE
                    },
                    COURS_ID + "=?",
                    new String[] { String.valueOf(refCours) },
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
            }

            Cours cours = new Cours(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3)
            );

            return cours;

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    public ArrayList<Cours> getAllCours() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Cours> listeCours = new ArrayList<>();

        try{
            Cursor cursor = db.query(
                    TABLE_COURS,
                    new String[] {
                            COURS_ID,
                            COURS_NOM,
                            COURS_ANNEE,
                            COURS_QUADRIMESTTRE
                    },
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null) {
                cursor.moveToFirst();
            }
            if (cursor.moveToFirst()) {
                do {
                    Cours cours = new Cours(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getInt(3)
                    );
                    listeCours.add(cours);
                }
                while (cursor.moveToNext());
            }




            System.out.println("Liste des Cours : "+listeCours);
            return listeCours;

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }
}
