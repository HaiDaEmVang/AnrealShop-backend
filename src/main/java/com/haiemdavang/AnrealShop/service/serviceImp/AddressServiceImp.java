package com.haiemdavang.AnrealShop.service.serviceImp;

import com.haiemdavang.AnrealShop.dto.address.AddressDto;
import com.haiemdavang.AnrealShop.mapper.AddressMapper;
import com.haiemdavang.AnrealShop.redis.service.IRedisService;
import com.haiemdavang.AnrealShop.repository.UserAddressRepository;
import com.haiemdavang.AnrealShop.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImp implements IAddressService {
    private final UserAddressRepository userAddressRepository;
    private final IRedisService redisService;
    private final AddressMapper mapper;
    private final String PREFIX_ADDRESS = "user:%s:address:";


    @Override
    public AddressDto findAddressPrimaryByUserId(String userId) {
        return mapper.toBaseAddressDto(userAddressRepository.findByUserIdAndPrimaryAddressTrue(userId));
    }

    @Override
    public List<AddressDto> findAllByUserId(String userId) {
        return userAddressRepository.findAllByUserId(userId).stream().map(mapper::toBaseAddressDto).toList();
    }
}
