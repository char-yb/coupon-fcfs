package com.fcfs.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcfs.consumer.domain.FailedEvent;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {
}
