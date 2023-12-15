package com.unicap.idear.idear.controllers;

import com.unicap.idear.idear.dtos.*;
import com.unicap.idear.idear.models.RoomModel;
import com.unicap.idear.idear.models.TeamModel;
import com.unicap.idear.idear.repositories.RoomRepository;
import com.unicap.idear.idear.services.ChatGPTService;
import com.unicap.idear.idear.services.RoomService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("rooms")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    private ChatGPTService chatGPTService;

    @Autowired
    private RoomRepository repository;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/")
    @Transactional
    public ResponseEntity createRoom(@RequestBody @Valid RoomCreationDto roomCreationDto, UriComponentsBuilder uriComponentsBuilder, Authentication authentication) {

        String username = authentication.getName();

        Long accessCode = roomService.generateRandomAccessCode();

        RoomModel savedRoom = roomService.saveRoom(roomCreationDto, username, accessCode);

        var uri = uriComponentsBuilder.path("rooms/{accessCode}").buildAndExpand(savedRoom.getAccessCode()).toUri();

        return ResponseEntity.created(uri).body(savedRoom);
    }

    @PostMapping("/{accessCode}")
    @Transactional
    public ResponseEntity saveSolution(@PathVariable(value = "accessCode") Long accessCode, @RequestBody @Valid IdeaSelectedDto ideaSelectedDto, UriComponentsBuilder uriComponentsBuilder) {
        var idea = roomService.saveSolutionInRoom(accessCode, ideaSelectedDto);
        var room = roomService.getRoomByAccessCode(accessCode).get();

        if (idea != null) {
            var reponseGpt = chatGPTService.getChatGPTResponse(room.getProblemModel().getProblemTitle(), room.getProblemModel().getProblemDescription(), idea.getIdeaContent());

            room.setSolution(reponseGpt);

            var uri = uriComponentsBuilder.path("rooms/{accessCode}").buildAndExpand(accessCode).toUri();
            return ResponseEntity.created(uri).body(room.getSolution());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Idea not found with this ID " + ideaSelectedDto.idIdea());
        }
    }

    @PutMapping("/{accessCode}")
    @Transactional
    public ResponseEntity addUserToRoom(@PathVariable(value = "accessCode") Long accessCode, @RequestBody @Valid RoomUserToRoomDto roomUserToRoomDto) {

        Optional<RoomModel> roomWithNewUser = roomService.addUserToRoom(roomUserToRoomDto, accessCode);

        if (roomWithNewUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(roomWithNewUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Room or username not found, the specified access code does not exist.");
        }
    }

    @PutMapping("/addIdea/{accessCode}")
    @Transactional
    public ResponseEntity createIdea(@PathVariable(value = "accessCode") Long accessCode, @RequestBody @Valid IdeaDto ideaDto, Authentication authentication) {

        String username = authentication.getName();

        Optional<RoomModel> roomWithIdea = roomService.saveIdeaInRoom(ideaDto, username, accessCode);

        if (roomWithIdea.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(roomWithIdea.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Room not found, the specified access code does not exist.");
        }
    }

    @PutMapping("/edit/{accessCode}")
    @Transactional
    public ResponseEntity editRoom(@PathVariable(value = "accessCode") Long accessCode, @RequestBody @Valid RoomUpdateDto roomUpdateDto) {

        Optional<RoomModel> updatedRoom = roomService.updateRoom(accessCode, roomUpdateDto);

        if (updatedRoom.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedRoom.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Room not found, the specified access code does not exist.");
        }
    }

    @PutMapping("/addMethod/{accessCode}")
    @Transactional
    public ResponseEntity addMethodInRoom(@PathVariable(value = "accessCode") Long accessCode, @RequestBody @Valid RoomInsertMethodDto roomInsertMethodDto) {

        Optional<RoomModel> roomWithMethod = roomService.choosingMethodInRoom(accessCode, roomInsertMethodDto.idMethod());

        if (roomWithMethod.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(roomWithMethod.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Room or method not found, the specified access code does not exist.");
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllRoomsByUsername(Authentication authentication) {

        String username = authentication.getName();

        List<TeamModel> teamsList = roomService.getAllRoomsByUsername(username);

        List<RoomModel> rooms = new ArrayList<>();

        if (!teamsList.isEmpty()) {
            for (TeamModel team : teamsList) {
                var room = team.getRoomModel();
                rooms.add(room);
                Long accessCode = room.getAccessCode();
                room.add(linkTo(methodOn(RoomController.class).getOneRoom(accessCode)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }

    @GetMapping("/{accessCode}")
    public ResponseEntity<Object> getOneRoom(@PathVariable(value = "accessCode") Long accessCode) {

        Optional<RoomModel> room = roomService.getRoomByAccessCode(accessCode);

        if (room.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found, the specified access code or access code is invalid.");
        }

        room.get().add(linkTo(methodOn(RoomController.class).getAllRooms()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(room.get());
    }

    @GetMapping("/allRooms")
    public ResponseEntity<Object> getAllRooms() {
        List<RoomModel> roomsList = roomService.getAllRooms();

        if (!roomsList.isEmpty()) {
            for (RoomModel room : roomsList) {
                Long accessCode = room.getAccessCode();
                room.add(linkTo(methodOn(RoomController.class).getOneRoom(accessCode)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(roomsList);
    }

    @DeleteMapping("/{accessCode}")
    @Transactional
    public ResponseEntity<Object> deleteRoom(@PathVariable(value = "accessCode") Long accessCode) {
        boolean deleted = roomService.deleteRoom(accessCode);

        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Room deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Room not found, the specified access code does not exist.");
    }
}
