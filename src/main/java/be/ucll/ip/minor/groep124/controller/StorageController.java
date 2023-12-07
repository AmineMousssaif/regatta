package be.ucll.ip.minor.groep124.controller;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Storage;
import be.ucll.ip.minor.groep124.model.StorageDto;
import be.ucll.ip.minor.groep124.service.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("storage")
public class StorageController {

    private int currentPage = 1, currentSize = 5;

    @Autowired
    private StorageService storageService;

    //EVERYTHING OVERVIEW - SORT - SEARCH
    @GetMapping("/overview")
    public String overview (Model model,
                            @RequestParam("page") Optional<Integer> page,
                            @RequestParam("size") Optional<Integer> size) {
        List<Storage> storages = storageService.findAll();
        setPagination(storages, "overview", model, page, size);
        if (storages.isEmpty()) {
            createSampleData();
            System.out.println("Sample data created");
        } else {
            System.out.println("Sample data not created");
        }
        return "overview-storage";
    }

    @GetMapping("/sort/{column}")
    public String overviewByName(@PathVariable String column, Model model,
                                 @RequestParam("page") Optional<Integer> page,
                                 @RequestParam("size") Optional<Integer> size) {
        List<Storage> result = storageService.findAll(column, true);
        setPagination(result, column, model, page, size);
        return "overview-storage";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "searchValue", required = false) String searchValue1, Model model,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size) {
        if (searchValue1 == null || searchValue1.trim().isEmpty()) {
            return "redirect:/storage/overview";
        }
        List<Storage> all = Collections.emptyList();
        try {
            all =  storageService.findAllByNameContainingIgnoreCase(searchValue1.trim());
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        String currentNav = "search?searchValue="+searchValue1;
        setPagination(all, currentNav, model, page, size);
        model.addAttribute("searchValue", searchValue1);
        return "overview-storage";
    }

    //ADD
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("storageDto", new StorageDto());
        return "add-storage";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute @Valid StorageDto storageDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("storageDto", storageDto);
            return "add-storage";
        }

        try {
            storageService.createStorage(storageDto);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "add-storage";
        }
        return "redirect:/storage/overview";
    }

    //UPDATE
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Storage storage = storageService.getStorage(id);
            model.addAttribute("storageDto", storage.toDto());
        } catch (DomainException | ServiceException exc) {
            model.addAttribute("error", exc.getMessage());
        }
        return "update-storage";
    }

    @PostMapping("/update/{id}")
    public String update (@PathVariable("id") long id,
                          @Valid StorageDto storage, BindingResult result, Model model) {
        if (result.hasErrors()) {
            storage.setId(id);
            model.addAttribute("storageDto", storage);
            return "update-storage";
        }
        try {
            storageService.updateStorage(id, storage);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
            return "update-storage";
        }

        return "redirect:/storage/overview";
    }

    //DELETE
    @GetMapping("/delete")
    public String delete_confirm(@RequestParam("id") long id, Model model) {
        try {
            Storage storage = storageService.getStorage(id);
            model.addAttribute("storageDto", storage.toDto());
        } catch (DomainException | ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "delete-storage-confirm";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") long id, Model model) {
        try {
            storageService.deleteStorage(id);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/storage/overview";
    }

    //DATA INJECTION & TRANSFER
    private void createSampleData() {
        StorageDto storage = new StorageDto();
        storage.setName("Bramske");
        storage.setHeight(10);
        storage.setPostalCode("3010");
        storage.setStorageSpace(10);
        storage.setYear(2020);

        storageService.createStorage(storage);
    }

    //PAGINATION
    //https://www.baeldung.com/spring-thymeleaf-pagination
    private List<Integer> getPageNumbers(int totalPages){
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            return pageNumbers;
        } else {
            return null;
        }
    }

    private void setPagination(List<Storage> desiredResult, String currentNav, Model model, Optional<Integer> page, Optional<Integer> size){
        currentPage = page.orElse(1);
        currentSize = size.orElse(5);

        Page<Storage> storagePage = storageService.findPaginated(PageRequest.of(currentPage - 1, currentSize), desiredResult);

        int totalPages = storagePage.getTotalPages();
        List<Integer> pageNumbers = getPageNumbers(totalPages);
        if(pageNumbers != null){
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("current", currentNav);
        }
        model.addAttribute("storages", storagePage);
    }
}
