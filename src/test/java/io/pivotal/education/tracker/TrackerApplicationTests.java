package io.pivotal.education.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrackerApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCreateTimesheet() {
        Timesheet timesheetToCreate =
                new Timesheet(2L,
                        3L,
                        LocalDate.of(2019,11,28),
                        6);

        ResponseEntity<Timesheet> timesheetResponseEntity =
                restTemplate.postForEntity("/timesheets", timesheetToCreate, Timesheet.class);

        assertThat(timesheetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(timesheetResponseEntity.getBody()).isNotNull();

        Timesheet timesheetSaved = timesheetResponseEntity.getBody();

        assertThat(timesheetSaved.getId()).isGreaterThan(0L);
        assertThat(timesheetSaved.getProjectId()).isEqualTo(timesheetToCreate.getProjectId());
        assertThat(timesheetSaved.getUserId()).isEqualTo(timesheetToCreate.getUserId());
        assertThat(timesheetSaved.getDate()).isEqualTo(timesheetToCreate.getDate());
        assertThat(timesheetSaved.getHours()).isEqualTo(timesheetToCreate.getHours());
    }

    @Test
    public void testFindTimesheet() {
        Timesheet timesheetCreated = createTimesheet(
                new Timesheet(
                        22L,
                        33L,
                        LocalDate.of(2019,11,28),
                        6
                )
        );

        ResponseEntity<Timesheet> timesheetResponseEntity =
                restTemplate.getForEntity("/timesheets/" + timesheetCreated.getId(),
                        Timesheet.class);

        assertThat(timesheetResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(timesheetResponseEntity.getBody()).isEqualTo(timesheetCreated);
    }

    private Timesheet createTimesheet(Timesheet timesheetToCreate) {
        return restTemplate
                .postForEntity("/timesheets", timesheetToCreate, Timesheet.class)
                .getBody();
    }
}
