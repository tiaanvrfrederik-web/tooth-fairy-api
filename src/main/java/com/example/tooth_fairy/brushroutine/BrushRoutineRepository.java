package com.example.tooth_fairy.brushroutine;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrushRoutineRepository extends JpaRepository<BrushRoutine, Long> {
    List<BrushRoutine> findByUser_IdOrderByPersonNameAsc(Long userId);

    Optional<BrushRoutine> findByIdAndUser_Id(Long id, Long userId);
}
