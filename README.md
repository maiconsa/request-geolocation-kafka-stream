# KafkaStreams: Solicitando Geolocation pelo IP com restrição de tempo entre as chamadas.


## Tecnologias e práticas utilizadas
- Java 11
- Docker
- Docker Compose
- Design Patterns: Abstract Factory , Factory , Strategy, Builder]
- JaCoCo para relatório de testes
- JUnit,Mockito
- Arquitetura Hexagonal
- Test Driven Design


## Exemplo payload:

{ "clientId": "e0001020-566e-4d8a-a834-ca73c4132aff", "ip": "134.92.114.214" }


## Demonstração

 Os testes a seguir foram realizados com as seguintes configurações:
 Tópico entrada: requested-geolocation
 Tópico saída: api-result-gelocation
 Janela de restrição: 2 minutos
 Ip: 134.92.114.214
 
 Client Id: e0001020-566e-4d8a-a834-ca73c4132aff


### Evidências

1 - Primeira Requisição

Neste caso a mensagem foi processada e solicitado as informação a api do IpStack e a resposta armazenada no cache em memoria.
![Logs primeira requisição](https://github.com/maiconsa/request-geolocation-kafka-stream/blob/main/imgs/first-request.png)

2 - Segunda Requisição dentro da janela de restrição
Em seguida não foi possível processar a requisição foi estava dentro da janela de restrição para o cliente e IP
![Logs segunda requisição](https://github.com/maiconsa/request-geolocation-kafka-stream/blob/main/imgs/second-request.png)

3 - Terceira Requisição - Fora da janela  de restrição
Por fim após passar a janela de restrição foi possível processar a chamada a api IpStack
![Logs terceira requisição requisição](https://github.com/maiconsa/request-geolocation-kafka-stream/blob/main/imgs/third-request.png)


4 - Resumo requisições enviadas
Como é possível ver forma feitas 3 requisições no tópico de entrada.
![Eventos enviados](https://github.com/maiconsa/request-geolocation-kafka-stream/blob/main/imgs/request-events.png)

5 - Resumo requisições respondidas
Apesas de terem sido geradas 3 mensagems no tópico  de entrada apenas duas foram processadas e a resposta enviadas ao tópico de saida.
![Eventos responsidos](https://github.com/maiconsa/request-geolocation-kafka-stream/blob/main/imgs/result-events.png)

6 - Resposta optida pelo IpStack
 A resposta obtida pelo api de geolocalização IpStack foi:

![Resposta IpStack](https://github.com/maiconsa/request-geolocation-kafka-stream/blob/main/imgs/response-payload-event.png)

