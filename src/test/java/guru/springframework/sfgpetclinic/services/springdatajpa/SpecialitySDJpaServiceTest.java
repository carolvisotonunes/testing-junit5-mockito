package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void deleteById() {
        //given - none
        //when
        service.deleteById(1l);
        service.deleteById(1l);
        //then
        then(specialtyRepository).should(times(2)).deleteById(1l);
    }

    @Test
    void deleteByIdAtLeastOnce() {
        //given - none
        //when
        service.deleteById(1l);
        service.deleteById(1l);
        //then
        then(specialtyRepository).should(atLeastOnce()).deleteById(1l);
    }

    @Test
    void deleteByIdAtMost() {
        //when
        service.deleteById(1l);
        service.deleteById(1l);
        //then
        then(specialtyRepository).should(atMost(5)).deleteById(1l);
    }

    @Test
    void deleteByIdNever() {
        //when
        service.deleteById(1l);
        service.deleteById(1l);
        //then
        //then(specialtyRepository).should(atLeastOnce()).deleteById(5l);
        then(specialtyRepository).should(never()).deleteById(5l);
    }


    @Test
    void delete() {
        //when
        service.delete(new Speciality());
        //then
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void findById() {
        Speciality speciality = new Speciality();
        when(specialtyRepository.findById(1l)).thenReturn(Optional.of(speciality));
        Speciality found = service.findById(1l);
        assertThat(found).isNotNull();
        verify(specialtyRepository).findById(anyLong());
    }

    @Test
    void findByIdBDD() {
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1l)).willReturn(Optional.of(speciality));
        Speciality found = service.findById(1l);
        assertThat(found).isNotNull();
        verify(specialtyRepository).findById(anyLong());
    }

    @Test
    void findByIdBDDWhen() {
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1l)).willReturn(Optional.of(speciality));
        //when
        Speciality found = service.findById(1l);
        //then
        assertThat(found).isNotNull();
        //verify(specialtyRepository).findById(anyLong());
        //then(specialtyRepository).should().findById(anyLong());
        then(specialtyRepository).should(times(1)).findById(anyLong());
        //then(specialtyRepository).shouldHaveNoInteractions();
    }

    @Test
    void testDeleteByObject() {
        //given
        Speciality speciality = new Speciality();
        //when
        service.delete(speciality);
        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }


    @Test
    void testDoThrow(){
        doThrow(new RuntimeException("bom")).when(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class,() ->specialtyRepository.delete(new Speciality()));
        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIdThrows(){
        //use this way to BDD approach
        given(specialtyRepository.findById(1l)).willThrow(new RuntimeException("boom"));
        assertThrows(RuntimeException.class,() ->service.findById(1L));
        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testDeleteBDD(){
        //use willThrow to void return
        willThrow(new RuntimeException("bom")).given(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class, ()->specialtyRepository.delete(new Speciality()));
        then(specialtyRepository).should().delete(any());
    }
}