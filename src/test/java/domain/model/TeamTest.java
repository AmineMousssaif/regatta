package domain.model;

import be.ucll.ip.minor.groep124.model.Team;
import domain.TeamBuilder;
import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidTeam_should_be_valid() {
        Team valid = TeamBuilder.aValidTeam().build();

        Set<ConstraintViolation<Team>> violations = validator.validate(valid);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenTeamWithNullName_should_be_invalid() {
        Team invalid = TeamBuilder.anInvalidTeamWithNoName().build();

        Set<ConstraintViolation<Team>> violations = validator.validate(invalid);

        assertEquals(1, violations.size());
        ConstraintViolation<Team> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void givenTeamWithNullCategory_should_be_invalid() {
        Team invalid = TeamBuilder.anInvalidTeamWithNoCategory().build();

        Set<ConstraintViolation<Team>> violations = validator.validate(invalid);

        assertEquals(1, violations.size());
        ConstraintViolation<Team> violation = violations.iterator().next();
        assertEquals("category.missing", violation.getMessage());
        assertEquals("category", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void givenTeamWithNoClub_should_be_valid() {
        Team valid = TeamBuilder.aValidTeamWithNoClub().build();

        Set<ConstraintViolation<Team>> violations = validator.validate(valid);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenTeamWithNoTeamMembers_should_be_invalid() {
        Team invalid = TeamBuilder.anInvalidTeamWithNoTeamMembers().build();

        Set<ConstraintViolation<Team>> violations = validator.validate(invalid);

        assertEquals(1, violations.size());
        ConstraintViolation<Team> violation = violations.iterator().next();
        assertEquals("too-few-passengers", violation.getMessage());
        assertEquals("passengers", violation.getPropertyPath().toString());
        assertEquals(0, violation.getInvalidValue());
    }

    @Test
    public void givenTeamWithSpaceName_should_be_invalid() {
        Team invalid = TeamBuilder.anInvalidTeamWithSpaceName().build();

        Set<ConstraintViolation<Team>> violations = validator.validate(invalid);

        assertEquals(2, violations.size());
        ConstraintViolation<Team> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        //assertEquals("name-characters.too.few", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals(" ", violation.getInvalidValue());
    }
}
