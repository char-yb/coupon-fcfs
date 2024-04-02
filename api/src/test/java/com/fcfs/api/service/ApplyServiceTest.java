package com.fcfs.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fcfs.api.repository.CouponRepository;

@SpringBootTest
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Test
	public void 한번만_응모() {
		applyService.apply(1L);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

	// 동시에 여러 요청이 들어왔을 경우,
	@Test
	public void 여러_번_응모() throws InterruptedException {
		/* 동시에 여러 요청이 들어오기에 멀티 스레드를 활용
			ExecutorService를 사용하는데, 병렬 작업을 간단하게 할 수 있는 자바의 API이다.
		 */
		int threadCount = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		// 모든 요청이 끝날 때까지 기다리는 CountDownLatch를 활용
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.apply(userId);
				} finally {
					latch.countDown();
				}
			});
		}

		// 모든 요청이 왔는 지 대기
		latch.await();

		long count = couponRepository.count();
		// 요청이 완료되면 생성된 쿠폰의 갯수 확인
		assertThat(count).isEqualTo(100);

		/* 레이스 컨디션 발생 여부 고려
		레이스 컨디션이란?
		두 개 이상의 스레드가 공유 데이터에 access를 하고 동시에 작업을 하려고 할 때 발생되는 문제
		 */
	}
}