package be.hers.info.inscriptionexamens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import be.hers.info.inscriptionexamens.custom.AdapterListView_Examen;
import be.hers.info.inscriptionexamens.database.ExamDB;
import be.hers.info.inscriptionexamens.model.Examen;
import be.hers.info.inscriptionexamens.model.Utilisateur;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Etud_MainPage extends AppCompatActivity
{
    private AdapterListView_Examen customList;
    private ExamDB db = new ExamDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etud_mainpage);

        // Toast
        Toast.makeText(Etud_MainPage.this, ("Page Etud !"), Toast.LENGTH_SHORT).show();

        // Page d'inscription
        Button changePage_inscription = findViewById(R.id.bChange_pInscription);
        changePage_inscription.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getApplicationContext(), Etud_InscriptionExamen.class);
                        // intent.putExtra("name", val);
                        startActivity(intent);
                    }
                }
        );

        // Page de désinscription
        Button changePage_desinscription = findViewById(R.id.bChange_pDesinscription);
        changePage_desinscription.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getApplicationContext(), Etud_DesinscriptionExamen.class);
                        // intent.putExtra("name", val);
                        startActivity(intent);
                    }
                }
        );

        // Déconnexion
        Button changePage_deconnexionEtud = findViewById(R.id.bChange_pDeconnexionEtud);
        changePage_deconnexionEtud.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("MATRICULE");
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
        );

        // Get user
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        final String matricule = preferences.getString("MATRICULE", "VIDE");
        Utilisateur user = db.getUtilisateur(matricule);

        // Init notifications
        customList = new AdapterListView_Examen(this, new ArrayList<Examen>());
        ListView listView = findViewById(R.id.customListExamsModifies);
        customList.addAll(db.getExamModifies(user.getId()));

        Button bDesinscription = findViewById(R.id.bDesinscription);

        // Si il n'y a pas de notifs
        if(customList.isEmpty())
            bDesinscription.setVisibility(View.GONE); // Gone ne prend pas de place

        bDesinscription.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ArrayList<Integer> list = customList.getSelectedIds();
                for (int id_exam : list)
                {
                    db.desinscrireUtilisateurAExamen(matricule, id_exam);
                }
                Intent intent = new Intent(getApplicationContext(), Etud_MainPage.class);
                startActivity(intent);
            }
        });

        listView.setAdapter(customList);
    }
}
