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
import tgbots.shelterbot.models.DogCandidate;
import tgbots.shelterbot.service.by_models.CandidateService;

import java.util.List;

@RestController
@RequestMapping("/dogCandidate")
public class DogCandidateController {

    private final CandidateService<DogCandidate> dogCandidateImpl;

    public DogCandidateController(@Qualifier("dogCandidateImpl") CandidateService<DogCandidate> dogCandidateImpl) {
        this.dogCandidateImpl = dogCandidateImpl;
    }

    @Operation(summary = "Поиск пользователя приюта для собак по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogCandidate.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя с таким id нет в базе данных"
                    )
            })
    @GetMapping("{id}")
    public ResponseEntity<DogCandidate> getDogCandidateById(@Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом",
            example = "832615783") @PathVariable Long id) {
        DogCandidate dogCandidate = dogCandidateImpl.getCandidateById(id);
        if (dogCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dogCandidate);
    }

    @Operation(summary = "Поиск пользователя приюта для собак по его Telegram нику",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogCandidate.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователя с таким Telegram ником нет в базе данных"
                    )
            })
    @GetMapping("/userName{userName}")
    public ResponseEntity<DogCandidate> getDogCandidateByUserName(@Parameter(description = "Telegram ник пользователя без символа @",
            example = "alex91") @PathVariable String userName) {
        DogCandidate dogCandidate = dogCandidateImpl.getCandidateByUserName(userName);
        if (dogCandidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(dogCandidate);
    }

    @Operation(summary = "Редактирование данных пользователя приюта для собак",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updating data dog shelter's user was realized successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogCandidate.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Проверьте правильность введенного id или номера телефона"
                    )
            })
    @PutMapping
    public ResponseEntity<DogCandidate> updateDogCandidate(
            @Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом. Поле обязательно для заполнения", example = "832615783") @RequestParam Long id,
            @Parameter(description = "Имя пользователя", example = "Егор Малов") @RequestParam(required = false) String name,
            @Parameter(description = "Telegram ник пользователя без символа @", example = "alex91") @RequestParam(required = false) String userName,
            @Parameter(description = "Номер телефона пользователя", example = "+79863251224") @RequestParam(required = false) String phoneNumber) {
        DogCandidate dogCandidate = new DogCandidate();
        dogCandidate.setId(id);
        if (name != null) {
            dogCandidate.setName(name);
        }
        if (userName != null) {
            dogCandidate.setUserName(userName);
        }
        if (phoneNumber != null && Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            dogCandidate.setPhoneNumber(phoneNumber);
        } else if (phoneNumber != null && !Verification.CHECK_PHONE_NUMBER.matcher(phoneNumber).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        DogCandidate result = dogCandidateImpl.updateCandidate(dogCandidate);
        if (result == null) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление пользователя приюта для собак по его id из базы данных",
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
    public ResponseEntity<String> deleteDogCandidateById(@Parameter(description = "Id пользователя, соответсвующий id чата между пользователем и ботом",
            example = "832615783") @PathVariable Long id) {
        String result = dogCandidateImpl.deleteCandidateById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление пользователя приюта для собак по его Telegram нику",
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
    @DeleteMapping("/userName")
    public ResponseEntity<String> deleteDogCandidateByUserName(@Parameter(description = "Telegram ник пользователя без символа @",
            example = "alex91") @RequestParam String userName) {
        String result = dogCandidateImpl.deleteCandidateByUserName(userName);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Полный перечень пользователей приюта для собак",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Full list of dog shelter's users",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = DogCandidate[].class)
                            )
                    )
            })
    @GetMapping
    public ResponseEntity<List<DogCandidate>> allDogCandidates() {
        List<DogCandidate> result = dogCandidateImpl.allCandidates();
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "Удаление из базы данных всех пользователей приюта для собак",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleting all dog shelter's users from data base was realized successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            })
    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        String result = "OK";
        dogCandidateImpl.clear();
        return ResponseEntity.ok(result);
    }
}
