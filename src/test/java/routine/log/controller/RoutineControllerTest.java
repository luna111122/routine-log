package routine.log.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import routine.log.domain.Routine;
import routine.log.domain.User;

import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.dto.routine.RoutineDeleteRequestDto;
import routine.log.exception.routine.RoutineNotFoundException;
import routine.log.repository.RoutineRepository;
import routine.log.repository.UserRepository;
import routine.log.service.RoutineService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoutineControllerTest {

    @InjectMocks
    private RoutineService routineService;

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void 루틴_생성(){
        //given
        RoutineCreateRequestDto requestDto= new RoutineCreateRequestDto("제목", "장소");
        User mockUser = new User(1L,"me", "1234");

        Routine savedRoutine=Routine.builder()
                .id(1L)
                .title("제목")
                .location("장소")
                .user(mockUser)
                .build();

        //Mocking 한거임
        when(routineRepository.save(any(Routine.class))).thenReturn(savedRoutine);

        // when  실제 서비스 호출
        Long routineId = routineService.createRoutine(requestDto, Optional.of(mockUser));

        // then
        assertEquals(1L, routineId);
        verify(routineRepository, times(1)).save(any(Routine.class));

    }

    @Test
    void 루틴_삭제(){
        //given
        Routine routine = new Routine(1L, "제목", "장소", null);


        // findById가 호출되면 routine 반환하도록 설정
        when(routineRepository.findById(1L)).thenReturn(Optional.of(routine));
        //Mocking 한거임
        doNothing().when(routineRepository).delete(routine);

        //when
        routineService.deleteRoutine(1L);

        //then
        verify(routineRepository,times(1)).delete(any(Routine.class));

    }

    @Test
    void 루틴_삭제_실패(){

        //given
        Routine routine = new Routine(1L, "제목", "장소", null);



        //given
        doThrow(new RoutineNotFoundException("루틴 없다"))
                .when(routineRepository)
                .delete(any(Routine.class));

        // findById가 호출되면 routine 반환하도록 설정
        when(routineRepository.findById(1L)).thenReturn(Optional.of(routine));

        //when & then
        assertThrows(RoutineNotFoundException.class, () ->{
            routineService.deleteRoutine(1L);
        });
    }

    @Test
    void 루틴_조회(){

        User mockUser = new User(1L,"me", "1234");

        Routine r1 = new Routine(1L, "제목", "장소", null);
        Routine r2 = new Routine(1L, "제목2", "장소2", null);
        //given
        List<Routine> routines = Arrays.asList(r1,r2);

        when(routineRepository.findAllByUserId(1L)).thenReturn(routines);

        //when
        List<Routine> routineList = routineService.getAllRoutines(mockUser);

        //then
        Assertions.assertThat(routineList).contains(r1,r2);
        verify(routineRepository,times(1)).findAllByUserId(anyLong());

    }



}