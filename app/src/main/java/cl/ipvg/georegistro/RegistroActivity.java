package cl.ipvg.georegistro;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {

    EditText etTitulo, etDescripcion, etCategoria;
    TextView tvFechaHora, tvCoordenadas;
    Button btnGuardar;

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

        // Acción al presionar el botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFormulario();
            }
        });
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
            // (En el próximo paso conectaremos esto a Firebase)
        }
    }
}