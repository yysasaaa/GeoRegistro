package cl.ipvg.georegistro;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnRegistro, btnListado, btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlazamos las variables con los botones del diseño
        btnRegistro = findViewById(R.id.btnRegistro);
        btnListado = findViewById(R.id.btnListado);
        btnMapa = findViewById(R.id.btnMapa);

        // Botón al registro
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistroActivity.class));
            }
        });

        // Botón al listado
        btnListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListadoActivity.class));
            }
        });

        // Botón al mapa
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapaActivity.class));
            }
        });
    }
}