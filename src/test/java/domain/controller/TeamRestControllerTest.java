package domain.controller;

import be.ucll.ip.minor.groep124.Groep124Application;
import be.ucll.ip.minor.groep124.controller.TeamRESTController;
import be.ucll.ip.minor.groep124.model.Team;
import be.ucll.ip.minor.groep124.model.TeamDto;
import be.ucll.ip.minor.groep124.service.TeamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.TeamBuilder;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TeamRESTController.class,
        includeFilters = @ComponentScan.Filter(classes = EnableWebSecurity.class))
@ContextConfiguration(classes= {Groep124Application.class})
public class TeamRestControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    TeamService teamService;

    @Autowired
    MockMvc teamRestController;

    @Autowired
    private MockMvc mockMvc;

    Team team1, team2, updatedInvalidTeam1, teamWithInvalidId;

    @Before
    public void setUp() {
        team1 = TeamBuilder.team1().build();
        updatedInvalidTeam1 = TeamBuilder.team1().withTeamName(null).build();
        team2 = TeamBuilder.team2().build();
        teamWithInvalidId = TeamBuilder.aValidTeam().build();
    }

    @Test
    public void givenTeam_whenGetRequestToAllTeams_thenJSONWithAllTeamsReturned() throws Exception {
        // given
        List<Team> teams = Arrays.asList(team1, team2);

        // mocking
        given(teamService.findAll()).willReturn(teams);

        // when
        teamRestController.perform(get("/api/team/overview")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(team1.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(team2.getName())));
    }

    @Test
    public void givenNoTeams_whenPutRequestToUpdateAnInvalidTeam_thenErrorInJSONFormatIsReturned() throws Exception {
        //when
        teamRestController.perform(put("/api/team/update/{id}", team1.getId())
                        .content(mapper.writeValueAsString(updatedInvalidTeam1))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("name.missing")));
                //.andExpect(jsonPath("$.name", Is.is("name-character.too.few")));
    }

    @Test
    public void givenTeam_whenPostRequestToCreateTeam_thenTeamIsCreated() throws Exception {
        // given
        TeamDto teamDto = new TeamDto();
        teamDto.setName("TeamName1");
        teamDto.setCategory("categor");
        teamDto.setClub("Club1");
        teamDto.setPassengers(5);

        // mocking
        given(teamService.createTeam(BDDMockito.any(TeamDto.class))).willReturn(team1);

        // when
        mockMvc.perform(post("/api/team/add")
                        .content(mapper.writeValueAsString(teamDto))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Is.is(team1.getName())));

        verify(teamService, times(1)).createTeam(BDDMockito.any(TeamDto.class));
    }
    @Test
    public void givenTeamId_whenDeleteRequestToDeleteTeam_thenTeamIsDeleted() throws Exception {
        // given
        long teamId = 1;

        // when
        mockMvc.perform(delete("/api/team/delete/{id}", teamId))
                // then
                .andExpect(status().isOk());

        verify(teamService, times(1)).deleteTeam(teamId);
    }







}
