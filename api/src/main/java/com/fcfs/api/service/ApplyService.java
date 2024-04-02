package com.fcfs.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcfs.api.domain.Coupon;
import com.fcfs.api.repository.CouponCountRepository;
import com.fcfs.api.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplyService {

	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;

	public void apply(Long userId) {
		// long count = couponRepository.count();
		Long count = couponCountRepository.increment();

		/*
		 현재 방식은 coupon 갯수를 가져와 발급이 가능하면 rdb에 저장하는 방식이다.
		 만약 쿠폰의 갯수가 많아지면 많아질수록 rdb에 connection 을 계속 사용하여 부하가 발생하는 단점이 존재.

		 * 주어진 유스케이스
		 mysql은 1분에 100개의 insert가 가능하다고 한다.
		 1. 10시에 10000개의 쿠폰 생성 요청이 발생
		 2. 10시 1분에 주문생성 요청
		 3. 10시 2분에 회원가입 요청

		 만약 타임아웃이 없다면, 좀 늦어도 100분뒤에 모든 요청이 완료되겠지만
		 대부분 서비스는 타임아웃이 존재할 것이다.
		 그러니 타임아웃에 따라 쿠폰 생성을 일부 못하게 될 것이다.

		 또한 DB 리소스를 많이 사용하기에 부하가 발생된다.
		 AWS를 사용한 구성으로 LB(Load Balancer)를 활용하는 방법도 있다.
		 로드밸런서는 여러 대의 애플리케이션에 트래픽을 분배할 수 있도록 도와주는 서비스로,
		 클라이언트가 쿠폰 생성 API 요청 시 트래픽을 적절히 분배해주고, API에서 Mysql을 사용할 수 있도록 한다.

		 ngrinder는 부하 테스트 툴로 구성해놓는다면 서버에 단기간에 많은 트래픽을 발생시킬 수 있다.
		 */

		if (count > 100) {
			return;
		}

		couponRepository.save(new Coupon(userId));
	}
}
