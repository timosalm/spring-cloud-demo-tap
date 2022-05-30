package com.example.orderservice.order;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Cacheable("Orders")
    @Override
    List<Order> findAll();

    @Cacheable("Order")
    @Override
    Optional<Order> findById(Long id);

    @CacheEvict(cacheNames = {"Order", "Orders"}, allEntries = true)
    @Override
    <S extends Order> S save(S order);
}