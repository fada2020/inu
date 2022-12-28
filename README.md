# [docker kafka](https://velog.io/@shinmj1207/Apache-Kafka-Docker-환경에서-Spring-boot-Kafka-연동하기)
## Docker compose 설정파일 작성
```bash
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka:2.12-2.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```


  version:

        docker-compose 버젼을 지정한다. 여기서는 2 라고 기술했다.

  services:

        docker-compose의 경우 docker 컨테이너로 수행될 서비스들은 services 하위에 기술한다.

  zookeeper:

        서비스 이름을 zookeeper 로 작성했다.

        service 하위에 작성하면 서비스 이름으로 동작한다.

  image:

        우리는 여기서 confluentinc/cp-zookeeper:latest 를 이용할 것이다.

        참고로 실전에서 사용하려면 latest 라는 태그를 사용하지 말고, 정확히 원하는 버젼을 기술해서 사용하길 추천한다.

        latest라고 태그를 지정하면, 매번 컨테이너를 실행할때마다 최신버젼을 다운받아 실행하므로 변경된 버젼으로 인해 원하지 않는 결과를 볼 수 있다. (주의 !!!)

  environment:

        confluentinc 는 몇가지 환경 변수를 설정할 수 있다.

        environment 하위에 필요한 환경을 작성하자.

  ZOOKEEPER_SERVER_ID:

        zookeeper 클러스터에서 유일하게 주키퍼를 식별할 아이디이다.

        동일 클러스터 내에서 이 값은 중복되면 안된다. 단일 브로커이기 때문에 이 값은 의미가 없다.

  ZOOKEEPER_CLIENT_PORT:

        zookeeper_client_port를 지정한다. 여기서는 기본 주키퍼의 포트인 2181로 지정한다.

        즉 컨테이너 내부에서 주키퍼는 2181로 실행된다.

  ZOOKEEPER_TICK_TIME:

        zookeeper이 클러스터를 구성할때 동기화를 위한 기본 틱 타임을 지정한다.

        millisecond로 지정할 수 있으며 여기서는 2000으로 설정했으니 2초가 된다.

  ZOOKEEPER_INIT_LIMIT:

        주키퍼 초기화를 위한 제한 시간을 설정한다.

        주키퍼 클러스터는 쿼럼이라는 과정을 통해서 마스터를 선출하게 된다. 이때 주키퍼들이 리더에게 커넥션을 맺을때 지정할 초기 타임아웃 시간이다.

        타임아웃 시간은 이전에 지정한 ZOOKEEPER_TICK_TIME 단위로 설정된다.

        우리는 ZOOKEEPER_TICK_TIME을 2000으로 지정했고, ZOOKEEPER_INIT_LIMIT을 5로 잡았으므로 2000 * 5 = 10000 밀리세컨이 된다. 즉, 10초가 된다.

        이 옵션은 멀티 브로커에서 유효한 속성이다.

  ZOOKEEPER_SYNC_LIMIT:

        이 시간은 주키퍼 리더와 나머지 서버들의 싱크 타임이다.

        이 시간내 싱크응답이 들어오는 경우 클러스터가 정상으로 구성되어 있늠을 확인하는 시간이다.

        여기서 2로 잡았으므로 2000 * 2 = 4000 으로 4초가 된다.

        이 옵션은 멀티 브로커에서 유효한 속성이다.

  kafka

        kafka 브로커 이름을 지정한다.

  image:

        kafka 브로커는 confluentinc/cp-kafka:latest 를 이용하였다.

        역시 태그는 latest보다는 지정된 버젼을 사용하는것을 추천한다.

  depends_on:

        docker-compose 에서는 서비스들의 우선순위를 지정해 주기 위해서 depends_on 을 이용한다.

        zookeeper 라고 지정하였으므로, kafka는 zookeeper이 먼저 실행되어 있어야 컨테이너가 올라오게 된다.

  ports:

        kafka 브로커의 포트를 의미한다.

        외부포트:컨테이너내부포트 형식으로 지정한다.

  environment:

        kafka 브로커를 위한 환경 변수를 지정한다.

  KAFKA_BROKER_ID:

        kafka 브로커 아이디를 지정한다. 유니크해야하며 지금 예제는 단일 브로커기 때문에 없어도 무방하다.

  KAFKA_ZOOKEEPER_CONNECT:

        kafka가 zookeeper에 커넥션하기 위한 대상을 지정한다.

        여기서는 zookeeper(서비스이름):2181(컨테이너내부포트) 로 대상을 지정했다.

  KAFKA_ADVERTISED_LISTENERS:

        외부에서 접속하기 위한 리스너 설정을 한다.

  KAFKA_LISTENER_SECURITY_PROTOCOL_MAP:

        보안을 위한 프로토콜 매핑이디. 이 설정값은 KAFKA_ADVERTISED_LISTENERS 과 함께 key/value로 매핑된다.

  KAFKA_INTER_BROKER_LISTENER_NAME:

        도커 내부에서 사용할 리스너 이름을 지정한다.

        이전에 매핑된 PLAINTEXT가 사용되었다.

  KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR:

        single 브로커인경우에 지정하여 1로 설정했다.

        멀티 브로커는 기본값을 사용하므로 이 설정이 필요 없다.

  KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS:

        카프카 그룹이 초기 리밸런싱할때 컨슈머들이 컨슈머 그룹에 조인할때 대기 시간이다.

## kafka server.properties 변경
$ docker exec -it kafka /bin/bash  
$ bash# cd /opt/kafka/config  
$ bash# vi server.properties  

## server.properteis
먼저 Socket Server Setting 부분에 listeners와 advertised.listeners의 주석 처리를 해제하고 후자에는 공인 ip를 작성한다.
```bash
############################# Socket Server Settings #############################

# The address the socket server listens on. It will get the value returned from
# java.net.InetAddress.getCanonicalHostName() if not configured.
#   FORMAT:
#     listeners = listener_name://host_name:port
#   EXAMPLE:
#     listeners = PLAINTEXT://your.host.name:9092
listeners=PLAINTEXT://:9092

# Hostname and port the broker will advertise to producers and consumers. If not set,
# it uses the value for "listeners" if configured.  Otherwise, it will use the value
# returned from java.net.InetAddress.getCanonicalHostName().
advertised.listeners=PLAINTEXT://공인ip:9092


delete.topic.enable=true
auto.create.topics.enable=true
```
$ docker stop kafka  
$ docker start kafka  

## docker-compose 실행
docker-compose -f docker-compose-single.yml up -d  

    -f <설정파일>을 통해서 우리가 작성한 설정으로 docker-compose를 실행한다.

    up 옵션을 통해 docker-compos 를 실행한다.

    -d 옵션은 detach 모드로 컨테이너를 백그라운드로 실행하게 해준다.

## docker 상태 로그 확인
docker ps  
docker logs [proccess name]  

## Kafka 테스트
$ bash# cd /opt/kafka/bin  
$ bash# kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic exam-topic  

### Test Terminal1 
$ bash# kafka-console-producer.sh --topic exam-topic --broker-list localhost:9092  
This is a msg;  

### Test Terminal2  
$ bash# kafka-console-consumer.sh --topic exam-topic --bootstrap-server localhost:9092 --from-beginning  
This is a msg;  

## 토픽 생성
docker exec kafka kafka-topics --create --topic my-topic --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1  

docker-compose:

        명령어를 수행한다.

exec:

    컨테이너 내에서 커맨드를 수행하도록 한다.

kafka:

    우리가 설정으로 생성한 브로커(서비스) 이름이다.

kafka-topics:

    카프카 토픽에 대한 명령을 실행한다.

--create:

    토픽을 생성하겠다는 의미이다.

--topic :

    생성할 토픽 이름을 지정한다.

--bootstrap-server service:port

    bootstrap-server는 kafak 브로커 서비스를 나타낸다. 이때 서비스:포트 로 지정하여 접근할 수 있다.

--replication-factor 1:

    복제 계수를 지정한다.

    여기서는 1로 지정했다.

--partition:

    토픽내에 파티션 개수를 지정한다.

## 토픽 확인
docker exec kafka kafka-topics --describe --topic my-topic --bootstrap-server kafka:9092  

docker-compose:

    명령어를 수행한다.

exec:

    컨테이너 내에서 커맨드를 수행하도록 한다.

kafka:

    우리가 설정으로 생성한 브로커(서비스) 이름이다.

kafka-topics:

    카프카 토픽에 대한 명령을 실행한다.

--describe:

    생성된 토픽에 대한 상세 설명을 보여달라는 옵션이다.

--topic :

    생성한 토픽 이름을 지정한다.

--bootstrap-server service:port

    bootstrap-server는 kafak 브로커 서비스를 나타낸다. 이때 서비스:포트 로 지정하여 접근할 수 있다.

결과로 토픽이름, 아이디, 복제계수, 파티션, 리더, 복제정보, isr 등을 확인할 수 있다.  

## 컨슈머 실행
docker exec kafka kafka-console-consumer --topic my-topic --bootstrap-server kafka:9092

    우선 docker-compose exec kafka bash 를 통해서 컨테이너 내부의 쉘로 접속한다.

    이후 kafka-console-consumer 를 이용하여 컨슘한다.

    역시 컨슘할 토픽을 지정하고, 브로커를 지정하기 위해서 --bootstrap-server 를 이용했다.
## 프로듀서 실행
docker exec kafka kafka-console-producer --topic my-topic --broker-list kafka:9092
    
    kafka-console-producer --topic <토픽이름> --broker-list kafka:9092

    위 명령을 통해서 브로커의 토픽에 접근하여 메시지를 생성한다.

    '>' 표시가 나타나면 위와 같이 메시지를 작성해 보자.

## docker-compose 컨테이너 내리기
docker-compose down
## application.yml
```bash
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: foo
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
```
## KafkaConfiguration.java
데이터 전송을 위한 KafkaProducerBean을 만든다.  
그리고 이 ProducerFactory를 이용하는 KafkaTemlate을 통해 데이터를 send하면 된다.  
만약 여러 데이터 타입을 전송하고 싶을 때는 Producer Bean을 여러 개 정의하면 될 듯 하다.  
>참고로 환경변수 파일을 통해 Kafka를 연동할 때 직렬화 방식을 지정하지 않으면  
> SpringSerializer가 `default`로 설정된다.  
하지만, java bean으로 설정 시 key, value의 직렬화 정보를 지정하지 않으면 default 값이  
> **자동으로 구성되지 않으니** 에러가 발생하지 않도록 조심하자.  
```java
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {

        Map<String,Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory(configs);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }
}

```
## KafkaProducer.java
Configuration 파일에서 정의한 kafkaTemplate 빈을 이용해 “exam-topic”이라는 이름의 토픽을  
전송할 수 있도록 메서드를 정의한다.  
```java
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private static final String TOPIC = "exam-topic";

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        log.info("Produce message : {}", message);
        this.kafkaTemplate.send(TOPIC, message);
    }
}

```
## KafkaConsumer.java
다음은 exam-topic이라는 이름의 토픽이 전달됐다면 해당 메세지들을 가져올 수 있게끔 하는  
KafkaListener를 달아 정의해주면 된다.  
```java
@Service
@Slf4j
public class KafkaConsumer {
    @KafkaListener(topics = "exam-topic", groupId = "foo")
    public void consume(String message) throws IOException {
        log.info("Consumed message : {}", message);
    }
}
```
## KafkaController.java
```java
@RestController
@RequestMapping(value = "/kafka")
@Slf4j
public class KafkaController {

    private final KafkaProducer producer;

    @Autowired
    KafkaController(KafkaProducer producer){
        this.producer = producer;
    }


    @PostMapping
    @ResponseBody
    public String sendMessage(@RequestParam String message) {
        log.info("message : {}", message);
        this.producer.sendMessage(message);

        return "success";
    }
}
```
## 테스트
이제 애플리케이션을 구동시켜 작성한 코드가 잘 돌아가는지 확인해보자.  
먼저 terminal로 kafka-console-consumer.sh 을 동작시키고  
>$ kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic exam-topic --from-beginning

