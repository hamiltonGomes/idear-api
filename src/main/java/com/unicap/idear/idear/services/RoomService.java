package com.unicap.idear.idear.services;

import com.unicap.idear.idear.dtos.*;
import com.unicap.idear.idear.models.*;
import com.unicap.idear.idear.repositories.RoomRepository;
import com.unicap.idear.idear.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MethodService methodService;

    @Autowired
    public RoomService(RoomRepository roomRepository, UserRepository userRepository, MethodService methodService) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;

        this.methodService = methodService;
    }

    public RoomModel saveRoom(RoomCreationDto roomCreationDto, String username, Long accessCode) {
        var user = (UserModel) userRepository.findByUsername(username);

        List<UserModel> participantsList = new ArrayList<>();
        participantsList.add(user);

        var team = new TeamModel(roomCreationDto.roomName(), participantsList);

        var problem = new ProblemModel(roomCreationDto.problemDataDto());

        var roomModel = new RoomModel(roomCreationDto.roomName(), problem, team, accessCode);

        return roomRepository.save(roomModel);
    }

    public IdeaModel saveSolutionInRoom(Long accessCode, IdeaSelectedDto ideaSelectedDto) {
        Optional<RoomModel> optionalRoom = roomRepository.findByAccessCode(accessCode);
        Long idIdea = ideaSelectedDto.idIdea();

        if (optionalRoom.isPresent()) {
            RoomModel roomModel = optionalRoom.get();
            List<IdeaModel> ideaList = roomModel.getIdeas();

            if (!ideaList.isEmpty()) {
                for (IdeaModel idea : ideaList) {
                    if (idIdea.equals(idea.getIdIdea())) {
                        return idea;
                    }
                }
            }
        }
        return null;
    }

    public Optional<RoomModel> saveIdeaInRoom(IdeaDto ideaDto, String username, Long accessCode) {
        Optional<RoomModel> optionalRoom = roomRepository.findByAccessCode(accessCode);
        RoomModel roomModel = optionalRoom.get();
        var user = (UserModel) userRepository.findByUsername(username);

        var idea = new IdeaModel(ideaDto.ideaContent(), user, roomModel);

        roomModel.getIdeas().add(idea);
        return Optional.of(roomRepository.save(roomModel));
    }

    public Optional<RoomModel> choosingMethodInRoom(Long accessCode, long idMethod) {
        Optional<MethodModel> methodOptional = methodService.getOneMethod(idMethod);

        if (methodOptional.isPresent()) {
            MethodModel selectedMethod = methodOptional.get();

            Optional<RoomModel> optionalRoom = roomRepository.findByAccessCode(accessCode);

            if (optionalRoom.isPresent()) {
                RoomModel roomModel = optionalRoom.get();
                roomModel.setMethodModel(selectedMethod);
                return Optional.of(roomRepository.save(roomModel));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    public Optional<RoomModel> addUserToRoom(RoomUserToRoomDto roomUserToRoomDto, Long accessCode) {

        Optional<RoomModel> optionalRoom = roomRepository.findByAccessCode(accessCode);
        Optional<UserModel> optionalUser = Optional.ofNullable((UserModel) userRepository.findByUsername(roomUserToRoomDto.username()));

        if (optionalRoom.isPresent() && optionalUser.isPresent()) {
            var room = optionalRoom.get();
            var user = optionalUser.get();
            var participants = room.getTeamModel().getParticipants();

            if (!participants.contains(user)) {
                participants.add(user);
                return Optional.of(roomRepository.save(room));
            }
        }
        return Optional.empty();
    }

    public Long generateRandomAccessCode() {
        Long accessCode;
        do {
            accessCode = (long) (Math.random() * 900000L) + 100000L;
        } while (roomRepository.existsByAccessCode(accessCode));

        return accessCode;
    }

    public Optional<RoomModel> updateRoom(Long accessCode, RoomUpdateDto roomUpdateDto) {
        Optional<RoomModel> optionalRoom = roomRepository.findByAccessCode(accessCode);

        if (optionalRoom.isPresent()) {
            var roomModel = optionalRoom.get();

            if (roomUpdateDto.roomName() != null) {
                roomModel.setRoomName(roomUpdateDto.roomName());
                roomModel.getTeamModel().setTeamName(roomUpdateDto.roomName());
            }
            if (roomUpdateDto.problemDataDto() != null) {
                roomModel.getProblemModel().setProblemDescription(roomUpdateDto.problemDataDto().problemDescription());
                roomModel.getProblemModel().setProblemTitle(roomUpdateDto.problemDataDto().problemTitle());
            }
            return Optional.of(roomRepository.save(roomModel));
        }
        return Optional.empty();
    }

    public Optional<RoomModel> getRoomByAccessCode(Long accessCode) {
        return roomRepository.findByAccessCode(accessCode);
    }

    public List<RoomModel> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<TeamModel> getAllRoomsByUsername(String username) {
        var user = (UserModel) userRepository.findByUsername(username);

        return user.getListTeams();
    }

    public boolean deleteRoom(Long accessCode) {
        Optional<RoomModel> optionalRoom = roomRepository.findByAccessCode(accessCode);

        if (optionalRoom.isPresent()) {
            roomRepository.delete(optionalRoom.get());
            return true;
        }
        return false;
    }
}
