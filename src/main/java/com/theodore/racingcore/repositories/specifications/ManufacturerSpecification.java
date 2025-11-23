package com.theodore.racingcore.repositories.specifications;

import com.theodore.racingcore.entities.cars.Manufacturer;
import com.theodore.racingcore.models.automobiles.manufacturers.requests.SearchManufacturersRequest;
import com.theodore.racingmodel.enums.Country;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ManufacturerSpecification {

    private ManufacturerSpecification() {
    }

    public static Specification<Manufacturer> filterCriteria(SearchManufacturersRequest searchRequest) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(searchRequest.namePart())) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")), "%" + searchRequest.namePart().toLowerCase() + "%"
                        )
                );
            }
            if (searchRequest.country() != null) {
                List<Country> countries = Country.startingWith(searchRequest.country());
                if (!countries.isEmpty()) {
                    predicates.add(root.get("country").in(countries));
                }
            }
            if (criteriaQuery != null) {
                criteriaQuery.distinct(true);
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
