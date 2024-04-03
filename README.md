# 요구사항 정의
```
선착순 100명에게 할인쿠폰을 제공하는 이벤트를 진행하고자 한다.

이 이벤트는 아래와 같은 조건을 만족하여야 한다.
- 선착순 100명에게만 지급되어야 한다.
- 101개 이상이 지급되면 안된다.
- 순간적으로 몰리는 트래픽을 버틸 수 있어야 한다.
```

```
# 카프카
분산 이벤트 스트리밍 플랫폼
이벤트 스트리밍이란 소스에서 목적지까지 이벤트를 실시간으로 스트리밍 하는 것

카프카의 기본 구조
Topic -> Producer <- Consumer

Topic은 기본적으로 Queue 구조
Topic에 데이터를 삽입할 수 있는 기능을 가진 것이 Producer
반대로 Topic에 삽입된 데이터를 가져올 수 있는 것이 Consumer
```

토픽 생성
```yaml
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic testTopic
```

프로듀서 실행

```yaml
docker exec -it kafka kafka-console-producer.sh --topic testTopic --broker-list 0.0.0.0:9092
```

컨슈머 실행

```yaml
docker exec -it kafka kafka-console-consumer.sh --topic testTopic --bootstrap-server localhost:9092
```
