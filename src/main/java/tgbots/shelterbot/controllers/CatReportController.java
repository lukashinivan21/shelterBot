package tgbots.shelterbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tgbots.shelterbot.models.CatReport;
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

    private final ReportService<CatReport> catReportService;

    public CatReportController(@Qualifier("catReportService") ReportService<CatReport> catReportService) {
        this.catReportService = catReportService;
    }

    @Operation(summary = "Поиск всех отчетов конкретного пользователя приюта для кошек по id пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Все отчеты пользователя успешно найдены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatReport[].class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя с таким id нет в базе данных"
                    )
            })
    @GetMapping("{id}")
    public ResponseEntity<List<CatReport>> getReportsByCandidateId(@Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом",
            example = "832615783") @PathVariable Long id) {
        List<CatReport> result = catReportService.getReportByCandidateId(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Список всех отчетов из базы данных отчетов приюта для кошек",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список всех отчетов приюта для кошек",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatReport[].class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Нет ни одного отчета по приюту для кошек"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<CatReport>> getAllReports() {
        List<CatReport> result = catReportService.getAllReports();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Поиск ids отчетов определенного пользователя питомника для кошек за определенную дату",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Перечень id успешно получен",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Long[].class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Проверьте правильность введенного id или дату"
                    )
            })
    @GetMapping("/idsOfReports")
    public ResponseEntity<List<Long>> getIdsOfReports(
            @Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом", example = "832615783") @RequestParam Long id,
            @Parameter(description = "Дата, за которую нужно узнать id отчетов", example = "2022-04-25") @RequestParam String dateS) {
        LocalDate date = LocalDate.parse(dateS);
        List<Long> result = catReportService.idsOfReportsByCandidateIdAndDate(id, date);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Поиск подписи к отчету приюта для кошек по id этого отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Подпись к отчету успешно найдена",
                            content = @Content(
                                    mediaType = MediaType.TEXT_MARKDOWN_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчета с таким id и соотвественно подписи к нему нет в базе "
                    )
            })
    @GetMapping("/caption")
    public ResponseEntity<String> getReportCaption(@Parameter(description = "Id отчета, получаемый из запроса getIdsOfReports", example = "5") @RequestParam Long id) {
        String result = catReportService.getReportCaption(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Поиск в базе данных фотографии из отчета приюта для кошек по id этого отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография была успешно найдена",
                            content = @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE,
                                    schema = @Schema(implementation = byte[].class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчета с таким id и соответственно фото нет в базе данных"
                    )
            })
    @GetMapping(value = "/{id}/report-photo-from-db")
    public ResponseEntity<byte[]> getReport(@Parameter(description = "Id отчета, получаемый из запроса getIdsOfReports", example = "5") @PathVariable Long id) {
        CatReport result = catReportService.findReportByReportId(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(result.getData().length);
        headers.setContentType(MediaType.parseMediaType("image/jpeg"));
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(result.getData());
    }

    @Operation(summary = "Поиск на жестком диске фотографии из отчета приюта для кошек по id этого отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фотография была успешно найдена",
                            content = @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Отчета с таким id и соответственно фото нет на жестком диске"
                    )
            })
    @GetMapping(value = "/{id}/report-photo-from-file")
    public void downloadReport(@Parameter(description = "Id отчета, получаемый из запроса getIdsOfReports", example = "5") @PathVariable Long id,
                               HttpServletResponse response) throws IOException {
        CatReport result = catReportService.findReportByReportId(id);
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

    @Operation(summary = "Удаление всех отчетов пользователя приюта для кошек по id  пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчеты были успешно удалены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "У пользователя с указанным id нет отчетов"
                    )
            })
    @DeleteMapping("/deleteByCandidateId/{id}")
    public ResponseEntity<String> deleteCatReportsByCandidateId(@Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом",
            example = "832615783") @PathVariable Long id) {
        String result = catReportService.deleteReportsByCandidateId(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Удаление всех отчетов приюта для кошек за указанную дату",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчеты были успешно удалены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "За указанную дату нет отчетов"
                    )
            })
    @DeleteMapping("/deleteByDate/{dateS}")
    public ResponseEntity<String> deleteCatReportsByDate(@Parameter(description = "Дата, за которую нужно удалить отчеты", example = "2022-04-25") @PathVariable String dateS) {
        LocalDate date = LocalDate.parse(dateS);
        String result = catReportService.deleteReportsByDate(date);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление всех отчетов конкретного пользователя приюта для кошек за указанную дату",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Отчеты были успешно удалены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "За указанную дату у данного пользователя нет отчетов"
                    )
            })
    @DeleteMapping("/deleteByCandidateIdAndDate")
    public ResponseEntity<String> deleteCatReportsByCandidateIdAndDate(
            @Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом", example = "832615783") @RequestParam("id") Long id,
            @Parameter(description = "Дата, за которую нужно удалить отчеты", example = "2022-04-25") @RequestParam("date") String dateS) {
        LocalDate date = LocalDate.parse(dateS);
        String result = catReportService.deleteReportsByCandidateIdAndDateReport(id, date);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление всех отчетов из базы приюта для кошек",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Все отчеты были успешно удалены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            })
    @DeleteMapping("/clear")
    public ResponseEntity<String> clear() {
        String result = "SUCCESS";
        catReportService.clear();
        return ResponseEntity.ok(result);
    }
}
