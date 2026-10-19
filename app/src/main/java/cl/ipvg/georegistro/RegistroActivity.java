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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {

    EditText etTitulo, etDescripcion, etCategoria;
    TextView tvFechaHora, tvCoordenadas;
    Button btnGuardar;

    // Cliente para obtener la ubicación GPS
    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Enlazar variables con el diseño XML
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCategoria = findViewById(R.id.etCategoria);
        tvFechaHora = findViewById(R.id.tvFechaHora);
        tvCoordenadas = findViewById(R.id.tvCoordenadas);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Generar Fecha y Hora automáticamente
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        tvFechaHora.setText("Fecha y Hora: " + fechaActual);

        // Inicializar el cliente de GPS
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Solicitar permisos y obtener la ubicación inmediatamente al abrir la pantalla
        obtenerUbicacion();

        // Acción al presionar el botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
            }
        });
    }

    private void obtenerUbicacion() {
        // Verificar si ya tenemos permiso del usuario
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si no tenemos permiso, lo solicitamos en pantalla
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Si ya tenemos permiso, leemos las coordenadas del GPS
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

    // Este método captura la respuesta del usuario (si Acepta o Deniega el permiso de ubicación)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el usuario aceptó, leemos la ubicación
                obtenerUbicacion();
            } else {
                // Si el usuario denegó, mostramos un mensaje
                tvCoordenadas.setText("Coordenadas: Permiso denegado");
                Toast.makeText(this, "Debe aceptar el permiso para registrar la ubicación", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void validarFormulario() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();

        // Verificar que no estén vacíos
        if (titulo.isEmpty() || descripcion.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Error: Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Validación correcta. Listo para guardar.", Toast.LENGTH_SHORT).show();
        }
    }
}