package io.github.wolfterro.ipdroid;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Elementos da MainActivity
    // =========================
    private Button button;      // Carregar Informações

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciando o resgate de informações
                // ==================================
                ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setTitle(getString(R.string.gettingInformations));
                pd.setMessage(getString(R.string.pleaseWait));
                pd.setCancelable(false);
                pd.show();

                IPInfoThread iit = new IPInfoThread(MainActivity.this, pd);
                iit.start();
            }
        });
    }
}
