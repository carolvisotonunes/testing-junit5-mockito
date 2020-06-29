package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_1 = "redirect:/owners/1";

    @Mock
    OwnerService ownerService;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController controller;


    @Test
    void processCreationForm() {
        //given
        Owner owner = new Owner(1l, "name", "lastname");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any())).willReturn(owner);

        //when
        String viewed = controller.processCreationForm(owner,bindingResult);

        //then
        assertThat(viewed).isEqualToIgnoringCase(REDIRECT_OWNERS_1);

    }

    @Test
    void processCreationFormErrors() {
        //given
        Owner owner = new Owner(1l, "name", "lastname");
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String viewed = controller.processCreationForm(owner,bindingResult);

        //then
        assertThat(viewed).isEqualToIgnoringCase(VIEWS_OWNER_CREATE_OR_UPDATE_FORM);

    }

    @Test
    void processFindFormWildCardString(){
        //given
        Owner owner = new Owner(1l, "name", "lastname");
        List<Owner> owners = new ArrayList<>();
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(ownerService.findAllByLastNameLike(captor.capture())).willReturn(owners);

        //when
        String viewName = controller.processFindForm(owner,bindingResult,null);

        //then
        assertThat("%lastname%").isEqualToIgnoringCase(captor.getValue());

    }

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;
    @Test
    void processFindFormWildCardStringAnnotations(){
        //given
        Owner owner = new Owner(1l, "name", "lastname");
        List<Owner> owners = new ArrayList<>();
        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture())).willReturn(owners);

        //when
        String viewName = controller.processFindForm(owner,bindingResult,null);

        //then
        assertThat("%lastname%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());

    }



}