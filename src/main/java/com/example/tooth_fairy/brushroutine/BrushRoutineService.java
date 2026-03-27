package com.example.tooth_fairy.brushroutine;

import com.example.tooth_fairy.brushroutine.dto.BrushRoutineRequest;
import com.example.tooth_fairy.brushroutine.dto.BrushRoutineResponse;
import com.example.tooth_fairy.user.User;
import com.example.tooth_fairy.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BrushRoutineService {
    private final BrushRoutineRepository brushRoutineRepository;
    private final UserRepository userRepository;

    public BrushRoutineService(BrushRoutineRepository brushRoutineRepository, UserRepository userRepository) {
        this.brushRoutineRepository = brushRoutineRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<BrushRoutineResponse> listForUser(Long userId) {
        return brushRoutineRepository.findByUser_IdOrderByPersonNameAsc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BrushRoutineResponse getForUser(Long routineId, Long userId) {
        BrushRoutine routine = brushRoutineRepository.findByIdAndUser_Id(routineId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return toResponse(routine);
    }

    @Transactional
    public BrushRoutineResponse create(Long userId, BrushRoutineRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        LocalDateTime now = LocalDateTime.now();
        BrushRoutine routine = new BrushRoutine();
        routine.setUser(user);
        applyRequest(routine, request, now);
        if (request.getActive() != null) {
            routine.setActive(request.getActive());
        }

        brushRoutineRepository.save(routine);
        return toResponse(routine);
    }

    @Transactional
    public BrushRoutineResponse update(Long routineId, Long userId, BrushRoutineRequest request) {
        BrushRoutine routine = brushRoutineRepository.findByIdAndUser_Id(routineId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        applyRequest(routine, request, LocalDateTime.now());
        if (request.getActive() != null) {
            routine.setActive(request.getActive());
        }

        return toResponse(routine);
    }

    @Transactional
    public void delete(Long routineId, Long userId) {
        BrushRoutine routine = brushRoutineRepository.findByIdAndUser_Id(routineId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        brushRoutineRepository.delete(routine);
    }

    private void applyRequest(BrushRoutine routine, BrushRoutineRequest request, LocalDateTime now) {
        routine.setPersonName(request.getPersonName());
        routine.setTimesPerDay(request.getTimesPerDay());
        routine.setMorningTime(request.getMorningTime());
        routine.setEveningTime(request.getEveningTime());
        routine.setNotes(request.getNotes());
        if (routine.getCreatedAt() == null) {
            routine.setCreatedAt(now);
        }
        routine.setUpdatedAt(now);
    }

    private BrushRoutineResponse toResponse(BrushRoutine routine) {
        BrushRoutineResponse r = new BrushRoutineResponse();
        r.setId(routine.getId());
        r.setPersonName(routine.getPersonName());
        r.setTimesPerDay(routine.getTimesPerDay());
        r.setMorningTime(routine.getMorningTime());
        r.setEveningTime(routine.getEveningTime());
        r.setNotes(routine.getNotes());
        r.setActive(routine.isActive());
        r.setCreatedAt(routine.getCreatedAt());
        r.setUpdatedAt(routine.getUpdatedAt());
        return r;
    }
}
