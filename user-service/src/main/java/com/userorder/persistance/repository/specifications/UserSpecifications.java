package com.userorder.persistance.repository.specifications;

import com.userorder.persistance.model.User;
import com.userorder.persistance.model.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserSpecifications {

    public static Specification<User> hasUsername(String username) {
        return (root, query, cb) -> {
            if (username == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("username"), username);
        };
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null) {
                return cb.conjunction();
            }
            return cb.equal(root.join("contacts").get("email"), email);
        };
    }

    public static Specification<User> hasStatus(UserStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<User> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("createdDate"), date);
        };
    }

    // More specifications as needed
}

// Similar classes for ContactSpecifications, AddressSpecifications, etc.