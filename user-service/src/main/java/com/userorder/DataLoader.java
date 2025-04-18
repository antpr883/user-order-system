package com.userorder;


import com.userorder.persistence.model.*;
import com.userorder.persistence.repository.ContactRepository;
import com.userorder.persistence.repository.UserRepository;
import com.userorder.service.AddressService;
import com.userorder.service.ContactService;
import com.userorder.service.UserService;
import com.userorder.service.dto.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class DataLoader implements ApplicationRunner {


    private final ContactService contactService;
    private final AddressService addressService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Sample data arrays
    private static final String[] FIRST_NAMES = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "Robert", "Maria", "Daniel", "Olivia"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
    private static final String[] STREETS = {"123 Main St", "456 Elm Ave", "789 Oak Blvd", "101 Pine Lane", "202 Maple Dr", 
                                           "303 Cedar Ct", "404 Birch Rd", "505 Willow Way", "606 Spruce St", "707 Cherry Ave"};
    private static final String[] CITIES = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", 
                                          "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
    private static final String[] PROVINCES = {"NY", "CA", "IL", "TX", "AZ", "PA", "TX", "CA", "TX", "CA"};
    private static final String[] COUNTRIES = {"USA", "USA", "USA", "USA", "USA", "USA", "USA", "USA", "USA", "USA"};
    private static final String[] ZIP_CODES = {"10001", "90001", "60601", "77001", "85001", "19101", "78201", "92101", "75201", "95101"};
    private static final String[] PHONE_NUMBERS = {"212-555-1234", "213-555-2345", "312-555-3456", "713-555-4567", "602-555-5678", 
                                                 "215-555-6789", "210-555-7890", "619-555-8901", "214-555-9012", "408-555-0123"};

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data loading...");
        
        // Only load data if the database is empty
        if (isDatabaseEmpty()) {
            try {
                // Create 10 people with all related data
                for (int i = 0; i < 10; i++) {
                    User user = createUser(i);
                    userRepository.save(user);
                    log.info("Created user: {} {}", user.getFirstName(), user.getLastName());
                }
                log.info("Data loading completed successfully.");
                
                // Update metrics after data loading

            } catch (DataIntegrityViolationException e) {
                log.warn("Data already exists in the database. Skipping data loading. Error: {}", e.getMessage());
            }
        }
        
        try {
            // Verify data was loaded properly by retrieving one user with all details
            Set<String> attributes = new HashSet<>(List.of("contacts","addresses"));
            UserDTO dto = userService.findById(1L, true, attributes);
            String a = "";
        } catch (Exception e) {
            log.warn("Could not verify data: {}", e.getMessage());
        }
    }
    
    /**
     * Check if the database is empty before inserting data
     * @return true if the database is empty
     */
    private boolean isDatabaseEmpty() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM User p", Long.class);
        Long count = query.getSingleResult();
        return count == 0;
    }
    
    /**
     * Check if an email already exists in the database
     * @param email the email to check
     * @return true if the email already exists
     */
    private boolean emailExists(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Contact c WHERE c.email = :email", Long.class);
        query.setParameter("email", email);
        Long count = query.getSingleResult();
        return count > 0;
    }
    
    /**
     * Check if a phone number already exists in the database
     * @param phoneNumber the phone number to check
     * @return true if the phone number already exists
     */
    private boolean phoneNumberExists(String phoneNumber) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(c) FROM Contact c WHERE c.phoneNumber = :phoneNumber", Long.class);
        query.setParameter("phoneNumber", phoneNumber);
        Long count = query.getSingleResult();
        return count > 0;
    }

    public User createUser(int index) {
        // Create the user with basic info
        User user = User.builder()
                .password("password" + (index + 1))
                .firstName(FIRST_NAMES[index])
                .lastName(LAST_NAMES[index])
                .birthDay(LocalDate.of(1980 + index, (index % 12) + 1, (index % 28) + 1))
                .build();

        // Create multiple addresses for each user (1-3 addresses)
        int numAddresses = (index % 3) + 1;
        for (int i = 0; i < numAddresses; i++) {
            Address address = createAddress(user, index, i);
            user.addAddress(address);
        }

        // Create multiple contacts for each user (1-2 contacts)
        int numContacts = (index % 2) + 1;
        for (int i = 0; i < numContacts; i++) {
            Contact contact = createContact(user, index, i);
            if (contact != null) {
                user.addContact(contact);
            }
        }
        return user;
    }

    public Address createAddress(User user, int userIndex, int addressIndex) {
        // Determine address type based on index - only LOCAL and INTERNATIONAL are available
        AddressType type = addressIndex == 0 ? AddressType.LOCAL : AddressType.INTERNATIONAL;
        
        // Create address with data from our sample arrays, with some variations
        Address address = Address.builder()
                .type(type)
                .street(STREETS[userIndex] + (addressIndex > 0 ? ", Suite " + (addressIndex * 100) : ""))
                .postZipCode(ZIP_CODES[userIndex])
                .city(CITIES[userIndex])
                .province(PROVINCES[userIndex])
                .country(COUNTRIES[userIndex])
                .build();

        address.setUser(user);
        return address;
    }

    public Contact createContact(User user, int userIndex, int contactIndex) {
        // Determine contact type based on index
        ContactType type = contactIndex == 0 ? ContactType.PERSONAL : ContactType.WORK;
        
        // Format email based on user name and contact type
        String email = FIRST_NAMES[userIndex].toLowerCase() + "." +
                      LAST_NAMES[userIndex].toLowerCase() +
                      (contactIndex > 0 ? ".work" : "") + 
                      "@example.com";
        
        // Check if this email already exists in the database
        if (emailExists(email)) {
            log.warn("Email {} already exists, skipping this contact", email);
            return null;
        }
        
        // Generate phone number with variation
        String phoneNumber = PHONE_NUMBERS[userIndex] + (contactIndex > 0 ? "0" : "");
        
        // Check if this phone number already exists in the database
        if (phoneNumberExists(phoneNumber)) {
            log.warn("Phone number {} already exists, skipping this contact", phoneNumber);
            return null;
        }
        
        // Create contact with appropriate data
        Contact contact = Contact.builder()
                .contactType(type)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();

        contact.setUser(user);
        return contact;
    }

}
