package cl.ipvg.georegistro;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    EditText etTitulo, etDescripcion, etCategoria;
    TextView tvFechaHora, tvCoordenadas;
    Button btnGuardar;

    // Cliente GPS
    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    // Cliente Firebase Firestore
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCategoria = findViewById(R.id.etCategoria);
        tvFechaHora = findViewById(R.id.tvFechaHora);
        tvCoordenadas = findViewById(R.id.tvCoordenadas);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Inicializar la base de datos de Firebase
        db = FirebaseFirestore.getInstance();

        String fechaActual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        tvFechaHora.setText("Fecha y Hora: " + fechaActual);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        obtenerUbicacion();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
            }
        });
    }

    private void obtenerUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        tvCoordenadas.setText("Coordenadas: " + location.getLatitude() + ", " + location.getLongitude());
                    } else {
                        tvCoordenadas.setText("Coordenadas: No se pudo obtener la ubicación");
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                tvCoordenadas.setText("Coordenadas: Permiso denegado");
                Toast.makeText(this, "Debe aceptar el permiso para registrar la ubicación", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validarFormulario() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String fechaHora = tvFechaHora.getText().toString();
        String coordenadas = tvCoordenadas.getText().toString();

        if (titulo.isEmpty() || descripcion.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Error: Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
        } else {
            // Empaquetar los datos para enviarlos a Firebase
            Map<String, Object> incidencia = new HashMap<>();
            incidencia.put("titulo", titulo);
            incidencia.put("descripcion", descripcion);
            incidencia.put("categoria", categoria);
            incidencia.put("fechaHora", fechaHora);
            incidencia.put("coordenadas", coordenadas);

            // Guardar en la colección "Incidencias"
            db.collection("Incidencias")
                    .add(incidencia)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(RegistroActivity.this, "¡Guardado con éxito en la nube!", Toast.LENGTH_LONG).show();
                            finish(); // Esto cierra la pantalla y te devuelve al menú principal
                        }
                    });
        }
    }
}