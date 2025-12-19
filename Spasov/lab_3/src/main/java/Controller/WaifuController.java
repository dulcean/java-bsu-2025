package waifu.controller;

import waifu.model.Waifu;
import waifu.repository.WaifuRepository;
import waifu.repository.WaifuStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WaifuController {
    @Autowired
    private WaifuRepository repository;

    @GetMapping("/search-image")
    public String searchImage(@RequestParam String query) {
        Optional<Waifu> existing = repository.findFirstByWaifuNameAndImageUrlIsNotNullOrderByIdDesc(query);
        if (existing.isPresent()) {
            return existing.get().getImageUrl();
        }

        String serviceUrl = "https://loremflickr.com/320/240/" + query.replace(" ", "");
        String finalUrl = serviceUrl;

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(serviceUrl).openConnection();

            con.setInstanceFollowRedirects(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.connect();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                finalUrl = con.getURL().toString();
            }
            con.disconnect();
        } catch (Exception e) {
            System.err.println("Final error get error: " + e.getMessage());
        }

        return finalUrl;
    }

    @PostMapping("/submit")
    public Waifu submitWaifu(@RequestBody Waifu waifu) {
        return repository.save(waifu);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("waifuName") String waifuName) throws IOException {

        String base64Image = "data:" + file.getContentType() + ";base64," +
                Base64.getEncoder().encodeToString(file.getBytes());

        Waifu waifu = new Waifu();
        waifu.setUsername(username);
        waifu.setWaifuName(waifuName);
        waifu.setImageUrl(base64Image);

        repository.save(waifu);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public List<WaifuStats> getStats() {
        return repository.getGlobalStats();
    }

    @GetMapping("/spy-data")
    public List<Waifu> getSpyData() {
        return repository.findAll();
    }
}