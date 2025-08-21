package akdogan.usercheck.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import akdogan.usercheck.user.UserRepository;
import akdogan.usercheck.user.UsernameBloomService;
import akdogan.usercheck.user.UsernameRedisCacheService;

@Service
public class AuthService {
    
    private UserRepository userRepository;
    private UsernameBloomService usernameBloomService;
    private UsernameRedisCacheService usernameRedisCacheService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(
        UserRepository userRepository,
        UsernameBloomService usernameBloomService,
        UsernameRedisCacheService usernameRedisCacheService
    ){
        this.userRepository = userRepository;
        this.usernameBloomService = usernameBloomService;
        this.usernameRedisCacheService = usernameRedisCacheService;
    }

    

    public boolean checkUsernameOnDB(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailOnDB(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean checkUsername(String username){

        long start, end;

        start = System.nanoTime();
        boolean mightBePresent = usernameBloomService.mightContain(username);
        end = System.nanoTime();
        logger.info("BloomFilter says {} - took {} ms", mightBePresent, (end - start) / 1_000_000);
        if (!mightBePresent){
            return false;
        }

        start = System.nanoTime();
        boolean inCache = usernameRedisCacheService.checkKeyExists(username);
        end = System.nanoTime();
        logger.info("Cache says {} - took {} ms", inCache, (end - start) / 1_000_000);
        if (inCache){
            usernameBloomService.add(username);
            return true;
        }

        start = System.nanoTime();
        boolean exists = userRepository.existsByUsername(username);
        end = System.nanoTime();
        logger.info("DB says {} - took {} ms", exists, (end - start) / 1_000_000);
        if (exists) {
            usernameBloomService.add(username);
            usernameRedisCacheService.cacheValue(username);
        }

        return exists;
    }


}
