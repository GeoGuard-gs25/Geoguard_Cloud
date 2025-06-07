package global.geoguard.specification;

import global.geoguard.model.Notification;
import global.geoguard.model.NotificationFilter;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class NotificationSpecification {

    public static Specification<Notification> withFilters(NotificationFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.title() != null && !filter.title().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.title().toLowerCase() + "%"));
            }

            if (filter.message() != null && !filter.message().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("message")), "%" + filter.message().toLowerCase() + "%"));
            }

            if( filter.lida() != null) {
                predicates.add(cb.equal(root.get("lida"), filter.lida()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
