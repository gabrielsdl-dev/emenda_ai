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

import br.senai.sp.emendaai.api.BrasilApiService;
import br.senai.sp.emendaai.model.Endereco;
import br.senai.sp.emendaai.util.Datas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetalheFeriadoActivity extends AppCompatActivity {


    private EditText campoCep;
    private ProgressBar progressoCep;
    private TextView txtResultadoCep;
    private Button btnCompartilhar;

    private BrasilApiService api;
    private Button btnBuscarCep;


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



        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            finish();
            return;
        }

        String nome = bundle.getString("feriado_nome", "");
        String data = bundle.getString("feriado_data", "");

        if (data.isEmpty()) {
            finish();
            return;
        }

        txtNome.setText(nome);
        txtData.setText(
                Datas.dia(data) + " de " + Datas.mesCurto(data)
                        + " • " + Datas.diaDaSemana(data)
        );
        txtContagem.setText(Datas.contagem(data));

        txtEmenda.setVisibility(
                Datas.ehEmenda(data) ? View.VISIBLE : View.GONE
        );

// Será habilitado quando a consulta retornar um endereço.
        btnCompartilhar.setEnabled(false);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://brasilapi.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(BrasilApiService.class);

        btnBuscarCep = findViewById(R.id.btnBuscarCep);
        btnBuscarCep.setOnClickListener(view -> buscarCep());
        btnCompartilhar.setOnClickListener(view -> compartilhar());


    }


    private void buscarCep() {
        String entrada = campoCep.getText().toString().trim();

        // Aceita oito números, com ou sem hífen.
        if (!entrada.matches("[0-9]{8}|[0-9]{5}-[0-9]{3}")) {
            campoCep.setError("Informe um CEP com 8 números.");
            campoCep.requestFocus();
            return;
        }

        String cep = entrada.replace("-", "");

        campoCep.setError(null);
        txtResultadoCep.setVisibility(View.GONE);
        btnCompartilhar.setEnabled(false);
        mostrarCarregamentoCep(true);

        api.buscarCep(cep).enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(
                    @NonNull Call<Endereco> call,
                    @NonNull Response<Endereco> response
            ) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }

                mostrarCarregamentoCep(false);

                if (response.code() == 404) {
                    mostrarErroCep("CEP não encontrado.");
                    return;
                }

                if (!response.isSuccessful()) {
                    mostrarErroCep(
                            "Erro ao consultar o CEP. Código: "
                                    + response.code()
                    );
                    return;
                }

                Endereco endereco = response.body();

                if (endereco == null) {
                    mostrarErroCep("A consulta não retornou um endereço.");
                    return;
                }

                String resultado =
                        "CEP: " + textoOuPadrao(endereco.getCep())
                                + "\nRua: " + textoOuPadrao(endereco.getRua())
                                + "\nBairro: " + textoOuPadrao(endereco.getBairro())
                                + "\nCidade: " + textoOuPadrao(endereco.getCidade())
                                + "\nEstado: " + textoOuPadrao(endereco.getEstado());

                mostrarEndereco(resultado);
            }

            @Override
            public void onFailure(
                    @NonNull Call<Endereco> call,
                    @NonNull Throwable t
            ) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }

                mostrarCarregamentoCep(false);
                mostrarErroCep(
                        "Falha na consulta. Verifique sua conexão "
                                + "e tente novamente."
                );

                Log.e("ConsultaCEP", "Falha ao buscar CEP", t);
            }
        });
    }

    private void mostrarCarregamentoCep(boolean carregando) {
        progressoCep.setVisibility(
                carregando ? View.VISIBLE : View.GONE
        );

        btnBuscarCep.setEnabled(!carregando);
        campoCep.setEnabled(!carregando);
    }

    private void mostrarErroCep(String mensagem) {
        txtResultadoCep.setText(mensagem);
        txtResultadoCep.setVisibility(View.VISIBLE);
        btnCompartilhar.setEnabled(false);
    }

    private String textoOuPadrao(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return "Não informado";
        }

        return valor;
    }



    private void mostrarEndereco(String endereco) {
        txtResultadoCep.setText(endereco);
        txtResultadoCep.setVisibility(View.VISIBLE);
        btnCompartilhar.setEnabled(true);
    }


    private void compartilhar() {
        TextView txtNome = findViewById(R.id.txtNomeFeriado);
        TextView txtData = findViewById(R.id.txtDataFeriado);

        String texto = "Emenda Aí!"
                + "\nFeriado: " + txtNome.getText().toString()
                + "\nData: " + txtData.getText().toString()
                + "\n\nDestino:\n"
                + txtResultadoCep.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);

        startActivity(
                Intent.createChooser(intent, "Compartilhar feriado")
        );
    }


}
