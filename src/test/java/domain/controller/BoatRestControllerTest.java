package domain.controller;

import be.ucll.ip.minor.groep124.Groep124Application;
import be.ucll.ip.minor.groep124.controller.BoatRESTController;
import be.ucll.ip.minor.groep124.model.Boat;
import be.ucll.ip.minor.groep124.model.BoatDto;
import be.ucll.ip.minor.groep124.model.TeamDto;
import be.ucll.ip.minor.groep124.service.BoatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.BoatBuilder;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Alternatively but launches whole application as mock:
/*@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Groep124Application.class)

*/
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BoatRESTController.class,
        includeFilters = @ComponentScan.Filter(classes = EnableWebSecurity.class))
@ContextConfiguration(classes= {Groep124Application.class})
public class BoatRestControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    BoatService boatService;

    @Autowired
    MockMvc boatRestController;

    Boat boat1, boat2, invalidBoat;

    @Before
    public void setUp(){
        boat1 = BoatBuilder.boat1().build();
        boat2 = BoatBuilder.boat2().build();
        invalidBoat = BoatBuilder.anInvalidBoatWithNoName().build();
    }

    @Test
    public void givenBoat_whenGetRequestToAllBoats_thenJSONWithAllBoatsReturned() throws Exception {
        //given
        List<Boat> boats = Arrays.asList(boat1, boat2);

        //mocking
        given(boatService.findAll()).willReturn(boats);

        //when
        boatRestController.perform(get("/api/boat/overview")
                            .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(boat1.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(boat2.getName())));
    }

    @Test
    public void givenNoBoats_whenPostRequestToAddAnInvalidBoat_thenErrorInJSONFormatIsReturned() throws Exception {
        //when
        boatRestController.perform(post("/api/boat/add")
                    .content(mapper.writeValueAsString(invalidBoat))
                    .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("name.missing")));
    }

    @Test
    public void givenBoatId_whenDeleteRequestToDeleteBoat_thenBoatIsDeleted() throws Exception {
        // given
        long boatId = 1;

        // when
        boatRestController.perform(delete("/api/boat/delete?id=1", boatId))
                // then
                .andExpect(status().isOk());

        verify(boatService, times(1)).deleteBoat(boatId);
    }

    @Test
    public void givenBoat_whenPostRequestToCreateBoat_thenBoatIsCreated() throws Exception {
        // given
        BoatDto boatDto = new BoatDto();
        boatDto.setName("BoatName1");
        boatDto.setEmail("email1@email.com");
        boatDto.setHeight(2);
        boatDto.setLength(2);
        boatDto.setWidth(2);
        boatDto.setInsurance("123456ABCD");

        // mocking
        given(boatService.createBoat(BDDMockito.any(BoatDto.class))).willReturn(boat1);

        // when
        boatRestController.perform(post("/api/boat/add")
                        .content(mapper.writeValueAsString(boatDto))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Is.is(boat1.getName())));

        verify(boatService, times(1)).createBoat(BDDMockito.any(BoatDto.class));
    }
}
