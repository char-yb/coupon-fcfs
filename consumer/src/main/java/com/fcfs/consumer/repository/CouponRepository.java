package com.fcfs.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcfs.consumer.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
