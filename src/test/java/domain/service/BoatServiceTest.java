package domain.service;

import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Boat;
import be.ucll.ip.minor.groep124.model.BoatRepository;
import be.ucll.ip.minor.groep124.service.BoatService;
import domain.BoatBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BoatServiceTest {

    @Mock
    BoatRepository boatRepository;

    @InjectMocks
    BoatService boatService;

    Boat boat1, boat2;

    @Before
    public void setUp(){
        boat1 = BoatBuilder.boat1().build();
        boat2 = BoatBuilder.boat2().build();
    }

    @Test
    public void givenNoBoats_WhenValidBoatAdded_ThenBoatIsAddedAndBoatIsReturned() {
        // Given
        Boat valid = BoatBuilder.aValidBoat().build();
        //mock
        when(boatRepository.findById(valid.getId())).thenReturn(Optional.empty());
        when(boatRepository.save(any())).thenReturn(valid);
        when(boatRepository.save(any())).thenReturn(valid);
        // When
        Boat boat = boatService.addBoat(valid);
        // Then
        Assertions.assertThat(valid.getName()).isEqualTo(boat.getName());
    }

    @Test
    public void givenNoBoats_WhenBoatIsUpdated_ThenThrowError() {
        // given
        Boat valid = BoatBuilder.aValidBoat().build();
        //mock
        when(boatRepository.findById(valid.getId())).thenReturn(Optional.empty());
        //when
        final Throwable raisedException = Assertions.catchThrowable(() -> boatService.updateBoat1(valid.getId(), valid));
        //then
        Assertions.assertThat(raisedException).isInstanceOf(ServiceException.class).hasMessageContaining("no.boat.with.this.id");
    }

}