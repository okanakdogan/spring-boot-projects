package akdogan.usercheck.common;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import akdogan.usercheck.user.User;
import akdogan.usercheck.user.UserRepository;
import akdogan.usercheck.user.UsernameBloomService;

@Component
public class StartupUserSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final UsernameBloomService usernameBloomService;
    private final Faker faker;

    public StartupUserSeeder(UserRepository userRepository, UsernameBloomService usernameBloomService) {
        this.userRepository = userRepository;
        this.usernameBloomService = usernameBloomService;
        this.faker = new Faker();
    }

    @Override
    @Transactional(readOnly = true)
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("🚀 Running user seed ...");

        // You can clean user db if you want to start fresh
        // userRepository.deleteAll();

        if(userRepository.count()==0){
            int count = 1_000_000;

            for (int i = 0; i < count; i++) {
                User user = new User();
                user.setUsername(faker.name().username());   
                user.setEmail(faker.internet().emailAddress());  
                user.setPassword(faker.internet().password());
                try {
                    userRepository.save(user);
                } catch (Exception e) {
                    i--;
                } 
            }
        }

        System.out.println("🚀 Starting seeding bloom filter");

        AtomicInteger counter = new AtomicInteger(0);
        userRepository.streamAll().map(User::getUsername).peek(username -> counter.incrementAndGet()).forEach(usernameBloomService::add);
        System.out.println("🚀 Completed seeding bloom filter with count of users:" + counter.get());
        
        System.out.println("🚀 Completed user seed. A user:" + userRepository.findTopByOrderByIdAsc() );
        System.out.println("🚀 Completed user seed. user count:" + userRepository.count() );

    }
}