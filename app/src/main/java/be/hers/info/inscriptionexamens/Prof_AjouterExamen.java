package be.hers.info.inscriptionexamens;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import be.hers.info.inscriptionexamens.database.ExamDB;
import be.hers.info.inscriptionexamens.model.Examen;
import be.hers.info.inscriptionexamens.model.TypeExamen;

public class Prof_AjouterExamen extends AppCompatActivity
{
    private LocalDate curDate;
    private LocalTime curTime;

    /**
     * initialise le spinner des types d'examens
     */
    private void initSpinnerTypeExam()
    {
        Spinner spinner = findViewById(R.id.iTypeExam);

        // Adapt
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter);

        // ajout des types d'examens
        for(TypeExamen t : TypeExamen.values())
        {
            adapter.add(t.toString());
        }
    }

    /**
     *
     */
    private void initSpinnerCours()
    {
        // TODO
    }


    /**
     * Créé un objet
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void creerExamen()
    {
        LocalDateTime date = LocalDateTime.of(curDate, curTime);

        EditText iDescription = findViewById(R.id.iDescription);
        String description = iDescription.getText().toString();

        EditText iDuree = findViewById(R.id.iDuree);
        String duree = iDuree.getText().toString();
        if(duree.length() <= 0)
            duree = "0";

        Spinner iType = findViewById(R.id.iTypeExam);
        String type = iType.getSelectedItem().toString();

        Examen exam1 = new Examen(
                1,
                TypeExamen.valueOf(type),
                description,
                date,
                Integer.parseInt(duree));

        // Ajout dans la Db
        final ExamDB db = new ExamDB(this);
        db.addExamen(exam1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_ajoutexamen);

        initSpinnerTypeExam();

        // Changement de Date
        CalendarView calendarView = findViewById(R.id.iDate);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                // Le mois en attribut commence à 0 (pour Janvier)
                curDate = LocalDate.of(year, month+1, dayOfMonth);
            }
        });

        // Changement d'heure
        TimePicker timePicker = findViewById(R.id.iTime);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
            {
                curTime = LocalTime.of(hourOfDay, minute);
            }
        });

        // Press Button
        Button validation = findViewById(R.id.bValider);
        validation.setOnClickListener(
                new View.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v)
                    {
                        creerExamen();
                    }
                }
        );
    }
}