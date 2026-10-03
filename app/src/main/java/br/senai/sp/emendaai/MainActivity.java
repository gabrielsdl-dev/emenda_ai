package br.senai.sp.emendaai;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.senai.sp.emendaai.adapter.FeriadoAdapter;
import br.senai.sp.emendaai.api.BrasilApiService;
import br.senai.sp.emendaai.model.Feriado;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String CHAVE_ANO = "ano_selecionado";
    private static final String CHAVE_FERIADOS = "feriados_carregados";

    /** Os quatro estados da tela. */
    private enum Estado {
        CARREGANDO, CONTEUDO, VAZIO, ERRO
    }

    private ProgressBar progresso;
    private RecyclerView lista;
    private View blocoErro;
    private TextView txtMensagemErro;
    private TextView txtVazio;

    private final Button[] botoesAno = new Button[3];

    private int anoSelecionado;

    private List<Feriado> listaFeriado = new ArrayList<>();
    private FeriadoAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progresso = findViewById(R.id.progresso);
        lista = findViewById(R.id.listaFeriados);
        blocoErro = findViewById(R.id.blocoErro);
        txtMensagemErro = findViewById(R.id.txtMensagemErro);
        txtVazio = findViewById(R.id.txtVazio);

        botoesAno[0] = findViewById(R.id.btnAno1);
        botoesAno[1] = findViewById(R.id.btnAno2);
        botoesAno[2] = findViewById(R.id.btnAno3);

        int anoAtual = LocalDate.now().getYear();
        for (int i = 0; i < botoesAno.length; i++) {
            final int ano = anoAtual + i;
            botoesAno[i].setText(String.valueOf(ano));

            botoesAno[i].setOnClickListener(v -> {
                anoSelecionado = ano;
                marcarBotaoDoAno();
                carregarFeriados();

            });
        }

//        Configurando o Recicle View
        adapter = new FeriadoAdapter(listaFeriado);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);


    }

    private void carregarFeriados() {
        //Ativar RETROFIT

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://brasilapi.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BrasilApiService api = retrofit.create(BrasilApiService.class);

        //Fim

        Call<List<Feriado>> requisicao = api.buscarFeriados(anoSelecionado);

        requisicao.enqueue(new Callback<List<Feriado>>() {
            @Override
            public void onResponse(Call<List<Feriado>> call, Response<List<Feriado>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Está Vazio", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Feriado> corpo = response.body();
                int quantidade = corpo.size();

                listaFeriado.clear();
                listaFeriado.addAll(corpo);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Feriado>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro de conexão", Toast.LENGTH_SHORT).show();


            }
        });
    }


    private void mostrarErro(String mensagem) {
        txtMensagemErro.setText(mensagem);
        mostrar(Estado.ERRO);
    }

    private void mostrar(Estado estado) {
        progresso.setVisibility(estado == Estado.CARREGANDO ? View.VISIBLE : View.GONE);
        lista.setVisibility(estado == Estado.CONTEUDO ? View.VISIBLE : View.GONE);
        txtVazio.setVisibility(estado == Estado.VAZIO ? View.VISIBLE : View.GONE);
        blocoErro.setVisibility(estado == Estado.ERRO ? View.VISIBLE : View.GONE);
    }

    private void marcarBotaoDoAno() {
        for (Button botao : botoesAno) {
            boolean escolhido = botao.getText().toString().equals(String.valueOf(anoSelecionado));
            botao.setAlpha(escolhido ? 1f : 0.45f);
        }
    }

}
