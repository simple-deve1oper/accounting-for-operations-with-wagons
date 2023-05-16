package dev.accounting.service;

import dev.accounting.entity.Station;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StationService {
    @Autowired
    private StationRepository stationRepository;

    public List<Station> findAll() {
        List<Station> stations = stationRepository.findAll();

        return stations;
    }

    public Station findById(Long id) {
        Optional<Station> station = stationRepository.findById(id);

        return station
                .orElseThrow(() -> new EntityNotFoundException(String.format("Станция с идентификатором %d не найдена", id)));
    }

    @Transactional
    public Station save(Station station) {
        return stationRepository.save(station);
    }

    @Transactional
    public void delete(Station station) {
        stationRepository.delete(station);
    }
}
