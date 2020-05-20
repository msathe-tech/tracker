package io.pivotal.education.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/timesheets")
public class TimesheetController {
    private final TimesheetRepository repository;

    public TimesheetController(TimesheetRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Timesheet> create(@RequestBody Timesheet timesheetToCreate) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repository.save(timesheetToCreate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Timesheet> findById(@PathVariable long id) {
        return repository.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Timesheet> update(@PathVariable long id,
                                            @RequestBody Timesheet timesheetToUpdate) {
        if (repository.findById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(repository.save(new Timesheet(id,
                            timesheetToUpdate.getProjectId(),
                            timesheetToUpdate.getUserId(),
                            timesheetToUpdate.getDate(),
                            timesheetToUpdate.getHours())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
