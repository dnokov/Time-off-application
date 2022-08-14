package academy.scalefocus.timeOffManagement.controller;

import academy.scalefocus.timeOffManagement.dto.TimeOffCreationDTO;
import academy.scalefocus.timeOffManagement.dto.TimeOffResponseDTO;
import academy.scalefocus.timeOffManagement.dto.TimeOffUpdateDTO;
import academy.scalefocus.timeOffManagement.security.UserPrincipal;
import academy.scalefocus.timeOffManagement.service.TimeOffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/timeOffs")
@Api(tags = "Time offs")
public class TimeOffController {

    @Autowired
    private TimeOffService timeOffService;

    @SneakyThrows
    @PostMapping
    @ApiOperation("Create a new time off")
    public ResponseEntity<?> addTimeOff(
            UriComponentsBuilder ucBuilder,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TimeOffCreationDTO timeOffCreationDTO) {

        TimeOffResponseDTO savedTimeOffDTO = timeOffService
                .addTimeOff(userPrincipal, timeOffCreationDTO);
        return ResponseEntity.created(
                ucBuilder.path("/timeOffs/{timeOffId}").buildAndExpand(savedTimeOffDTO.getId()).toUri())
                .build();
    }

    @GetMapping("{id}")
    @ApiOperation("Get a specific time off")
    public ResponseEntity<?> getTimeOff(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long id) {

        TimeOffResponseDTO timeOffResponseDTO = timeOffService.getTimeOff(userPrincipal, id);
        return new ResponseEntity<>(timeOffResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a time off")
    public ResponseEntity<?> deleteTimeOffRequest(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long timeOffId) {
        timeOffService.deleteTimeOffRequest(userPrincipal, timeOffId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("Modify the information for a time off")
    public ResponseEntity<?> updateTimeOff(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long timeOffId, @Valid @RequestBody TimeOffUpdateDTO timeOffUpdateDTO) {
        return new ResponseEntity<>(timeOffService.updateTimeOff
                (userPrincipal, timeOffId, timeOffUpdateDTO), HttpStatus.OK);
    }

    @PatchMapping("/send/{timeOffId}")
    @ApiOperation("Send email for approving a time off")
    public ResponseEntity<?> sendTimeOff(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @PathVariable Long timeOffId) {

        timeOffService.sendTimeOff(userPrincipal, timeOffId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{timeOffId}/")
    @ApiOperation("Approve a time off request")
    public ResponseEntity<?> approveTimeOff(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                            @PathVariable Long timeOffId, @RequestParam("value") Boolean answer) {

        timeOffService.approveTimeOffRequest(userPrincipal, timeOffId, answer);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
