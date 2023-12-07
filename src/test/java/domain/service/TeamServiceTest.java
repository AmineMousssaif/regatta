package domain.service;

import domain.TeamBuilder;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Team;
import be.ucll.ip.minor.groep124.model.TeamRepository;
import be.ucll.ip.minor.groep124.service.TeamService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamService teamService;

    @Test
    public void givenNoTeams_WhenValidTeamAdded_ThenTeamIsAddedAndTeamIsReturned() {
        // Given
        Team valid = TeamBuilder.aValidTeam().build();
        //mock
        when(teamRepository.findById(valid.getId())).thenReturn(Optional.empty());
        when(teamRepository.save(any())).thenReturn(valid);
        // When
        Team team = teamService.addTeam(valid);
        // Then
        assertThat(valid.getName()).isEqualTo(team.getName());
    }

    @Test
    public void givenNoTeams_WhenTeamIsUpdated_ThenThrowError() {
        // given
        Team valid = TeamBuilder.aValidTeam().build();
        //mock
        when(teamRepository.findById(valid.getId())).thenReturn(Optional.empty());
        //when
        final Throwable raisedException = Assertions.catchThrowable(() -> teamService.updateTeam1(valid.getId(), valid));
        //then
        Assertions.assertThat(raisedException).isInstanceOf(ServiceException.class).hasMessageContaining("no.team.with.this.id");
    }



}
