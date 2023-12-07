package be.ucll.ip.minor.groep124.service;

import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegattaService {

    @Autowired
    private RegattaRepository regattaRepository;

    public List<Regatta> findAll() {
        return regattaRepository.findAll();
    }

    public List<Regatta> findAllCaseSensitive(String sortString, boolean ascending) {
        if(!ascending){
            return regattaRepository.findAll(Sort.by(sortString).descending());
        } else {
            return regattaRepository.findAll(Sort.by(sortString).ascending());
        }
    }

    public List<Regatta> findAllIgnoreCase(Comparator<Regatta> comparator, boolean ascending) {
        List<Regatta> regattas = findAll();

        if(!ascending){
            regattas.sort(comparator.reversed());
            return regattas;
        } else {
            regattas.sort(comparator);
            return regattas;
        }
    }

    public List<Regatta> findAllByCategoryAndOrPeriodIgnoreCase(String category, LocalDate dateAfter, LocalDate dateBefore){
        if (category == null || category.trim().isEmpty()) {
            category = "";
        }
        if(dateAfter == null || dateBefore == null || dateAfter.toString().trim().isEmpty() || dateBefore.toString().trim().isEmpty()){
            //BELOW: is an option to throw a warning in case someone only selects 1 date for the period.
            //This is too difficult to implement for our expertise.
            //However; the possible code is present to showcase this part of user-friendliness was thought of.
            /*            if(dateAfter != null && dateAfter.toString().trim().isEmpty()){
                dateAfter = null;
            }
            if(dateBefore != null && dateBefore.toString().trim().isEmpty()){
                dateBefore = null;
            }
            if(dateAfter != null || dateBefore != null){
                model.addAttribute("error", "To search within a time period you need to give up both the \"After date\" " +
                        "and the \"Before date\". The results below are only filtered on category if you filled in that field.");
            }*/
            return findAllByCategoryContainingIgnoreCase(category.trim());
        } else {
            return findAllByCategoryAndPeriod(category.trim(), dateAfter, dateBefore);
        }
    }

    private List<Regatta> findAllByCategoryContainingIgnoreCase(String category) {
        List<Regatta> allRegattas = this.findAll();
        allRegattas = allRegattas.stream().filter(o->o.getCategory().toLowerCase().startsWith(category.toLowerCase())).collect(Collectors.toList());
        return validateEmptyList(allRegattas);
    }

    private List<Regatta> findAllByCategoryAndPeriod(String category, LocalDate dateAfter, LocalDate dateBefore) {
        List<Regatta> allRegattas = this.findAll();
        allRegattas = allRegattas.stream().filter(o->o.getCategory().toLowerCase().startsWith(category.toLowerCase()) && o.getDate().isBefore(dateBefore) && o.getDate().isAfter(dateAfter)).collect(Collectors.toList());
        return validateEmptyList(allRegattas);
    }

    public List<Regatta> validateEmptyList(List<Regatta> regattas){
        if(regattas == null || regattas.isEmpty()){
            throw new ServiceException("search", "No regattas were found for your search.");
        } else {
            return regattas;
        }
    }

    //PAGINATION
    public Page<Regatta> findPaginated(Pageable pageable, List<Regatta> regattasList){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Regatta> pageList;

        if (regattasList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, regattasList.size());
            pageList = regattasList.subList(startItem, toIndex);
        }

        Page<Regatta> regattaPage
                = new PageImpl<Regatta>(pageList, PageRequest.of(currentPage, pageSize), regattasList.size());

        return regattaPage;
    }

    //REGATTA METHODS
    public Regatta getRegatta(long id) {
        return regattaRepository.findById(id)
                .orElseThrow(() -> new ServiceException("get", "no.regatta.with.this.id"));
    }

    public Regatta updateRegatta(long id, RegattaDto regattaDto) {
        Regatta regatta = getRegatta(id);
        if (!regatta.hasNoTeams(regatta)) {
            throw new ServiceException("update", "regatta.has.teams");
        } else {
            regatta = regattaDto.updateRegatta(regatta);
            try {
                return regattaRepository.save(regatta);
            } catch (DataIntegrityViolationException e){
                throw new ServiceException("update", "Error updating regatta: " + e.getMessage());
            }
        }
    }

    public Regatta createRegatta(RegattaDto regattaDto){
        for(Regatta regatta : findAll()) {
            if(regatta.getOrganiser().equals(regattaDto.getOrganiser()) && regatta.getDate().equals(regattaDto.getDate())) {
                throw new ServiceException("create" + "a club cannot organise 2 or more regattas on the same day.");
            }
        }
        Regatta regatta = regattaDto.createRegatta();
        try {
            return regattaRepository.save(regatta);
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("create", e.getMessage());
        }
    }

    public void deleteRegatta(long id) {
        if(!getRegatta(id).hasNoTeams(getRegatta(id))){
            throw new ServiceException("delete", "regatta.has.teams");
        }
        regattaRepository.delete(getRegatta(id));
    }

    public List<Regatta> findByOrganiser(String organiser) {
        List<Regatta> allRegattas = this.findAll();
        allRegattas = allRegattas.stream().filter(o->o.getOrganiser().toLowerCase().startsWith(organiser.toLowerCase())).collect(Collectors.toList());
        return validateEmptyList(allRegattas);
    }
}

