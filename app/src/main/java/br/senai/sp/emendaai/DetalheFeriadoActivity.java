package br.senai.sp.emendaai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.senai.sp.emendaai.util.Datas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetalheFeriadoActivity extends AppCompatActivity {


    private EditText campoCep;
    private ProgressBar progressoCep;
    private TextView txtResultadoCep;
    private Button btnCompartilhar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_feriado);

        TextView txtNome = findViewById(R.id.txtNomeFeriado);
        TextView txtData = findViewById(R.id.txtDataFeriado);
        TextView txtContagem = findViewById(R.id.txtContagemFeriado);
        TextView txtEmenda = findViewById(R.id.txtAvisoEmenda);

        campoCep = findViewById(R.id.campoCep);
        progressoCep = findViewById(R.id.progressoCep);
        txtResultadoCep = findViewById(R.id.txtResultadoCep);
        btnCompartilhar = findViewById(R.id.btnCompartilhar);


    }



    private void mostrarEndereco(String endereco) {
        txtResultadoCep.setText(endereco);
        txtResultadoCep.setVisibility(View.VISIBLE);
        btnCompartilhar.setEnabled(true);
    }


}
