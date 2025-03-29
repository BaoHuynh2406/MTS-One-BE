package com.mts.mtsone.modules.auth.security;

import com.mts.mtsone.modules.auth.entity.BlacklistedToken;
import com.mts.mtsone.modules.auth.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public void blacklistToken(String token, Long expirationTime) {
        if (expirationTime < System.currentTimeMillis()) {
            return; // Token đã hết hạn, không thêm vào blacklist
        }
        //Kiễm tra xem nó có trong blacklist chưa?
        if(isTokenBlacklisted(token)){
            return; // Token đã có trong blacklist, không thêm vào blacklist nữa
        }
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpirationDate(new Date(expirationTime));
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.findByToken(token).isPresent();
    }

    @Scheduled(cron = "0 0 * * * ?") // Chạy mỗi giờ
    public void removeExpiredTokens() {
        blacklistedTokenRepository.deleteByExpirationDateBefore(new Date());
    }
} 