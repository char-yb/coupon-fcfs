package com.fcfs.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	// 데이터베이스의 unique 키를 활용하여 한 사람당 한 개의 쿠폰을 발급받도록 할 수 있다.
	// 그러나, 보통 서비스는 한 유저가 같은 타입의 쿠폰을 여러 개 가질 수 있기에 실용적이지 않다.

	// private Long couponType;

	public Coupon(Long userId) {
		this.userId = userId;
	}
}
