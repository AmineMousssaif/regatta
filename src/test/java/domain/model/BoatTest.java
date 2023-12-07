package domain.model;

import be.ucll.ip.minor.groep124.model.Boat;
import org.junit.Test;
import domain.BoatBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoatTest {

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
    public void givenValidBoat_should_be_valid() {
        Boat valid = BoatBuilder.aValidBoat().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(valid);

        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenBoatWithNullName_should_be_invalid() {
        Boat invalid = BoatBuilder.anInvalidBoatWithNoName().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(invalid);

        assertEquals(2, violations.size());
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void givenBoatWithNullEmail_should_be_invalid() {
        Boat invalid = BoatBuilder.anInvalidBoatWithNoEmail().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(invalid);

        assertEquals(2, violations.size());
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("email.missing", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void givenBoatWithNoInsuranceNumber_should_be_invalid() {
        Boat invalid = BoatBuilder.anInvalidBoatWithNoInsurance().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(invalid);

        assertEquals(2, violations.size());
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("insurance.missing", violation.getMessage());
        assertEquals("insurance", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    @Test
    public void givenBoatWithNoLength_should_be_invalid() {
        Boat invalid = BoatBuilder.anInvalidBoatWithNoLength().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(invalid);

        assertEquals(1, violations.size());
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("length.too.small", violation.getMessage());
        assertEquals("length", violation.getPropertyPath().toString());
        assertEquals(0, violation.getInvalidValue());
    }

    @Test
    public void givenBoatWithNoWidth_should_be_invalid() {
        Boat invalid = BoatBuilder.anInvalidBoatWithNoWidth().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(invalid);

        assertEquals(1, violations.size());
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("width.too.small", violation.getMessage());
        assertEquals("width", violation.getPropertyPath().toString());
        assertEquals(0, violation.getInvalidValue());
    }

    @Test
    public void givenBoatWithNoHeight_should_be_invalid() {
        Boat invalid = BoatBuilder.anInvalidBoatWithNoHeight().build();

        Set<ConstraintViolation<Boat>> violations = validator.validate(invalid);

        assertEquals(1, violations.size());
        ConstraintViolation<Boat> violation = violations.iterator().next();
        assertEquals("height.too.small", violation.getMessage());
        assertEquals("height", violation.getPropertyPath().toString());
        assertEquals(0, violation.getInvalidValue());
    }
}
