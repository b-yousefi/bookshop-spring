package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Publication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi Date: 5/13/2020
 */
public class PublicationRepositoryTest extends DataTest {
        private Publication publication;

        @BeforeEach
        public void setUp() {
                publication = createPublication();
        }

        @Test
        public void validate_notBlank_name() {
                Publication publication = Publication.builder().build();
                LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();
                // execute validation, check for violations
                Set<ConstraintViolation<Publication>> violations = validator.validate(publication, Default.class);
                // do we have one?
                assertThat(violations.size()).isEqualTo(1);
                // Assert.assertEquals(1, violations.size());

                // now, check the constraint violations to check for our specific error
                ConstraintViolation<Publication> violation = violations.iterator().next();

                // contains the right message?
                assertThat(violation.getMessageTemplate()).isEqualTo("Publication name is required");

                // from the right field?
                String field = violation.getPropertyPath().toString();
                assertThat(field).isEqualTo("name");
                validator.close();
        }

        @Test
        public void validate_website_w1() {
                // Setup the validator API in our unit test
                LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();
                Publication publication = Publication.builder().name("Oxford").website("oxford_com").build();
                // execute validation, check for violations
                Set<ConstraintViolation<Publication>> violations = validator.validate(publication, Default.class);
                // do we have one?
                assertThat(violations.size()).isEqualTo(1);
                // Assert.assertEquals(1, violations.size());

                // now, check the constraint violations to check for our specific error
                ConstraintViolation<Publication> violation = violations.iterator().next();

                // contains the right message?
                assertThat(violation.getMessageTemplate()).isEqualTo("Wrong Pattern");

                // from the right field?
                String field = violation.getPropertyPath().toString();
                assertThat(field).isEqualTo("website");
                validator.close();
        }

        @Test
        public void removeSuccessfully() {
                getEntityManager().remove(publication);
                assertThat(getPublicationRepository().findById(publication.getId())).isNotPresent();
        }

        @Test
        public void saveAndfindAllByNameContaining() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertThat(getPublicationRepository().findAllByNameContainingIgnoreCase(publication.getName(),
                                sortedByName)).hasSize(1);
        }
}
