package be.hers.info.inscriptionexamens;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import be.hers.info.inscriptionexamens.custom.AdapterListView_Examen;
import be.hers.info.inscriptionexamens.database.ExamDB;
import be.hers.info.inscriptionexamens.model.Examen;
import be.hers.info.inscriptionexamens.model.Utilisateur;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Etud_DesinscriptionExamen extends AppCompatActivity
{
    private AdapterListView_Examen customList;
    private final ExamDB db = new ExamDB(this);

    /**
     * Initialise la listView
     * @param refUtilisateur id de l'utilisateur connecté
     */
    private void initList(int refUtilisateur)
    {
        // Liste d'examens inscits
        List<Examen> examens = db.getExamenByInscription(refUtilisateur);

        customList.addAll(examens);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etud_desinscription);

        // Adapter la liste au model custom
        this.customList = new AdapterListView_Examen(this, new ArrayList<Examen>());
        ListView listView = findViewById(R.id.customListExams);
        listView.setAdapter(customList);


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("USER", Activity.MODE_PRIVATE);
        final String matricule = preferences.getString("MATRICULE", "VIDE");

        // Recup l'utilisateur
        Utilisateur user = db.getUtilisateur(matricule);

        // init liste
        initList(user.getId());

        Button bDesinscription = findViewById(R.id.bDesinscription);
        bDesinscription.setOnClickListener
        (
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        ArrayList<Integer> list = customList.getSelectedIds();
                        for (int id_exam : list)
                        {
                            if(db.desinscrireUtilisateurAExamen(matricule, id_exam))
                                System.out.println("DESINSCRIT MHUAHAHAHAHA");
                        }
                        Intent intent = new Intent(getApplicationContext(), Etud_MainPage.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
