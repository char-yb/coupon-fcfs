package com.fcfs.api.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// 토픽의 데이터를 전송할 프로듀서
@Component
@RequiredArgsConstructor
public class CouponCreateProducer {

	private final KafkaTemplate<String, Long> kafkaTemplate;

	// 토픽의 userId를 전달할 것이기에 userId를 매개변수로 가지는 메서드 생성

	/** 토픽 생성 Command
	 * @param userId
	 * docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic coupon_create
	 * docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092 --key-deserializer
	 "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
	 */
	public void create(Long userId) {
		kafkaTemplate.send("coupon_create", userId);
	}
}
