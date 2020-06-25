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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void deleteById() {
        service.deleteById(1l);
        verify(specialtyRepository).deleteById(1l);
    }

    @Test
    void deleteByIdAtLeastOnce() {
        service.deleteById(1l);
        service.deleteById(1l);
        verify(specialtyRepository, atLeastOnce()).deleteById(1l);
    }

    @Test
    void deleteByIdAtMost() {
        service.deleteById(1l);
        service.deleteById(1l);
        verify(specialtyRepository, atMost(5)).deleteById(1l);
    }

    @Test
    void deleteByIdNever() {
        service.deleteById(1l);
        service.deleteById(1l);
        verify(specialtyRepository, atLeastOnce()).deleteById(1l);
        verify(specialtyRepository, never()).deleteById(5l);
    }


    @Test
    void delete() {
        service.delete(new Speciality());
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
    void findByIdBBDB() {
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1l)).willReturn(Optional.of(speciality));
        Speciality found = service.findById(1l);
        assertThat(found).isNotNull();
        verify(specialtyRepository).findById(anyLong());
    }

    @Test
    void testDeleteByObject() {
        Speciality speciality = new Speciality();
        service.delete(speciality);
        verify(specialtyRepository).delete(any(Speciality.class));
    }
}