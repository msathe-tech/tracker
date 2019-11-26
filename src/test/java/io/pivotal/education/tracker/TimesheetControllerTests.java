package io.pivotal.education.tracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TimesheetControllerTests {
    private TimesheetRepository repository;
    private TimesheetController controller;

    @BeforeEach
    public void setUp() {
        repository = mock(TimesheetRepository.class);
        controller = new TimesheetController(repository);
    }

    @Test
    public void testCreateTimesheet() {
        Timesheet timesheetToCreate =
                new Timesheet(2L,
                        3L,
                        LocalDate.of(2019,11,28),
                        6);

        Timesheet timesheetSaved =
                new Timesheet(1L,
                        2L,
                        3L,
                        LocalDate.of(2019,11,28),
                        6);

        doReturn(timesheetSaved)
                .when(repository)
                .save(timesheetToCreate);

        ResponseEntity<Timesheet> timesheetResponseEntity =
                controller.create(timesheetToCreate);

        verify(repository)
                .save(timesheetToCreate);

        assertThat(timesheetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(timesheetResponseEntity.getBody()).isEqualTo(timesheetSaved);
    }

}
