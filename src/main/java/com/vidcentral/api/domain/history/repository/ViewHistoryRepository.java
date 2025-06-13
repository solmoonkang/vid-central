package com.vidcentral.api.domain.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vidcentral.api.domain.history.entity.ViewHistory;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {
}
