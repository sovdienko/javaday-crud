package com.example;

import com.example.domain.Ad;
import com.example.domain.AdRepository;
import com.example.domain.User;
import com.example.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by sovd on 04.06.2016.
 */

@Configuration
public class AppConfig {

    private static final Integer[] MOBILE_OPERATOR_CODES = new Integer[]{
            39,
            50,
            63,
            66,
            67,
            68,
            93,
            95,
            96,
            97,
            98,
            99
    };

    private static final String CITY = "Киев";

    private static final String[] DISTRICTS = new String[]{
            "Голосеево",
            "Дарница",
            "Деснянский",
            "Днепровский",
            "Оболонь",
            "Печерск",
            "Подол",
            "Святошино",
            "Соломенский",
            "Шевченковский"
    };

    private static final String[] COMMENTS = new String[]{
            "",
            "всю суму",
            "ну дуже треба",
            "можна частинами",
            "маленький, можу під'їхати"
    };

    private static final int PUBLISHING_TIME_MAX_DIFF = 4;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    private static User nextUser() {
        User user = new User();
        user.setPhoneNumber(nextPhoneNumber());
        return user;
    }

    private static String nextPhoneNumber() {
        return String.format("0%d%07d", nextMobileOperatorCode(), nextInt(10000000));
    }

    private static int nextMobileOperatorCode() {
        return nextRandomFromArray(MOBILE_OPERATOR_CODES);
    }


    private static <T> T nextRandomFromArray(T[] array) {
        return array[nextInt(array.length)];
    }

    private static int nextInt(int bound) {
        return new Random().nextInt(bound);
    }


    private static Ad nextAd(User user, LocalDateTime publishedAt) {
        Ad ad = new Ad();

        Ad.Type type = nextType();
        ad.setType(type);
        Ad.Currency currency = nextCurrency();
        ad.setCurrency(currency);

        ad.setAmount(nextAmount());
        ad.setRate(nextRate(currency, type));
        ad.setUser(user);

        ad.setLocation(new Ad.Location(CITY, nextDistrict()));
        ad.setComment(nextComments());
        ad.setPublishedAt(nextPublishingTime(publishedAt));
        ad.setStatus(Ad.Status.PUBLISHED);
        return ad;
    }

    private static Ad.Type nextType() {
        return nextRandomFromArray(Ad.Type.values());
    }

    private static BigInteger nextAmount() {
        return BigInteger.valueOf(nextInt(100) * 100 + 100);
    }

    private static Ad.Currency nextCurrency() {
        return nextRandomFromArray(Ad.Currency.values());
    }

    private static BigDecimal nextRate(Ad.Currency currency, Ad.Type type) {
        return avgRate(currency, type);
    }

    private static BigDecimal avgRate(Ad.Currency currency, Ad.Type type) {
        return (currency == Ad.Currency.USD ?
                BigDecimal.valueOf(type == Ad.Type.BUY ? 22.11 : 22.55) :
                BigDecimal.valueOf(type == Ad.Type.BUY ? 25.32 : 25.71)
        );
    }

    private static String nextDistrict() {
        return nextRandomFromArray(DISTRICTS);
    }

    private static LocalDateTime nextPublishingTime(LocalDateTime previous) {
        return previous.plusMinutes(nextInt(PUBLISHING_TIME_MAX_DIFF - 1) + 1);
    }

    private static String nextComments() {
        return nextRandomFromArray(COMMENTS);
    }

    private void setupAdmin(LocalDateTime publishedAt) {
        User admin = new User();
        admin.setPhoneNumber("hontareva");
        userRepository.save(admin);
        Ad ad = new Ad();
        ad.setType(Ad.Type.BUY);
        ad.setAmount(BigInteger.valueOf(100000000));
        ad.setCurrency(Ad.Currency.USD);
        ad.setRate(nextRate(ad.getCurrency(), ad.getType()));
        ad.setUser(admin);
        ad.setStatus(Ad.Status.PUBLISHED);
        ad.setPublishedAt(publishedAt);
        ad.setLocation(new Ad.Location("Киев", "Печерск"));
        ad.setComment("играем по крупному");
        adRepository.save(ad);
    }

    public void load() {
        int amount = 100;
        LocalDateTime now = LocalDateTime.now();
        final LocalDateTime publishedAt = now.minusMinutes(PUBLISHING_TIME_MAX_DIFF * 100);

        LocalDateTime at = publishedAt;
        for (int i = 0; i < amount; ++i) {
            User user = nextUser();
            userRepository.save(user);

            Ad ad = nextAd(user, at);
            adRepository.save(ad);

            at = ad.getPublishedAt();
        }
        setupAdmin(publishedAt.minusMinutes(10));
    }



    @Bean
    CommandLineRunner commandLineRunner(AppConfig dataLoader) {
        return (o) -> dataLoader.load();
    }
}
