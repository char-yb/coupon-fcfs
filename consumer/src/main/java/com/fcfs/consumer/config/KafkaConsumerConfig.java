package com.fcfs.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KafkaConsumerConfig {
	// 컨슈머 인스턴스를 위한 설정값 config
	@Bean
	public ConsumerFactory<String, Long> consumerFactory() {
		Map<String, Object> config = new HashMap<>();
		// 서버 정보 추가
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		// GROUP ID
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");
		// key serialize 정보 추가
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		// value serialize 정보 추가
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(config);
	}

	// 토픽으로부터 메세지를 받을 Listener를 생성
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Long> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();
		// 컨슈머 팩토리 setting
		factory.setConsumerFactory(consumerFactory());

		return factory;
	}
}
