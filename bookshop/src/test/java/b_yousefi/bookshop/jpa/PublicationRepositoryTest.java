package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.jpa.creation.ModelFactory;
import b_yousefi.bookshop.models.Publication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/13/2020
 */
@DataJpaTest
public class PublicationRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PublicationRepository publicationRepository;
    private Publication publication;

    @AfterEach
    public void cleanup() {
        publicationRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        publication = ModelFactory.createPublication(entityManager);
    }

    @Test
    public void validate_notBlank_name() {
        Publication publication = Publication.builder()
                .build();
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        // execute validation, check for violations
        Set<ConstraintViolation<Publication>> violations =
                validator.validate(publication, Default.class);
        // do we have one?
        assertThat(violations.size()).isEqualTo(1);
        // Assert.assertEquals(1, violations.size());

        // now, check the constraint violations to check for our specific error
        ConstraintViolation<Publication> violation = violations.iterator().next();

        // contains the right message?
        assertThat("Publication name is required").isEqualTo(
                violation.getMessageTemplate());

        // from the right field?
        assertThat("name").isEqualTo(
                violation.getPropertyPath().toString());
    }

    @Test
    public void validate_website_w1() {
        // Setup the validator API in our unit test
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        Publication publication = Publication.builder()
                .name("Oxford")
                .website("oxford_com")
                .build();
        // execute validation, check for violations
        Set<ConstraintViolation<Publication>> violations =
                validator.validate(publication, Default.class);
        // do we have one?
        assertThat(violations.size()).isEqualTo(1);
        // Assert.assertEquals(1, violations.size());

        // now, check the constraint violations to check for our specific error
        ConstraintViolation<Publication> violation = violations.iterator().next();

        // contains the right message?
        assertThat("Wrong Pattern").isEqualTo(
                violation.getMessageTemplate());

        // from the right field?
        assertThat("website").isEqualTo(
                violation.getPropertyPath().toString());
    }

    @Test
    public void removeSuccessfully() {
        entityManager.remove(publication);
        assertThat(publicationRepository.findById(publication.getId())).isNotPresent();
    }

    @Test
    public void saveAndfindAllByNameContaining() {
        Pageable sortedByName =
                PageRequest.of(0, 3, Sort.by("name"));
        assertThat(publicationRepository.findAllByNameContainingIgnoreCase(publication.getName(), sortedByName
        )).hasSize(1);
    }
}
