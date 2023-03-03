package com.supermarket.back.repository;

import com.supermarket.back.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider,Integer> {
    Provider findProviderByPid(Integer pid);
    Page<Provider> findProviderByNameLikeAndAddressLike(String name, String address, Pageable pageable);
}
