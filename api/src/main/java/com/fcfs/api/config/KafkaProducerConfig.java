package com.fcfs.api.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

	/** Topic의 데이터를 전송하기 위한 Producer 설정
	 * Producer 인스턴스 생성을 위한 설정 값 정의
	 */
	@Bean
	public ProducerFactory<String, Long> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		// 서버 정보 추가
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9092");
		// key serialize 정보 추가
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		// value serialize 정보 추가
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

		return new DefaultKafkaProducerFactory<>(config);
	}

	// 카프카 토픽의 전송을 위한 카프카 템플릿 생성
	@Bean
	public KafkaTemplate<String, Long> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	// @Bean
	// public ConsumerFactory<String, String> consumerFactory() {
	// 	return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	// }
	//
	// @Bean
	// public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
	// 	ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
	// 	factory.setConsumerFactory(consumerFactory());
	// 	return factory;
	// }
}
