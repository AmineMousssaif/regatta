import domain.controller.BoatRestControllerTest;
import domain.controller.TeamRestControllerTest;
import domain.model.TeamTest;
import domain.model.BoatTest;
import domain.service.BoatServiceTest;
import domain.service.TeamServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({BoatTest.class, TeamTest.class, BoatServiceTest.class, TeamServiceTest.class, BoatRestControllerTest.class, TeamRestControllerTest.class})
public class AllTests {

}