package be.hers.info.inscriptionexamens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import be.hers.info.inscriptionexamens.database.ExamDB;
import be.hers.info.inscriptionexamens.model.Examen;
import be.hers.info.inscriptionexamens.model.Utilisateur;

public class MainActivity extends AppCompatActivity
{
    private boolean isUser(String matricule, String password)
    {
        // TODO : DB check
        return true;
    }

    private boolean isTeacher(String matricule)
    {
        // TODO : DB check
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //On crée la DB
        final ExamDB db = new ExamDB(this);
        Utilisateur cedric = new Utilisateur("H111111","111111","Cédric","Peeters",true);
        db.addUtilisateur(cedric);
        Utilisateur joram = new Utilisateur("H222222","222222","Joram","Mushymiyimana",true);
        db.addUtilisateur(joram);
        Utilisateur bob = new Utilisateur("E111111","111111","Bob","Lennon",false);
        db.addUtilisateur(bob);
        Utilisateur luke = new Utilisateur("E222222","222222","Luke","Skywalker",false);
        db.addUtilisateur(luke);
        //System.out.println("Matricule : "+cedric.getMatricule() + "/ Nom : " + cedric.getNom());

        //J'ajoute deux examens just pr tester
        Date date = new Date();
        date.setYear(2019);
        date.setMonth(12);
        date.setDate(27);
        date.setHours(12);
        date.setMinutes(30);
        Examen exam1 = new Examen(1,1,"écrit papier","blabla", date ,60);
        Date date2 = new Date();
        date.setYear(2019);
        date.setMonth(12);
        date.setDate(28);
        date.setHours(14);
        date.setMinutes(25);
        Examen exam2 = new Examen(2,1,"oral","blabla2", date2 ,20);

        //PROBLEME ID.................
        System.out.println("IDEXAMS : "+exam1.getId()+" "+exam2.getId());

        luke.addToList(Integer.toString(exam1.getId()));
        luke.addToList(Integer.toString(exam2.getId()));
        //-----
        //Je try récup
        System.out.println("LISTE EXAM : " +luke.getListeExamens());
        System.out.println(db.getExamen(2));

        // Boutton connexion
        Button button = findViewById(R.id.bConnect);

        // Identifiants
        final EditText matricule = findViewById(R.id.iMatricule);
        final EditText password = findViewById(R.id.iPassword);

        button.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if(db.comparerMDP(matricule.getText().toString(), password.getText().toString()))
                    {
                        //System.out.println("Comparaison MDP : "+db.comparerMDP(matricule.getText().toString(), password.getText().toString()));

                        if(db.verifierEstProf(matricule.getText().toString()))
                        {
                            Intent intent = new Intent(getApplicationContext(), MainPage_prof.class);
                            // intent.putExtra("name", val);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(), MainPage_etud.class);
                            // intent.putExtra("name", val);
                            startActivity(intent);
                        }
                    }
                }
            }
        );
    }
}
