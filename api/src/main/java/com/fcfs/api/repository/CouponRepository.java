package com.fcfs.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcfs.api.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
