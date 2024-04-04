package com.fcfs.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fcfs.api.repository.CouponRepository;

@SpringBootTest
@ActiveProfiles("test")
class ApplyServiceTest {
	// Test 시작 전, docker exec -it {redis 컨테이너} redis-cli로 접속하여 flushall을 해준다.
	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Test
	public void 한번만_응모() {
		// redis incr은 key:value에서 1 씩 증가시키는 명령어이다.
		// incr은 성능도 빠르고, 데이터 정합성도 지킬 수 있는 활용도가 있기에
		// 발급된 쿠폰 갯수를 제어하기 위해서 incr 명령어를 사용할 것이다.
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
		// CountDownLatch: https://dev-monkey-dugi.tistory.com/152
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 1; i <= threadCount; i++) {
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

		/*
		 일시적으로 Thread sleep을 준다.
		 카프카를 사용한다면 API에서 직접 쿠폰을 생성할 때에 비해서 처리량을 조절할 수 있다.
		 처리량을 조절함으로서 데이터베이스의 부하를 줄일 수 있다.
		 다만, 테스트 케이스에서 쿠폰 생성까지 약간의 텀이 존재하는 단점이 존재한다.
		 */
		Thread.sleep(10000);

		long count = couponRepository.count();
		// 요청이 완료되면 생성된 쿠폰의 갯수 확인
		assertThat(count).isEqualTo(100);

		/* 레이스 컨디션 발생 여부 고려
		레이스 컨디션이란?
		두 개 이상의 스레드가 공유 데이터에 access를 하고 동시에 작업을 하려고 할 때 발생되는 문제

		Synchronized 를 활용한 방법도 존재할 수도 있지만, 서버를 여러 대 사용한다면 해결방법으로 옳지 않다.
		Mysql, redis를 활용한 락을 구현하여 해결할 수도 있지만, 우리가 원하는 건 쿠폰 갯수에 대한 정합성이기에 락으로 해결한다면,
		발급된 쿠폰의 수를 가져오는 것부터 쿠폰을 생성하는 데까지 락을 걸어야 한다.

		* flushall: 레디스 데이터 초기화 명령어

		테스트 케이스가 성공한 이유
		- redis는 싱글 스레드 기반 동작하기에, 스레드 1에서 작업 완료전까지 wait 상태가 된다.
		- 그래서 모든 스레드에서는 언제나 최신값을 가져오기에 쿠폰 갯수에 대한 정합성을 해결할 수 있다.
		 */
	}

	@Test
	public void 한명당_한개의_쿠폰만_발급() throws InterruptedException {
		/* 동시에 여러 요청이 들어오기에 멀티 스레드를 활용
			ExecutorService를 사용하는데, 병렬 작업을 간단하게 할 수 있는 자바의 API이다.
		 */
		int threadCount = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		// 모든 요청이 끝날 때까지 기다리는 CountDownLatch를 활용
		// CountDownLatch: https://dev-monkey-dugi.tistory.com/152
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 1; i <= threadCount; i++) {
			// long userId = i;
			executorService.submit(() -> {
				try {
					applyService.apply(1L);
				} finally {
					latch.countDown();
				}
			});
		}

		// 모든 요청이 왔는 지 대기
		latch.await();

		/*
		 일시적으로 Thread sleep을 준다.
		 카프카를 사용한다면 API에서 직접 쿠폰을 생성할 때에 비해서 처리량을 조절할 수 있다.
		 처리량을 조절함으로서 데이터베이스의 부하를 줄일 수 있다.
		 다만, 테스트 케이스에서 쿠폰 생성까지 약간의 텀이 존재하는 단점이 존재한다.
		 */
		Thread.sleep(10000);

		long count = couponRepository.count();
		// 요청이 완료되면 생성된 쿠폰의 갯수 확인
		assertThat(count).isEqualTo(1);

		/* 레이스 컨디션 발생 여부 고려
		레이스 컨디션이란?
		두 개 이상의 스레드가 공유 데이터에 access를 하고 동시에 작업을 하려고 할 때 발생되는 문제

		Synchronized 를 활용한 방법도 존재할 수도 있지만, 서버를 여러 대 사용한다면 해결방법으로 옳지 않다.
		Mysql, redis를 활용한 락을 구현하여 해결할 수도 있지만, 우리가 원하는 건 쿠폰 갯수에 대한 정합성이기에 락으로 해결한다면,
		발급된 쿠폰의 수를 가져오는 것부터 쿠폰을 생성하는 데까지 락을 걸어야 한다.

		* flushall: 레디스 데이터 초기화 명령어

		테스트 케이스가 성공한 이유
		- redis는 싱글 스레드 기반 동작하기에, 스레드 1에서 작업 완료전까지 wait 상태가 된다.
		- 그래서 모든 스레드에서는 언제나 최신값을 가져오기에 쿠폰 갯수에 대한 정합성을 해결할 수 있다.
		 */
	}
}