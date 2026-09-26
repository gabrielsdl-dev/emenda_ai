# Emenda Aí!

Lista os feriados nacionais do ano, marca quais caem em terça ou quinta (os que
dão emenda) e ajuda a turma a combinar o ponto de encontro pelo CEP.



## A API

Tudo vem da [BrasilAPI](https://brasilapi.com.br). É pública, não pede cadastro e
não usa chave, então nada de segredo dentro do código do app.

| Caminho | Devolve |
|---|---|
| `GET /api/feriados/v1/{ano}` | array de feriados nacionais |
| `GET /api/cep/v2/{cep}` | objeto com rua, bairro, cidade e UF |


# emenda_ai
