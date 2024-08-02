package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.service.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final Integer EXPIRE_MINS = 2;

    private final CacheManager cacheManager;

    @Override
    public int generateOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);

        Cache cache = cacheManager.getCache("otpCache");
        if (cache != null) {
            cache.put(key, otp);
        }

        // Schedule to remove the OTP after EXPIRE_MINS
        scheduleCacheEviction(key);

        return otp;
    }

    @Override
    public int getOtp(String key) {
        Cache cache = cacheManager.getCache("otpCache");
        if (cache != null) {
            Integer otp = cache.get(key, Integer.class);
            return otp != null ? otp : 0;
        }
        return 0;
    }

    @Override
    public void clearOTP(String key) {
        Cache cache = cacheManager.getCache("otpCache");
        if (cache != null) {
            cache.evict(key);
        }
    }

    private void scheduleCacheEviction(String key) {
        new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(EXPIRE_MINS);
                clearOTP(key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
