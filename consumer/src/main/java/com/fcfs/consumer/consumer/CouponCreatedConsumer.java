package com.fcfs.consumer.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fcfs.consumer.domain.Coupon;
import com.fcfs.consumer.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponCreatedConsumer {

	private final CouponRepository couponRepository;

	@KafkaListener(topics = "coupon_create", groupId = "group_1")
	public void listener(Long userId) {
		// System.out.println("userId: " + userId);
		/*
		 여기서 테스트 케이스를 실행하면, 실패가 될 것이다.
		 왜냐하면, 데이터 처리가 실시간이 아니기 때문이다.
		 * 컨슈머는 데이터를 수신하고 있다가 토픽에 데이터가 전송되면 데이터를 받아서 쿠폰을 생성한다.
		 컨슈머에서 쿠폰이 모두 생성되는 시각과 조회하는 시각이 다르며, 갯수 차이가 발생된다.
		 */
		couponRepository.save(new Coupon(userId));
	}
}
