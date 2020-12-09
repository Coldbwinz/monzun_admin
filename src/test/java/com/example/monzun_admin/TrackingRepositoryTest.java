package com.example.monzun_admin;

import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.repository.TrackingRepository;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestContextManager;


@RunWith(DataProviderRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class TrackingRepositoryTest extends AbstractTestCase {
    @Autowired
    private TrackingRepository trackingRepository;

    @Before
    public void setUpContext() throws Exception {
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);
    }

    @DataProvider
    public static Object[][] randomTrackingNamesWhenUpdating() {
        return new Object[][]{
                {faker.name().name(), faker.name().name()},
        };
    }

    @Test
    public void create() {
        Tracking tracking = createTestTrackingEntity();
        trackingRepository.saveAndFlush(tracking);
        Assertions.assertNotNull(tracking.getId());
        Assertions.assertTrue(trackingRepository.existsById(tracking.getId()));
    }

    @Test
    public void createDuplicate() {
        String name = faker.name().title();
        Tracking tracking = createTestTrackingEntity();
        Tracking duplicateTracking = createTestTrackingEntity();
        tracking.setName(name);
        duplicateTracking.setName(name);
        trackingRepository.save(tracking);
        Assertions.assertTrue(trackingRepository.existsById(tracking.getId()));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> trackingRepository.saveAndFlush(duplicateTracking));
    }

    @Test()
    @UseDataProvider("randomTrackingNamesWhenUpdating")
    public void update(String nameOne, String nameTwo) {
        Tracking tracking = createTestTrackingEntity();
        tracking.setName(nameOne);
        trackingRepository.save(tracking);
        Assertions.assertNotNull(trackingRepository.findByName(nameOne));
        tracking.setName(nameTwo);
        trackingRepository.saveAndFlush(tracking);
        Assertions.assertNotNull(trackingRepository.findByName(nameTwo));
    }

    @Test
    public void delete() {
        Tracking tracking = createTestTrackingEntity();
        trackingRepository.save(tracking);
        Assertions.assertNotNull(tracking.getId());
        Assertions.assertTrue(trackingRepository.existsById(tracking.getId()));
        trackingRepository.deleteById(tracking.getId());
        Assertions.assertFalse(trackingRepository.existsById(tracking.getId()));
    }

    private Tracking createTestTrackingEntity() {
        Tracking tracking = new Tracking();
        tracking.setName(faker.name().title());
        tracking.setActive(faker.bool().bool());
        tracking.setDescription(faker.team().sport());
        tracking.setStartedAt(faker.date().birthday());
        tracking.setEndedAt(faker.date().birthday(1, 2));
        return tracking;
    }
}
