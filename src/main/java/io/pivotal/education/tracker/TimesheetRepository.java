package io.pivotal.education.tracker;

import org.springframework.data.repository.CrudRepository;

public interface TimesheetRepository extends CrudRepository<Timesheet,Long> {}
