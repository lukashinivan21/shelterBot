package tgbots.shelterbot.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tgbots.shelterbot.models.Report;
import tgbots.shelterbot.service.by_models.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/catReport")
public class CatReportController {

    private final ReportService catReportService;

    public CatReportController(@Qualifier("catReportService")ReportService catReportService) {
        this.catReportService = catReportService;
    }

    @GetMapping("{id}")
    public ResponseEntity<List<? extends Report>> getReportsByCandidateId(@PathVariable Long id) {
        List<? extends Report> result = catReportService.getReportByCandidateId(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<? extends Report>> getAllReports() {
        List<? extends Report> result = catReportService.getAllReports();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/idsOfReports")
    public ResponseEntity<List<Long>> getIdsOfReports(@RequestParam Long id, @RequestParam String dateS) {
        LocalDate date = LocalDate.parse(dateS);
        List<Long> result = catReportService.idsOfReportsByCandidateIdAndDate(id, date);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}/report-photo-from-db")
    public ResponseEntity<byte[]> getReport(@PathVariable Long id) {
        Report result = catReportService.findReportByReportId(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(result.getData().length);
        headers.setContentType(MediaType.parseMediaType("image/jpeg"));
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(result.getData());
    }

    @GetMapping(value = "/{id}/report-photo-from-file")
    public void downloadReport(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Report result = catReportService.findReportByReportId(id);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Path path = Path.of(result.getFilePath());
        try (InputStream is = Files.newInputStream(path); OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType("image/jpeg");
            response.setContentLength((int) result.getFileSize());
            is.transferTo(os);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDogReportsByCandidateId(@PathVariable Long id) {
        String result = catReportService.deleteReportsByCandidateId(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{dateS}")
    public ResponseEntity<String> deleteDogReportsByDate(@PathVariable String dateS) {
        LocalDate date = LocalDate.parse(dateS);
        String result = catReportService.deleteReportsByDate(date);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDogReportsByCandidateIdAndDate(@RequestParam Long id, @RequestParam String dateS) {
        LocalDate date = LocalDate.parse(dateS);
        String result = catReportService.deleteReportsByCandidateIdAndDateReport(id, date);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clear() {
        catReportService.clear();
        return ResponseEntity.ok().build();
    }
}
