package tgbots.shelterbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tgbots.shelterbot.constants.Verification;
import tgbots.shelterbot.models.CatCandidate;
import tgbots.shelterbot.service.by_models.CandidateService;

import java.util.List;

@RestController
@RequestMapping("/catCandidate")
public class CatCandidateController {

    private final CandidateService<CatCandidate> catCandidateImpl;

    public CatCandidateController(@Qualifier("catCandidateImpl") CandidateService<CatCandidate> catCandidateImpl) {
        this.catCandidateImpl = catCandidateImpl;
    }


    @Operation(summary = "Поиск пользователя приюта для кошек по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatCandidate.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя с таким id нет в базе данных"
                    )
            })
    @GetMapping("{id}")
    public ResponseEntity<CatCandidate> getCatCandidateById(@Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом",
            example = "832615783") @PathVariable Long id) {
        CatCandidate catCandidate = catCandidateImpl.getCandidateById(id);
        if (catCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catCandidate);
    }

    @Operation(summary = "Поиск пользователя приюта для кошек по его Telegram нику",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatCandidate.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя с таким Telegram ником нет в базе данных"
                    )
            })
    @GetMapping("/userName{userName}")
    public ResponseEntity<CatCandidate> getCatCandidateByUserName(@Parameter(description = "Telegram ник пользователя без символа @",
            example = "alex91") @PathVariable String userName) {
        CatCandidate catCandidate = catCandidateImpl.getCandidateByUserName(userName);
        if (catCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(catCandidate);
    }

    @Operation(summary = "Редактирование данных пользователя приюта для кошек",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updating data cat shelter's user was realized successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatCandidate.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Проверьте правильность введенного id или номера телефона"
                    )
            })
    @PutMapping
    public ResponseEntity<CatCandidate> updateCatCandidate(
            @Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом. Поле обязательно для заполнения", example = "832615783") @RequestParam Long id,
            @Parameter(description = "Имя пользователя", example = "Егор Малов") @RequestParam(required = false) String name,
            @Parameter(description = "Telegram ник пользователя без символа @", example = "alex91") @RequestParam(required = false) String userName,
            @Parameter(description = "Номер телефона пользователя", example = "+79863251224") @RequestParam(required = false) String phoneNumber) {
        CatCandidate catCandidate = new CatCandidate();
        catCandidate.setId(id);
        if (name != null) {
            catCandidate.setName(name);
        }
        if (userName != null) {
            catCandidate.setUserName(userName);
        }
        if (phoneNumber != null && Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            catCandidate.setPhoneNumber(phoneNumber);
        } else if (phoneNumber != null && !Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        CatCandidate result = catCandidateImpl.updateCandidate(catCandidate);
        if (result == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление пользователя приюта для кошек по его id из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление пользователя было успешно проведено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя с таким id нет в базе данных"
                    )
            })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCatCandidateById(@Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом",
            example = "832615783") @PathVariable Long id) {
        String result = catCandidateImpl.deleteCandidateById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление пользователя приюта для кошек по его Telegram нику",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаление пользователя было успешно проведено",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя с таким Telegram ником нет в базе данных"
                    )
            })
    @DeleteMapping("/userNameDelete")
    public ResponseEntity<String> deleteCatCandidateByUserName(@Parameter(description = "Telegram ник пользователя без символа @",
            example = "alex91") @RequestParam String userName) {
        String result = catCandidateImpl.deleteCandidateByUserName(userName);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Полный перечень пользователей приюта для кошек",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Full list of cat shelter's users",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CatCandidate[].class)
                            )
                    )
            })
    @GetMapping
    public ResponseEntity<List<CatCandidate>> allCatCandidates() {
        List<CatCandidate> result = catCandidateImpl.allCandidates();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление из базы данных всех пользователей приюта для кошек",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleting all cat shelter's users from data base was realized successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            })
    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        String result = "OK";
        catCandidateImpl.clear();
        return ResponseEntity.ok(result);
    }
}
