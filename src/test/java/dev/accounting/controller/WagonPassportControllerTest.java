package dev.accounting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.accounting.dto.TypeDTO;
import dev.accounting.dto.WagonDTO;
import dev.accounting.entity.Type;
import dev.accounting.entity.Wagon;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.security.PersonDetailsService;
import dev.accounting.service.TypeService;
import dev.accounting.service.WagonService;
import dev.accounting.util.DataTransformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестирование класса WagonPassportController
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(WagonPassportController.class)
public class WagonPassportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WagonService wagonService;
    @MockBean
    private TypeService typeService;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private PersonDetailsService personDetailsService;

    private Wagon wagon1;
    private Wagon wagon2;
    private Type type;

    @BeforeEach
    void setUp() {
        type = new Type(1L, "Тип 1", Collections.emptyList());
        wagon1 = new Wagon(123L, "1122334", type, 1.1, 1.3);
        wagon2 = new Wagon(155L, "4433221", type, 2.2, 2.6);
        type.setWagons(Arrays.asList(wagon1, wagon2));
    }

    @Test
    @WithMockUser(username = "guest", password = "4321", roles = {"GUEST"})
    void wagonPassportController_getAllWagons() throws Exception {
        List<Wagon> wagons = Arrays.asList(
                wagon1, wagon2
        );
        Mockito.when(wagonService.findAll()).thenReturn(wagons);
        List<WagonDTO> wagonsDTO = DataTransformation.convertingListDataWagonsFromEntityToDTO(wagons);
        String responseJson = objectMapper.writeValueAsString(wagonsDTO);

        mockMvc.perform(get("/api/v1/passport/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(123, 155)))
                .andExpect(jsonPath("$[*].number", containsInAnyOrder("1122334", "4433221")));
    }

    @Test
    @WithMockUser(username = "guest", password = "4321", roles = {"GUEST"})
    void wagonPassportController_getWagonByNumber() throws Exception {
        Mockito.when(wagonService.findByNumber("1122334")).thenReturn(wagon1);
        WagonDTO wagonDTO = DataTransformation.convertingWagonDataFromEntityToDTO(wagon1);
        String responseJson = objectMapper.writeValueAsString(wagonDTO);

        mockMvc.perform(get("/api/v1/passport/1122334")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(123)))
                .andExpect(jsonPath("$.number", equalTo("1122334")))
                .andExpect(jsonPath("$.type.id", equalTo(1)))
                .andExpect(jsonPath("$.type.name", equalTo("Тип 1")))
                .andExpect(jsonPath("$.tareWeight", equalTo(1.1)))
                .andExpect(jsonPath("$.loadCapacity", equalTo(1.3)));

        mockMvc.perform(get("/api/v1/passport/12345678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Номер вагона не может содержать более 7 цифр")));

        Mockito.when(wagonService.findByNumber("1111111"))
                .thenThrow(new EntityNotFoundException("Вагон с номером '1111111' не найден"));
        mockMvc.perform(get("/api/v1/passport/1111111")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Вагон с номером '1111111' не найден")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonPassportController_createWagon() throws Exception {
        WagonDTO wagonDTO = new WagonDTO("4433221", new TypeDTO(1L, "Тип 1"), 2.2, 2.6);
        WagonDTO wagonDTOFromDB = new WagonDTO(155L, "4433221", new TypeDTO(1L, "Тип 1"), 2.2, 2.6);
        Mockito.when(wagonService.save(Mockito.any(Wagon.class))).thenReturn(wagon2);
        String requestJson = objectMapper.writeValueAsString(wagonDTO);
        String responseJson = objectMapper.writeValueAsString(wagonDTOFromDB);
        mockMvc.perform(post("/api/v1/passport/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(155)))
                .andExpect(jsonPath("$.number", equalTo("4433221")))
                .andExpect(jsonPath("$.type.id", equalTo(1)))
                .andExpect(jsonPath("$.type.name", equalTo("Тип 1")))
                .andExpect(jsonPath("$.tareWeight", equalTo(2.2)))
                .andExpect(jsonPath("$.loadCapacity", equalTo(2.6)));

        WagonDTO otherWagonDTO = new WagonDTO("", new TypeDTO(1L, "Тип 1"), 2.2, 2.6);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getFieldErrors())
                .thenReturn(Collections.singletonList(new FieldError("otherWagonDTO", "number", "Поле number не может быть пустым")));
        requestJson = objectMapper.writeValueAsString(otherWagonDTO);
        mockMvc.perform(post("/api/v1/passport/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Поле number не может быть пустым;")));

        Mockito.when(wagonService.existByNumber("7777744")).thenReturn(true);
        wagonDTO = new WagonDTO("7777744", new TypeDTO(1L, "Тип 1"), 1.9, 2.1);
        requestJson = objectMapper.writeValueAsString(wagonDTO);
        mockMvc.perform(post("/api/v1/passport/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo("Вагон с номером '7777744' уже существует")));

        wagonDTO = new WagonDTO("8887799", new TypeDTO(), 1.9, 2.1);
        requestJson = objectMapper.writeValueAsString(wagonDTO);
        mockMvc.perform(post("/api/v1/passport/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Ошибка валидации типа вагона")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonPassportController_updateWagon() throws Exception {
        WagonDTO wagonDTO = new WagonDTO(155L, "4433221", new TypeDTO(1L, "Тип 1"), 2.2, 2.6);
        Mockito.when(wagonService.save(Mockito.any(Wagon.class))).thenReturn(wagon2);
        Mockito.when(wagonService.existByNumber("4433221")).thenReturn(true);
        Mockito.when(wagonService.getIdByNumber("4433221")).thenReturn(155L);
        String requestJson = objectMapper.writeValueAsString(wagonDTO);
        String responseJson = objectMapper.writeValueAsString(wagonDTO);
        mockMvc.perform(put("/api/v1/passport/4433221/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(155)))
                .andExpect(jsonPath("$.number", equalTo("4433221")))
                .andExpect(jsonPath("$.type.id", equalTo(1)))
                .andExpect(jsonPath("$.type.name", equalTo("Тип 1")))
                .andExpect(jsonPath("$.tareWeight", equalTo(2.2)))
                .andExpect(jsonPath("$.loadCapacity", equalTo(2.6)));

        WagonDTO otherWagonDTO = new WagonDTO("", new TypeDTO(1L, "Тип 1"), 2.2, 2.6);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getFieldErrors())
                .thenReturn(Collections.singletonList(new FieldError("otherWagonDTO", "number", "Поле number не может быть пустым")));
        requestJson = objectMapper.writeValueAsString(otherWagonDTO);
        mockMvc.perform(put("/api/v1/passport/4433221/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Поле number не может быть пустым;")));

        requestJson = objectMapper.writeValueAsString(wagonDTO);
        mockMvc.perform(put("/api/v1/passport/12345678/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Номер вагона не может содержать более 7 цифр")));

        mockMvc.perform(put("/api/v1/passport/1111111/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Вагон с номером '1111111' не существует")));

        wagonDTO = new WagonDTO("4433221", new TypeDTO(), 2.2, 2.6);
        requestJson = objectMapper.writeValueAsString(wagonDTO);
        mockMvc.perform(put("/api/v1/passport/4433221/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Ошибка валидации типа вагона")));
    }

    @Test
    @WithMockUser(username = "admin", password = "1111", roles = {"ADMIN"})
    void wagonPassport_deleteWagon() throws Exception {
        Mockito.when(wagonService.findByNumber("4433221")).thenReturn(wagon2);
        Mockito.when(wagonService.existByNumber("4433221")).thenReturn(true);
        Mockito.doNothing().when(wagonService).delete(Mockito.any(Wagon.class));
        mockMvc.perform(delete("/api/v1/passport/4433221/delete")).andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/passport/12345678/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Номер вагона не может содержать более 7 цифр")));

        Mockito.when(wagonService.existByNumber("1111111")).thenReturn(false);
        mockMvc.perform(delete("/api/v1/passport/1111111/delete"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Вагон с номером '1111111' не существует")));
    }
}
