package com.example.monzun_admin;

import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.repository.UserRepository;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@AutoConfigureTestDatabase(replace = NONE)
@RunWith(DataProviderRunner.class)
@ContextConfiguration
@DataJpaTest

public class UserRepositoryTest extends AbstractTestCase {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUpContext() throws Exception {
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }

    @DataProvider
    public static Object[][] randomEmailsWhenUpdating() {
        return new Object[][]{
                {faker.bothify("???????#@mail.ru"), faker.bothify("???????#@mail.ru")},
        };
    }

    @Test
    public void create() {
        User user = createTestUserEntity(faker.bothify("???????#@mail.ru"));
        userRepository.save(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertTrue(userRepository.existsById(user.getId()));
    }

    @Test
    public void createDuplicate() {
        String randomEmail = faker.bothify("???????#@mail.ru");
        User user = createTestUserEntity(randomEmail);
        User duplicateUser = createTestUserEntity(randomEmail);
        userRepository.save(user);
        Assertions.assertTrue(userRepository.existsById(user.getId()));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(duplicateUser));
    }

    @ParameterizedTest
    @MethodSource("randomEmailsWhenUpdating")
    public void update(String firstEmail, String secondEmail) {
        User user = createTestUserEntity(firstEmail);
        userRepository.save(user);
        Assertions.assertNotNull(userRepository.findByEmail(firstEmail));
        user.setEmail(secondEmail);
        userRepository.save(user);
        Assertions.assertNotNull(userRepository.findByEmail(secondEmail));
    }

    @Test
    public void delete() {
        User user = createTestUserEntity(faker.bothify("???????#@mail.ru"));
        userRepository.save(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertTrue(userRepository.existsById(user.getId()));
        userRepository.deleteById(user.getId());
        Assertions.assertFalse(userRepository.existsById(user.getId()));
    }

    private User createTestUserEntity(String email) {
        User user = new User();
        user.setEmail(email);
        user.setName(faker.name().lastName());
        user.setPassword(faker.phoneNumber().phoneNumber());
        user.setRole(RoleEnum.TRACKER.getRole());
        return user;
    }
}
