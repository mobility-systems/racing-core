package com.theodore.racingcore.integration;

import com.theodore.racingcore.entities.racing.Driver;
import com.theodore.racingcore.repositories.DriverRepository;
import com.theodore.racingcore.utils.RacingCoreTestUtils;
import com.theodore.racingcore.utils.TestData;

import java.util.List;

public class TestDataFeeder {

    private final DriverRepository driverRepository;

    public TestDataFeeder(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void feedDriverTable() {
        List<Driver> driversList = List.of(
                createDriver(TestData.EXISTING_ALIAS_1, 1),
                createDriver(TestData.EXISTING_ALIAS_2, 2)
        );
        driverRepository.saveAll(driversList);
    }

    public void cleanDriverTable() {
        driverRepository.deleteAll();
    }

    private Driver createDriver(String alias, Integer trophies) {
        String id = RacingCoreTestUtils.generateUlId();
        return new Driver(id, trophies, alias);
    }

}
