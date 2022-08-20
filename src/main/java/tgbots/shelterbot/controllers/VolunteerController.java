package tgbots.shelterbot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tgbots.shelterbot.models.Volunteer;
import tgbots.shelterbot.service.by_models.VolunteerService;

import java.util.List;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @Operation(summary = "Поиск волонтера приюта по его id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтер успешно найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Волонтера с таким id нет в базе данных"
                    )
            })
    @GetMapping("{id}")
    public ResponseEntity<Volunteer> getVolunteer(@Parameter(description = "Id волонтера, соответсвующий id чата между волонтером и ботом",
            example = "832615783") @PathVariable Long id) {
        Volunteer foundVolunteer = volunteerService.getVolunteer(id);
        if (foundVolunteer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(foundVolunteer);
    }

    @Operation(summary = "Поиск волонтера приюта по его имени",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтер успешно найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Волонтера с таким именем нет в базе данных"
                    )
            })
    @GetMapping("/name{name}")
    public ResponseEntity<Volunteer> getVolunteerByName(@Parameter(description = "Имя волонтера", example = "Роман") @PathVariable String name) {
        Volunteer result = volunteerService.getVolunteer(name);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Редактирование данных волонтера",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updating volunteer's data was realized successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Проверьте правильность введенного id"
                    )
            })
    @PutMapping
    public ResponseEntity<Volunteer> updateVolunteer(
            @Parameter(description = "Id волонтера, соответсвующий id чата между волонтером и ботом. Поле обязательно для заполнения", example = "832615783") @RequestParam Long id,
            @Parameter(description = "Имя волонтера", example = "Роман") @RequestParam(required = false) String name,
            @Parameter(description = "Telegram ник волонтера без символа @", example = "alex91") @RequestParam(required = false) String userName) {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(id);
        if (name != null) {
            volunteer.setName(name);
        }
        if (userName != null) {
            volunteer.setUserName(userName);
        }
        Volunteer result = volunteerService.updateVolunteer(volunteer);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление волонтера приюта по его id из базы данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтер успешно удален",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Волонтера с таким id нет в базе данных"
                    )
            })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVolunteer(@Parameter(description = "Id волонтера, соответсвующий id чата между волонтером и ботом",
            example = "832615783") @PathVariable Long id) {
        String result = volunteerService.deleteVolunteer(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Удаление волонтера приюта по его Telegram нику",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Волонтер успешно удален",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Волонтера с таким Telegram ником нет в базе данных"
                    )
            })
    @DeleteMapping("/userNameDelete")
    public ResponseEntity<String> deleteVolunteerByUserName(@Parameter(description = "Telegram ник пользователя без символа @", example = "alex91") @RequestParam("userName") String userName) {
        String result = volunteerService.deleteVolunteer(userName);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Полный перечень волонтеров приюта",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Full list volunteers of shelter",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer[].class)
                            )
                    )
            })
    @GetMapping
    public ResponseEntity<List<Volunteer>> getAllVolunteers() {
        List<Volunteer> result = volunteerService.allVolunteers();
        if (result == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);
    }
}
