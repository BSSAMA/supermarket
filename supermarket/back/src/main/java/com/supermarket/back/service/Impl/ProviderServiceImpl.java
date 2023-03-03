package com.supermarket.back.service.Impl;

import com.supermarket.back.entity.BuyCommodity;
import com.supermarket.back.entity.IDClass.BuyCommodityID;
import com.supermarket.back.entity.Provider;
import com.supermarket.back.repository.ProviderRepository;
import com.supermarket.back.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProviderServiceImpl implements ProviderService {
    @Autowired
    ProviderRepository providerRepository;

    @Override
    public void saveProvider(Provider provider) {
        providerRepository.save(provider);
    }

    @Override
    public Page<Provider> getProvider(String name, String type, Pageable pageable) {
        return providerRepository.findProviderByNameLikeAndAddressLike('%'+name+'%','%'+type+'%',pageable);
    }

    @Override
    public boolean providerExist(Integer pid) {
        Provider provider = providerRepository.findProviderByPid(pid);
        return provider != null;
    }

    @Override
    public boolean deleteById(Integer pid) {
        providerRepository.deleteById(pid);
        return true;
    }

    @Override
    public boolean pidExist(Integer pid) {
        Provider provider = providerRepository.findProviderByPid(pid);
        return provider != null;
    }
}
