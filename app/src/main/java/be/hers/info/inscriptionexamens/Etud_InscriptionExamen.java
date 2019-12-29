package be.hers.info.inscriptionexamens;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import be.hers.info.inscriptionexamens.custom.AdapterListView_Examen;
import be.hers.info.inscriptionexamens.database.ExamDB;
import be.hers.info.inscriptionexamens.model.Examen;

public class Etud_InscriptionExamen extends AppCompatActivity
{
    private AdapterListView_Examen customList;

    /**
     * Initialise la listView d'examens
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initList()
    {
        final ExamDB db = new ExamDB(this);
        customList.addAll(db.getAllExamen());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etud_inscription);

        // Adapter la liste au model custom
        this.customList = new AdapterListView_Examen(this, new ArrayList<Examen>());
        ListView listView = findViewById(R.id.customListExams);
        listView.setAdapter(customList);

        // init liste
        initList();

        Button bInscription = findViewById(R.id.bInscription);
        bInscription.setOnClickListener
        (
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        ArrayList<Integer> list = customList.getSelectedIds();
                        System.out.println("NOMBRE SELECTION " + list.size());
                    }
                }
        );
    }
}
