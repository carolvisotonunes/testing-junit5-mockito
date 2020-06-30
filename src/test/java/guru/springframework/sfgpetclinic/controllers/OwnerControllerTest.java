package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(
                            invocation -> {
                            List<Owner> owners = new ArrayList<>();
                            String name = invocation.getArgument(0);
                            if (name.equals("%lastname%")) {
                                owners.add(new Owner(1l, "Joe", "Buck"));
                                return owners;
                            } else if (name.equals("%DontFindMe%")) {
                                return owners;
                            } else if (name.equals("%FindMe%")) {
                                owners.add(new Owner(1l, "name1", "lastname1"));
                                owners.add(new Owner(2l, "name2", "lastname2"));
                                return owners;
                            }
                            throw new RuntimeException("Invalid Argument");
                        });
    }


    @Test
    void processCreationForm() {
        //given
        Owner owner = new Owner(1l, "name", "lastname");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any())).willReturn(owner);

        //when
        String viewed = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewed).isEqualToIgnoringCase(REDIRECT_OWNERS_1);

    }

    @Test
    void processCreationFormErrors() {
        //given
        Owner owner = new Owner(1l, "name", "lastname");
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String viewed = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(viewed).isEqualToIgnoringCase(VIEWS_OWNER_CREATE_OR_UPDATE_FORM);

    }


    @Test
    void processFindFormWildCardNotFound() {
        //given
        Owner owner = new Owner(1l, "name", "DontFindMe");
        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
    }

    @Test
    void processFindFormWildCardFound() {
        //given
        Owner owner = new Owner(1l, "name", "FindMe");
        //when
        String viewName = controller.processFindForm(owner, bindingResult, Mockito.mock(Model.class));

        //then
        assertThat("%FindMe%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);
    }


}