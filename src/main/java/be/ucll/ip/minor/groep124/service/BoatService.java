package be.ucll.ip.minor.groep124.service;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Boat;
import be.ucll.ip.minor.groep124.model.BoatDto;
import be.ucll.ip.minor.groep124.model.BoatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoatService {

    @Autowired
    private  BoatRepository boatRepository;

    public List<Boat> findAll(){
        return boatRepository.findAll();
    }

    public List<Boat> findAll(String sortString, boolean ascending){
        if(!ascending){
            return boatRepository.findAll(Sort.by(sortString).descending());
        } else {
            return boatRepository.findAll(Sort.by(sortString).ascending());
        }
    }

    public List<Boat> findAllByBoatInsuranceNumber(String insuranceNumber) {
        if(insuranceNumber == null || insuranceNumber.trim().isEmpty()){
            throw new ServiceException("search", "The insurance number you are looking for cannot be empty.");
        }
        List<Boat> allBoats = this.findAll();
        allBoats = allBoats.stream().filter(o -> o.getInsurance().toLowerCase().startsWith(insuranceNumber.toLowerCase())).collect(Collectors.toList());
        return validateEmptyList(allBoats);
    }

    public List<Boat> findAllByBoatHeightAndWidth(int height, int width) {
        if(height <= 0 || width <= 0) {
            throw new ServiceException("search", "Boats with height and/or width of 0 do not exist.");
        }
        List<Boat> allBoats = this.findAll();
        allBoats = allBoats.stream().filter(o -> o.getHeight() == height && o.getWidth() == width).collect(Collectors.toList());
        return validateEmptyList(allBoats);
    }

    public List<Boat> validateEmptyList(List<Boat> boats){
        if(boats == null || boats.size() == 0){
            throw new ServiceException("search", "No boats were found for your search.");
        } else {
            return boats;
        }
    }

    //PAGINATION
    public Page<Boat> findPaginated(Pageable pageable, List<Boat> boatList){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Boat> pageList;

        if (boatList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, boatList.size());
            pageList = boatList.subList(startItem, toIndex);
        }

        Page<Boat> boatPage
                = new PageImpl<Boat>(pageList, PageRequest.of(currentPage, pageSize), boatList.size());

        return boatPage;
    }

    //BOAT METHODS
    public Boat getBoat(long id){
        return boatRepository.findById(id)
                .orElseThrow(() -> new ServiceException("get", "no.boat.with.this.id"));
    }

    public Boat createBoat(BoatDto boatDto){
        try {
            Boat boat = boatDto.createBoat();
            return boatRepository.save(boat);
        } catch (DomainException d){
            throw new ServiceException(d.getAction(), d.getMessage());
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("create", "Error creating boat: " + e.getMessage());
        }
    }

    public Boat addBoat(Boat boat){
        try{
            return boatRepository.save(boat);
        } catch (DataIntegrityViolationException | IllegalArgumentException exc){
            throw new ServiceException("create", "Could not add boat because: " + exc.getMessage());
        }
    }

    public Boat updateBoat(long id, BoatDto boatDto){
        Boat boat = getBoat(id);

        if (boat.inStorage(boat)) {
            throw new ServiceException("update", "Boat.registered.in.storage.and.can't.be.updated: ");
        }
        else {
            try{
                boat = boatDto.updateBoat(boat);
                return boatRepository.save(boat);
            } catch (DomainException d){
                throw new ServiceException(d.getAction(), d.getMessage());
            } catch (DataIntegrityViolationException exc){
                throw new ServiceException("update", "Could not update boat because: " + exc.getMessage());
            }
        }
    }

    public Boat deleteBoat(long id) {
        if (getBoat(id).inStorage(getBoat(id))) {
            throw new ServiceException("Delete", "Boat.registered.in.storage.and.can't.delete: ");
        } else {
            try {
                Boat boat = getBoat(id);
                boatRepository.deleteById(id);
                return boat;
            } catch (DataIntegrityViolationException exc) {
                throw new ServiceException("Delete", "Could not delete boat because: " + exc.getMessage());
            }
        }
    }

    public Boat updateBoat1(long id, Boat boat){
        Boat boat1 = getBoat(id);

        boat1.setName(boat.getName());
        boat1.setEmail(boat.getEmail());
        boat1.setLength(boat.getLength());
        boat1.setWidth(boat.getWidth());
        boat1.setHeight(boat.getHeight());
        boat1.setInsurance(boat.getInsurance());
        try {
            return boatRepository.save(boat);
        } catch (RuntimeException exc){
            throw new ServiceException("Update", "Could not update boat because: " + exc.getMessage());
        }
    }
}
