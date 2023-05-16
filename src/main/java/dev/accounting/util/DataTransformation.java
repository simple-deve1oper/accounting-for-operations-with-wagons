package dev.accounting.util;

import dev.accounting.dto.*;
import dev.accounting.entity.*;
import dev.accounting.exception.EntityValidationException;
import dev.accounting.service.CargoService;
import dev.accounting.service.PathwayService;
import dev.accounting.service.StationService;
import dev.accounting.service.TypeService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataTransformation {
    public static TypeDTO convertingTypeDataFromEntityToDTO(Type type) {
        Long id = type.getId();
        String name = type.getName();

        TypeDTO typeDTO = new TypeDTO(id, name);

        return typeDTO;
    }

    public static WagonDTO convertingWagonDataFromEntityToDTO(Wagon wagon) {
        Long id = wagon.getId();
        String number = wagon.getNumber();
        TypeDTO type = convertingTypeDataFromEntityToDTO(wagon.getType());
        Double tareWeight = wagon.getTareWeight();
        Double loadCapacity = wagon.getLoadCapacity();

        WagonDTO wagonDTO = new WagonDTO();
        if (id != null) {
            wagonDTO.setId(id);
        }
        wagonDTO.setNumber(number);
        wagonDTO.setType(type);
        wagonDTO.setTareWeight(tareWeight);
        wagonDTO.setLoadCapacity(loadCapacity);

        return wagonDTO;
    }

    public static List<WagonDTO> convertingListDataWagonsFromEntityToDTO(List<Wagon> wagons) {
        return wagons.stream().map(wagon -> convertingWagonDataFromEntityToDTO(wagon))
                .collect(Collectors.toList());
    }

    public static Wagon convertingWagonDataFromDTOToEntity(WagonDTO wagonDTO, TypeService typeService) {
        Long id = wagonDTO.getId();
        String number = wagonDTO.getNumber();
        Type type = typeService.findById(wagonDTO.getType().getId());
        Double tareWeight = wagonDTO.getTareWeight();
        Double loadCapacity = wagonDTO.getLoadCapacity();

        Wagon wagon = new Wagon();
        if (id != null) {
            wagon.setId(id);
        }
        wagon.setNumber(number);
        wagon.setType(type);
        wagon.setTareWeight(tareWeight);
        wagon.setLoadCapacity(loadCapacity);

        return wagon;
    }

    public static PathwayModelDTO convertingPathwayFromEntityToModelDTO(Pathway pathway) {
        Long id = pathway.getId();
        Integer number = pathway.getNumber();

        PathwayModelDTO pathwayModelDTO = new PathwayModelDTO(id, number);

        return pathwayModelDTO;
    }

    public static StationModelDTO convertingStationFromEntityToModelDTO(Station station) {
        Long id = station.getId();
        String name = station.getName();
        List<PathwayModelDTO> patways = station.getPathways().stream()
                .map(pathway -> convertingPathwayFromEntityToModelDTO(pathway))
                .collect(Collectors.toList());

        StationModelDTO stationModelDTO = new StationModelDTO(id, name, patways);

        return stationModelDTO;
    }

    public static Pathway convertingPathwayFromModelDTOToEntity(PathwayModelDTO pathwayModelDTO) {
        Long id = pathwayModelDTO.getId();
        Integer number = pathwayModelDTO.getNumber();

        Pathway pathway = new Pathway(id, null, number, Collections.emptyList());

        return pathway;
    }

    public static Station convertingStationFromModelDTOToEntity(StationModelDTO stationModelDTO) {
        Long id = stationModelDTO.getId();
        String name = stationModelDTO.getName();

        List<Pathway> pathways = stationModelDTO.getPathways().stream()
                .map(p -> convertingPathwayFromModelDTOToEntity(p))
                .collect(Collectors.toList());
        Station station = new Station();
        if (id != null) {
            station.setId(id);
        }
        station.setName(name);
        station.setPathways(pathways);

        return station;
    }

    public static StationDTO convertingStationDataFromEntityToDTO(Station station) {
        Long id = station.getId();
        String name = station.getName();

        StationDTO stationDTO = new StationDTO(id, name);

        return stationDTO;
    }

    public static Pathway convertingPathwayDataFromDTOToEntity(PathwayDTO pathwayDTO, StationService stationService) {
        StationDTO stationDTOFromObject = pathwayDTO.getStation();
        Station stationFromDB = stationService.findById(stationDTOFromObject.getId());
        StationDTO stationDTOFromDB = convertingStationDataFromEntityToDTO(stationFromDB);
        if (!stationDTOFromDB.equals(stationDTOFromObject)) {
            throw new EntityValidationException("Ошибка валидации станции у пути");
        }

        Long id = pathwayDTO.getId();
        Integer number = pathwayDTO.getNumber();

        Pathway pathway = new Pathway();
        if (id != null) {
            pathway.setId(id);
        }
        pathway.setStation(stationFromDB);
        pathway.setNumber(number);

        return pathway;
    }

    public static PathwayDTO convertingPathwayDataFromEntityToDTO(Pathway pathway) {
        Long id = pathway.getId();
        StationDTO station = convertingStationDataFromEntityToDTO(pathway.getStation());
        Integer number = pathway.getNumber();

        PathwayDTO pathwayDTO = new PathwayDTO(id, station, number);

        return pathwayDTO;
    }

    public static CargoDTO convertingCargoDataFromEntityToDTO(Cargo cargo) {
        Long id = cargo.getId();
        String code = cargo.getCode();
        String name = cargo.getName();

        CargoDTO cargoDTO = new CargoDTO(id, code, name);

        return cargoDTO;
    }

    public static Cargo convertingCargoDataFromDTOToEntity(CargoDTO cargoDTO) {
        Long id = cargoDTO.getId();
        String code = cargoDTO.getCode();
        String name = cargoDTO.getName();

        Cargo cargo = new Cargo();
        if (id != null) {
            cargo.setId(id);
        }
        cargo.setCode(code);
        cargo.setName(name);

        return cargo;
    }

    public static DocumentDTO convertingDocumentDataFromEntityToDTO(Document document) {
        Long id = document.getId();
        Integer serialNumber = document.getSerialNumber();
        String wagonNumber = document.getWagonNumber();
        CargoDTO cargoDTO = convertingCargoDataFromEntityToDTO(document.getCargo());
        Double cargoWeight = document.getCargoWeight();
        Double wagonWeight = document.getWagonWeight();
        PathwayDTO pathway = convertingPathwayDataFromEntityToDTO(document.getPathway());
        LocalDateTime departureDate = document.getDepartureDate();

        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setId(id);
        documentDTO.setSerialNumber(serialNumber);
        documentDTO.setWagonNumber(wagonNumber);
        documentDTO.setCargoDTO(cargoDTO);
        documentDTO.setCargoWeight(cargoWeight);
        documentDTO.setWagonWeight(wagonWeight);
        documentDTO.setPathway(pathway);
        if (departureDate != null) {
            documentDTO.setDepartureDate(departureDate);
        }

        return documentDTO;
    }

    public static List<Document> createListDocumentsFromReceptionDTO(ReceptionDTO receptionDTO, CargoService cargoService, PathwayService pathwayService) {
        List<WagonBasicDTO> wagonsDTO = receptionDTO.getWagons();
        Pathway pathway = pathwayService.findById(receptionDTO.getPathway().getId());
        List<Document> documents = wagonsDTO.stream()
                .map(w -> {
                    Cargo cargo = cargoService.findById(w.getCargoDTO().getId());

                    Document document = new Document();
                    document.setWagonNumber(w.getWagonNumber());
                    document.setCargo(cargo);
                    document.setCargoWeight(w.getCargoWeight());
                    document.setWagonWeight(w.getWagonWeight());
                    document.setPathway(pathway);

                    return document;
                })
                .collect(Collectors.toList());

        return documents;
    }
}
