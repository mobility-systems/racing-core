package com.theodore.racingcore.repositories;

import com.theodore.racingcore.entities.cars.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    //testing slow db query for logs
    @Query(value = """
    with delay as (select pg_sleep(2))
    select cm.*
    from car_model cm, delay
    where cm.id = :id
    """, nativeQuery = true)
    CarModel findSlowById(@Param("id") Long id);
}
