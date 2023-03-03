package com.supermarket.back.service;

import com.supermarket.back.entity.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderService {

    void saveProvider(Provider provider);

    Page<Provider> getProvider(String name, String type, Pageable pageable);

    boolean providerExist(Integer pid);

    boolean deleteById(Integer pid);

    boolean pidExist(Integer pid);
}
