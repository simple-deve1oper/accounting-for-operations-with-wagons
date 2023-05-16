package dev.accounting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.accounting.dto.CargoDTO;
import dev.accounting.entity.Cargo;
import dev.accounting.security.PersonDetailsService;
import dev.accounting.service.CargoService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CargoNomenclatureController.class)
public class CargoNomenclatureControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CargoService cargoService;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private PersonDetailsService personDetailsService;

    private Cargo cargo1;
    private Cargo cargo2;

    @BeforeEach
    void setUp() {
        cargo1 = new Cargo(1L, "001", "Тест 1", Collections.emptyList());
        cargo2 = new Cargo(2L, "002", "Тест 2", Collections.emptyList());
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void cargoNomenclatureController_getAllCargos() throws Exception {
        List<Cargo> cargos = Arrays.asList(cargo1, cargo2);
        Mockito.when(cargoService.findAll())
                .thenReturn(cargos);
        List<CargoDTO> cargosDTO = cargos.stream()
                .map(cargo -> DataTransformation.convertingCargoDataFromEntityToDTO(cargo))
                .collect(Collectors.toList());
        String responseJson = objectMapper.writeValueAsString(cargosDTO);

        mockMvc.perform(get("/api/v1/cargos/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].code", containsInAnyOrder("001", "002")))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Тест 1", "Тест 2")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void cargoNomenclatureController_getCargoById() throws Exception {
        Mockito.when(cargoService.findById(1L)).thenReturn(cargo1);
        CargoDTO cargoDTO = DataTransformation.convertingCargoDataFromEntityToDTO(cargo1);
        String responseJson = objectMapper.writeValueAsString(cargoDTO);

        mockMvc.perform(get("/api/v1/cargos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.code", equalTo("001")))
                .andExpect(jsonPath("$.name", equalTo("Тест 1")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void cargoNomenclatureController_createCargo() throws Exception {
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setCode("001");
        cargoDTO.setName("Тест 1");
        Mockito.when(cargoService.existsByCode(Mockito.anyString())).thenReturn(false);
        Mockito.when(cargoService.save(Mockito.any(Cargo.class)))
                .thenReturn(cargo1);
        CargoDTO cargoDTOFromDB = DataTransformation.convertingCargoDataFromEntityToDTO(cargo1);
        String requestJson = objectMapper.writeValueAsString(cargoDTO);
        String responseJson = objectMapper.writeValueAsString(cargoDTOFromDB);

        mockMvc.perform(post("/api/v1/cargos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.code", equalTo("001")))
                .andExpect(jsonPath("$.name", equalTo("Тест 1")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void cargoNomenclatureController_updateName() throws Exception {
        Cargo tempCargo = new Cargo(1L, "001", "Груз 1", Collections.emptyList());
        Mockito.when(cargoService.findById(1L)).thenReturn(tempCargo);
        Mockito.when(cargoService.save(Mockito.any(Cargo.class))).thenReturn(cargo1);
        CargoDTO cargoDTO = DataTransformation.convertingCargoDataFromEntityToDTO(cargo1);
        String responseJson = objectMapper.writeValueAsString(cargoDTO);

        mockMvc.perform(patch("/api/v1/cargos/1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "Тест 1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.code", equalTo("001")))
                .andExpect(jsonPath("$.name", equalTo("Тест 1")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void cargoNomenclatureController_deleteCargo() throws Exception {
        Mockito.when(cargoService.findById(1L)).thenReturn(cargo1);
        Mockito.doNothing().when(cargoService).delete(Mockito.any(Cargo.class));
        mockMvc.perform(delete("/api/v1/cargos/1/delete")).andExpect(status().isOk());
    }
}
