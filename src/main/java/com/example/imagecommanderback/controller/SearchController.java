package com.example.imagecommanderback.controller;

import com.example.imagecommanderback.dto.ImageDto;
import com.example.imagecommanderback.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller provides endpoints to perform images by containing object name.
 *
 * @author Rodion Minkin
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private SearchService searchService;

    public SearchController(@Autowired SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(value = "/images")
    @ResponseBody
    public List<ImageDto> searchImagesWithObject(@RequestParam(value = "object-name") String objectName) {
        return this.searchService.getImagesByLabel(objectName);
    }
}
