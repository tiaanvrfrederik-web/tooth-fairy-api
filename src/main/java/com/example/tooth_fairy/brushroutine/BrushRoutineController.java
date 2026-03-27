package com.example.tooth_fairy.brushroutine;

import com.example.tooth_fairy.brushroutine.dto.BrushRoutineRequest;
import com.example.tooth_fairy.brushroutine.dto.BrushRoutineResponse;
import com.example.tooth_fairy.security.UserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brush-routines")
public class BrushRoutineController {
    private final BrushRoutineService brushRoutineService;

    public BrushRoutineController(BrushRoutineService brushRoutineService) {
        this.brushRoutineService = brushRoutineService;
    }

    @GetMapping
    public List<BrushRoutineResponse> list(@AuthenticationPrincipal UserPrincipal principal) {
        return brushRoutineService.listForUser(principal.getUser().getId());
    }

    @GetMapping("/{id}")
    public BrushRoutineResponse get(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        return brushRoutineService.getForUser(id, principal.getUser().getId());
    }

    @PostMapping
    public ResponseEntity<BrushRoutineResponse> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BrushRoutineRequest request
    ) {
        BrushRoutineResponse created = brushRoutineService.create(principal.getUser().getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public BrushRoutineResponse update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody BrushRoutineRequest request
    ) {
        return brushRoutineService.update(id, principal.getUser().getId(), request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id
    ) {
        brushRoutineService.delete(id, principal.getUser().getId());
        return ResponseEntity.noContent().build();
    }
}
