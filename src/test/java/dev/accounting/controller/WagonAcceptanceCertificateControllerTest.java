package dev.accounting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.accounting.dto.*;
import dev.accounting.entity.Cargo;
import dev.accounting.entity.Document;
import dev.accounting.entity.Pathway;
import dev.accounting.entity.Station;
import dev.accounting.security.PersonDetailsService;
import dev.accounting.service.CargoService;
import dev.accounting.service.DocumentService;
import dev.accounting.service.PathwayService;
import dev.accounting.service.WagonService;
import dev.accounting.util.DataTransformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Тестирование класса WagonAcceptanceCertificateController
 * @version 1.0
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(WagonAcceptanceCertificateController.class)
public class WagonAcceptanceCertificateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DocumentService documentService;
    @MockBean
    private WagonService wagonService;
    @MockBean
    private CargoService cargoService;
    @MockBean
    private PathwayService pathwayService;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private PersonDetailsService personDetailsService;

    private Cargo cargo;
    private Pathway pathway;
    private Document document1;
    private Document document2;
    private Document document3;

    @BeforeEach
    void setUp() {
        cargo = new Cargo(1L, "001", "Тест 1", Collections.emptyList());
        Station station = new Station(1L, "Станция 1", Collections.emptyList());
        pathway = new Pathway(1L, station, 6, Collections.emptyList());
        document1 = new Document(1L, 1, "1234567", cargo,
                12.0, 13.0, pathway, null);
        document2 = new Document(2L, 2, "4566543", cargo,
                22.5, 43.1, pathway, null);
        document3 = new Document(3L, 3, "1111111", cargo,
                18.5, 23.3, pathway, LocalDateTime.now());
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonAcceptanceCertificateController_getAllDocuments() throws Exception {
        List<Document> documents = Arrays.asList(document1, document2, document3);
        Mockito.when(documentService.findAll()).thenReturn(documents);
        List<DocumentDTO> documentsDTO = documents.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList());
        String responseJson = objectMapper.writeValueAsString(documentsDTO);

        mockMvc.perform(get("/api/v1/documents/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$[*].serialNumber", containsInAnyOrder(1, 2, 3)))
                .andExpect(jsonPath("$[*].wagonNumber", containsInAnyOrder("1234567", "4566543", "1111111")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonAcceptanceCertificateController_getDocumentById() throws Exception {
        Mockito.when(documentService.findById(Mockito.anyLong()))
                .thenReturn(document1);
        DocumentDTO documentDTO = DataTransformation.convertingDocumentDataFromEntityToDTO(document1);
        String responseJson = objectMapper.writeValueAsString(documentDTO);

        mockMvc.perform(get("/api/v1/documents/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.serialNumber", equalTo(1)))
                .andExpect(jsonPath("$.wagonNumber", equalTo("1234567")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonAcceptanceCertificateController_getAllDocumentsById() throws Exception {
        List<Document> documents = Arrays.asList(document1, document2);
        Mockito.when(documentService.findByDepartureDateIsNullAndPathway_Id(1L, Sort.by("serialNumber")))
                .thenReturn(documents);
        List<DocumentDTO> documentsDTO = documents.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList());
        String responseJson = objectMapper.writeValueAsString(documentsDTO);

        mockMvc.perform(get("/api/v1/documents/1/pathway")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].serialNumber", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].wagonNumber", containsInAnyOrder("1234567", "4566543")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonAcceptanceCertificateController_createDocuments() throws Exception {
        List<Document> documents = Arrays.asList(document1, document2);

        CargoDTO cargoDTO = new CargoDTO(1L, "001", "Тест 1");
        List<WagonBasicDTO> wagonsDTO = Arrays.asList(
                new WagonBasicDTO("1234567", cargoDTO, 12.0, 13.0),
                new WagonBasicDTO("4566543", cargoDTO, 22.5, 43.1)
        );
        StationDTO stationDTO = new StationDTO(1L, "Станция 1");
        PathwayDTO pathwayDTO = new PathwayDTO(1L, stationDTO, 6);
        ReceptionDTO receptionDTO = new ReceptionDTO(wagonsDTO, pathwayDTO);

        Mockito.when(wagonService.existByNumber(Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(documentService.existsByWagonNumberAndDepartureDateIsNull(Mockito.anyString()))
                .thenReturn(false);
        Mockito.when(documentService.maxSerialNumberByDepartureDateIsNullAndPathway_Id(null))
                .thenReturn(null);
        Mockito.when(documentService.saveAll(Mockito.anyList()))
                .thenReturn(documents);
        Mockito.when(pathwayService.findById(1L)).thenReturn(pathway);
        Mockito.when(cargoService.findById(1L)).thenReturn(cargo);

        List<DocumentDTO> documentsDTO = documents.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList());
        String requestJson = objectMapper.writeValueAsString(receptionDTO);
        String responseJson = objectMapper.writeValueAsString(documentsDTO);

        mockMvc.perform(post("/api/v1/documents/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].serialNumber", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].wagonNumber", containsInAnyOrder("1234567", "4566543")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonAcceptanceCertificateController_reorder() throws Exception {
        List<String> wagonNumbers = Arrays.asList("1234567", "4566543");
        StationDTO stationDTO = new StationDTO(1L, "Станция 1");
        PathwayDTO pathwayDTO = new PathwayDTO(1L, stationDTO, 6);
        ChangeDTO changeDTO = new ChangeDTO(wagonNumbers, pathwayDTO, true);

        Station temptation = new Station(1L, "Станция 1", Collections.emptyList());
        Pathway tempPathway = new Pathway(2L, temptation, 8, Collections.emptyList());
        Document tempDocument1 = new Document(1L, 1, "1234567", cargo, 12.0, 13.0, tempPathway, null);
        Document tempDocument2 = new Document(2L, 2, "4566543", cargo,
                22.5, 43.1, tempPathway, null);
        Document tempDocument3 = new Document(3L, 3, "1111111", cargo,
                18.5, 23.3, tempPathway, null);

        Mockito.when(wagonService.existByNumber(Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(documentService.findByWagonNumberAndDepartureDateIsNull("1234567"))
                        .thenReturn(tempDocument1);
        Mockito.when(documentService.findByWagonNumberAndDepartureDateIsNull("4566543"))
                .thenReturn(tempDocument2);
        Mockito.when(pathwayService.findById(1L)).thenReturn(pathway);

        List<Document> documents = Arrays.asList(document1, document2);
        Mockito.when(documentService.saveAll(Mockito.anyList())).thenReturn(documents);
        Mockito.when(documentService.findByDepartureDateIsNullAndPathway_Id(1L, Sort.by("serialNumber")))
                .thenReturn(Collections.emptyList(), documents);

        String requestJson = objectMapper.writeValueAsString(changeDTO);
        String responseJson = objectMapper.writeValueAsString(documents.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList()));

        mockMvc.perform(put("/api/v1/documents/reorder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].serialNumber", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].wagonNumber", containsInAnyOrder("1234567", "4566543")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonAcceptanceCertificateController_departure() throws Exception {
        Mockito.when(pathwayService.findById(1L)).thenReturn(pathway);
        Mockito.when(documentService.maxSerialNumberByDepartureDateIsNullAndPathway_Id(1L)).thenReturn(2);
        Mockito.when(documentService.findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(1L, 1))
                .thenReturn(Collections.singletonList(document1));
        Mockito.when(documentService
                .findByDepartureDateIsNullAndPathway_Id(1L, Sort.by("serialNumber")))
                .thenReturn(Collections.singletonList(document2));

        Document departure = new Document(1L, 1, "1234567", cargo,
                12.0, 13.0, pathway, LocalDateTime.now());
        Document modifiedSerialNumber = document2 = new Document(2L, 1, "4566543", cargo,
                22.5, 43.1, pathway, null);

        List<Document> documents1 = Collections.singletonList(departure);
        List<Document> documents2 = Collections.singletonList(modifiedSerialNumber);
        Mockito.when(documentService.saveAll(Mockito.anyList()))
                .thenReturn(documents1, documents2);

        List<DocumentDTO> documents1DTO = documents1.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList());
        String responseJson = objectMapper.writeValueAsString(documents1DTO);
        
        mockMvc.perform(patch("/api/v1/documents/departure/1/pathway")
                .contentType(MediaType.APPLICATION_JSON).param("quantity", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].serialNumber", containsInAnyOrder(1)))
                .andExpect(jsonPath("$[*].wagonNumber", containsInAnyOrder("1234567")));
    }

    @Test
    @WithMockUser(username = "moderator", password = "1234", roles = {"MODERATOR"})
    void wagonAcceptanceCertificateController_deleteDocument() throws Exception {
        Mockito.when(documentService.findById(Mockito.anyLong())).thenReturn(document3);
        Mockito.doNothing().when(documentService).delete(Mockito.any(Document.class));
        mockMvc.perform(delete("/api/v1/documents/3/delete")).andExpect(status().isOk());
    }
}
