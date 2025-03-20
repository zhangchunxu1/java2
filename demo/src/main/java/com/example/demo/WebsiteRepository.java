package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Integer> {
        // 自定义查询方法，查询 id 为 1 的记录
    @Query("SELECT w FROM Website w WHERE w.id = 1")
    Optional<Website> findWebsiteByIdOne();
}

