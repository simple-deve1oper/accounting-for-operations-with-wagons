package dev.accounting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.accounting.dto.PathwayDTO;
import dev.accounting.dto.PathwayModelDTO;
import dev.accounting.dto.StationDTO;
import dev.accounting.dto.StationModelDTO;
import dev.accounting.entity.Pathway;
import dev.accounting.entity.Station;
import dev.accounting.security.PersonDetailsService;
import dev.accounting.service.PathwayService;
import dev.accounting.service.StationService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Тестирование класса StationModelController
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(StationModelController.class)
public class StationModelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StationService stationService;
    @MockBean
    private PathwayService pathwayService;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private PersonDetailsService personDetailsService;

    private Station station;
    private Pathway pathway1;
    private Pathway pathway2;

    @BeforeEach
    void setUp() {
        station = new Station(1L, "Станция 1", Collections.emptyList());
        pathway1 = new Pathway(1L, station, 1, Collections.emptyList());
        pathway2 = new Pathway(2L, station, 2, Collections.emptyList());
        station.setPathways(Arrays.asList(pathway1, pathway2));
    }

    @Test
    @WithMockUser(username = "guest", password = "4321", roles = {"GUEST"})
    void stationModelController_getAllStations() throws Exception {
        List<Station> stations = Collections.singletonList(station);
        Mockito.when(stationService.findAll()).thenReturn(stations);
        List<StationModelDTO> stationsDTO = stations.stream()
                .map(s -> DataTransformation.convertingStationFromEntityToModelDTO(s))
                .collect(Collectors.toList());
        String responseJson = objectMapper.writeValueAsString(stationsDTO);

        mockMvc.perform(get("/api/v1/model/stations/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Станция 1")))
                .andExpect(jsonPath("$[0].pathways", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "guest", password = "4321", roles = {"GUEST"})
    void stationModelController_getStationById() throws Exception {
        Mockito.when(stationService.findById(1L)).thenReturn(station);
        StationModelDTO stationModelDTO = DataTransformation.convertingStationFromEntityToModelDTO(station);
        String responseJson = objectMapper.writeValueAsString(stationModelDTO);

        mockMvc.perform(get("/api/v1/model/stations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Станция 1")))
                .andExpect(jsonPath("$.pathways", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void stationModelController_createStation() throws Exception {
        StationModelDTO stationModelDTO = new StationModelDTO();
        stationModelDTO.setName("Станция 1");
        List<PathwayModelDTO> pathwayModelsDTO = Arrays.asList(
                new PathwayModelDTO(1L, 1),
                new PathwayModelDTO(2L, 2)
        );
        stationModelDTO.setPathways(pathwayModelsDTO);
        String requestJson = objectMapper.writeValueAsString(stationModelDTO);
        Mockito.when(stationService.save(Mockito.any(Station.class)))
                .thenReturn(station);
        StationModelDTO stationModelDTOFromDB = DataTransformation.convertingStationFromEntityToModelDTO(station);
        String responseJson = objectMapper.writeValueAsString(stationModelDTOFromDB);

        mockMvc.perform(post("/api/v1/model/stations/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Станция 1")))
                .andExpect(jsonPath("$.pathways", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void stationModelController_updateNameStation() throws Exception {
        Station tempStation = new Station(1L, "Тест", Arrays.asList(pathway1, pathway2));
        Mockito.when(stationService.findById(1L)).thenReturn(tempStation);
        Mockito.when(stationService.save(Mockito.any(Station.class))).thenReturn(station);
        StationModelDTO stationModelDTO = DataTransformation.convertingStationFromEntityToModelDTO(station);
        String responseJson = objectMapper.writeValueAsString(stationModelDTO);

        mockMvc.perform(patch("/api/v1/model/stations/1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "Станция 1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo("Станция 1")))
                .andExpect(jsonPath("$.pathways", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void stationModelController_deleteStation() throws Exception {
        Mockito.when(stationService.findById(1L)).thenReturn(station);
        Mockito.doNothing().when(stationService).delete(Mockito.any(Station.class));
        mockMvc.perform(delete("/api/v1/model/stations/1/delete")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void stationModelController_createPathway() throws Exception {
        StationDTO stationDTO = new StationDTO(1L, "Станция 1");
        PathwayDTO pathwayDTO = new PathwayDTO();
        pathwayDTO.setStation(stationDTO);
        pathwayDTO.setNumber(2);
        Mockito.when(pathwayService.save(Mockito.any(Pathway.class)))
                .thenReturn(pathway2);
        Mockito.when(stationService.findById(1L)).thenReturn(station);
        PathwayDTO pathwayDTOFromDB = new PathwayDTO(2L, stationDTO, 2);

        String requestJson = objectMapper.writeValueAsString(pathwayDTO);
        String responseJson = objectMapper.writeValueAsString(pathwayDTOFromDB);

        mockMvc.perform(post("/api/v1/model/pathways/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(2)))
                .andExpect(jsonPath("$.station.id", equalTo(1)))
                .andExpect(jsonPath("$.station.name", equalTo("Станция 1")))
                .andExpect(jsonPath("$.number", equalTo(2)));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void stationModelController_deletePathway() throws Exception {
        Mockito.when(pathwayService.findById(2L)).thenReturn(pathway2);
        Mockito.doNothing().when(pathwayService).delete(Mockito.any(Pathway.class));
        mockMvc.perform(delete("/api/v1/model//pathways/2/delete")).andExpect(status().isOk());
    }
}
