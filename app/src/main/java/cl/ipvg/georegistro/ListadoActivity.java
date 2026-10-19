package cl.ipvg.georegistro;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ListadoActivity extends AppCompatActivity {

    ListView listViewIncidencias;
    FirebaseFirestore db;
    ArrayList<String> listaDatos;
    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        listViewIncidencias = findViewById(R.id.listViewIncidencias);
        db = FirebaseFirestore.getInstance();

        listaDatos = new ArrayList<>();

        // Cargar los datos desde Firebase Firestore
        cargarDatosDesdeFirebase();
    }

    private void cargarDatosDesdeFirebase() {
        db.collection("Incidencias")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extraer los campos de cada documento guardado
                            String titulo = document.getString("titulo");
                            String categoria = document.getString("categoria");
                            String fecha = document.getString("fechaHora");
                            String coordenadas = document.getString("coordenadas");

                            // Formatear el texto que se verá en la lista sin emojis
                            String item = "Titulo: " + titulo + "\nCat: " + categoria + "\nFecha: " + fecha + "\n" + coordenadas;
                            listaDatos.add(item);
                        }

                        // Mostrar los datos en el ListView usando un adaptador simple
                        adaptador = new ArrayAdapter<>(ListadoActivity.this, android.R.layout.simple_list_item_1, listaDatos);
                        listViewIncidencias.setAdapter(adaptador);

                    } else {
                        Toast.makeText(ListadoActivity.this, "Error al cargar el historial.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}