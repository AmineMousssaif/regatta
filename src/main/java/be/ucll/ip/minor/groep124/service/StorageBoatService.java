package be.ucll.ip.minor.groep124.service;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class StorageBoatService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private BoatRepository boatRepository;

    public Boat addBoatToStorage(long boatId, long storageId){
        Boat boat = boatRepository.findById(boatId).orElseThrow(() -> new ServiceException("get", "no.boat.with.this.id"));
        Storage storage = findStorage(storageId);
        if (!boat.getStorageName().equals("unknown") ) {
            throw new ServiceException("addBoatToStorage", "Boat is registered in a storage, and can't be added");
        }

        try {
            storage.addBoat(boat);
            boatRepository.save(boat);
            return boat;
        } catch (DomainException d){
            throw new ServiceException(d.getAction(), d.getMessage());
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("addBoatToStorage", e.getMessage());
        }
    }

    public Boat removeBoatFromStorage(long boatId, long storageId){
        Boat boat = boatRepository.findById(boatId).orElseThrow(() -> new ServiceException("get", "no.boat.with.this.id"));
        Storage storage = findStorage(storageId);
        try {
            storage.removeBoat(boat);
            boatRepository.save(boat);
            return boat;
        } catch (DomainException d){
            throw new ServiceException(d.getAction(), d.getMessage());
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("removeBoatFromStorage", e.getMessage());
        }
    }

    //Moet geen errors gooien bij empty list/null!
    public Storage findStorage(long storageId){
        return storageRepository.findById(storageId).orElseThrow(() -> new ServiceException("get", "no.storage.with.this.id"));
    }

    public Set<Boat> findBoatsByStorageId(long storageId) {
        return findStorage(storageId).getBoatsStored();
    }
}
