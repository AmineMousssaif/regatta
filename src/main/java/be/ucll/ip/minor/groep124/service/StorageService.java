package be.ucll.ip.minor.groep124.service;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Storage;
import be.ucll.ip.minor.groep124.model.StorageDto;
import be.ucll.ip.minor.groep124.model.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageService{

    @Autowired
    private StorageRepository storageRepository;

    public List<Storage> findAll(){
        return storageRepository.findAll();
    }

    public List<Storage> findAll(String sortString, boolean ascending) {
        if(!ascending){
            return storageRepository.findAll(Sort.by(sortString).descending());
        } else {
            return storageRepository.findAll(Sort.by(sortString).ascending());
        }
    }

    public List<Storage> findAllByNameContainingIgnoreCase(String value) {
        List<Storage> allStorages = this.findAll();
        allStorages = allStorages.stream().filter(o->o.getName().toLowerCase().startsWith(value.toLowerCase())).collect(Collectors.toList());
        return validateEmptyList(allStorages);
    }

    public List<Storage> validateEmptyList(List<Storage> storages){
        if(storages == null || storages.isEmpty()){
            throw new ServiceException("search", "No storages were found for your search.");
        } else {
            return storages;
        }
    }

    //PAGINATION
    public Page<Storage> findPaginated(Pageable pageable, List<Storage> storageList){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Storage> pageList;

        if (storageList.size() < startItem){
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, storageList.size());
            pageList = storageList.subList(startItem, toIndex);
        }

        Page<Storage> storagePage = new PageImpl<Storage>(pageList, PageRequest.of(currentPage, pageSize), storageList.size());
        return storagePage;
    }

    //STORAGE METHODS
    public Storage getStorage(long storageId) {
        return storageRepository.findById(storageId).orElseThrow(() -> new ServiceException("get", "no.storage.with.this.id"));
    }

    public Storage updateStorage(long id, StorageDto storageDto) {
        Storage storage = getStorage(id);
        if (!storage.storageIsEmpty(storage)) {
            throw new ServiceException("update", "storage.not.empty");
        } else {
            try {
                storage = storageDto.updateStorage(storage);
                return storageRepository.save(storage);
            } catch (DomainException d){
                throw new ServiceException(d.getAction(), d.getMessage());
            } catch (DataIntegrityViolationException e){
                throw new ServiceException("update", "Error updating storage: " + e.getMessage());
            }

        }
    }

    public Storage createStorage(StorageDto storageDto){
        try {
            Storage storage = storageDto.createStorage();
            return storageRepository.save(storage);
        } catch (DomainException d){
            throw new ServiceException(d.getAction(), d.getMessage());
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("create", e.getMessage());
        }
    }

    public void deleteStorage(long id) {
        Storage storage = getStorage(id);
        if (!storage.storageIsEmpty(storage)) {
            throw new ServiceException("delete", "storage.not.empty");
        }
        storageRepository.delete(storage);
    }
}
