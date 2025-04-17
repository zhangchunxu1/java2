package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Integer> {
    Page<Website> findByNameContaining(String name, Pageable pageable);
    Page<Website> findByCountryContaining(String country, Pageable pageable);
    Page<Website> findByNameContainingAndCountryContaining(String name, String country, Pageable pageable);
    Page<Website> findByAlexa(Integer alexa, Pageable pageable);
    Page<Website> findByNameContainingAndAlexa(String name, Integer alexa, Pageable pageable);
    Page<Website> findByCountryContainingAndAlexa(String country, Integer alexa, Pageable pageable);
    Page<Website> findByNameContainingAndCountryContainingAndAlexa(String name, String country, Integer alexa, Pageable pageable);
        // 自定义查询方法，查询 id 为 1 的记录
    @Query("SELECT w FROM Website w WHERE w.id = 1")
    Optional<Website> findWebsiteByIdOne();
}

