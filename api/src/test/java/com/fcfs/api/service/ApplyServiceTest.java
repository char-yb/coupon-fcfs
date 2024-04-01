package com.fcfs.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
}