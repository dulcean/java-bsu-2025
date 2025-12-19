package backend.src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import backend.src.entity.Anime;
import backend.src.service.AnimeService;

@Controller
public class PageController {
    @Autowired
    private AnimeService animeService;

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @GetMapping("/api/random-anime")
    public String getRandomPersonPage(Model model) {
        try {
            Anime randomPerson = animeService.getRandomPerson();

            model.addAttribute("anime", randomPerson);

            return "result"; // Thymeleaf template name
        } catch (Exception e) {
            model.addAttribute("error",
                    "Ошибка при получении данных: " + e.getMessage());
            return "error";
        }
    }
}