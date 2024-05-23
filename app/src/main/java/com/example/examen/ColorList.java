package com.example.examen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ColorList extends AppCompatActivity {

    Spinner colorsSpinner, timesSpinner;
    String[] colors = {"Rojo", "Amarillo", "Verde", "Azul"};
    String[] times = {"1", "5", "10", "15", "20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        colorsSpinner = findViewById(R.id.colorsSpinner);
        timesSpinner = findViewById(R.id.timesSpinner);

        ArrayAdapter<String> colorsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        colorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorsSpinner.setAdapter(colorsAdapter);

        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, times);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timesSpinner.setAdapter(timesAdapter);

        findViewById(R.id.proceedButton).setOnClickListener(v -> checkSelectionAndProceed());

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> finish());
    }

    private void checkSelectionAndProceed() {
        String selectedColor = colorsSpinner.getSelectedItem().toString();
        String selectedTime = timesSpinner.getSelectedItem().toString();

        if (selectedColor.equals("Azul") && selectedTime.equals("15")) {
            startActivity(new Intent(ColorList.this, FractalActivity.class));
        } else {
            Toast.makeText(ColorList.this, "Color or time not matching", Toast.LENGTH_SHORT).show();
        }
    }
}
