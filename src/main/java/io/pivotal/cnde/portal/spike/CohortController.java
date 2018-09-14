package io.pivotal.cnde.portal.spike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CohortController {

    private final JobIdGenerator jobIdGenerator;
    private final CohortCommandHandler cohortCommandHandler;

    public CohortController(JobIdGenerator jobIdGenerator, CohortCommandHandler cohortCommandHandler) {
        this.jobIdGenerator = jobIdGenerator;
        this.cohortCommandHandler = cohortCommandHandler;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/cohorts")
    public void provisionCohort(@RequestBody ProvisionCohortDto requestDto) {

        JobId jobId = jobIdGenerator.nextId();

        ProvisionCohortCommand command = new ProvisionCohortCommand(
                jobId,
                requestDto.getCohortName(),
                requestDto.getRepositoryName(),
                requestDto.getCaddyNotes(),
                requestDto.getSlackChannelName()
        );

        cohortCommandHandler.provisionCohort(command);
    }

    static class ProvisionCohortDto {

        private final String cohortName;
        private final String repositoryName;
        private final String caddyNotes;
        private final String slackChannelName;

        @JsonCreator
        public ProvisionCohortDto(@JsonProperty("cohortName") String cohortName,
                                  @JsonProperty("repositoryName") String repositoryName,
                                  @JsonProperty("caddyNotes") String caddyNotes,
                                  @JsonProperty("slackChannelName") String slackChannelName) {
            this.cohortName = cohortName;
            this.repositoryName = repositoryName;
            this.caddyNotes = caddyNotes;
            this.slackChannelName = slackChannelName;
        }

        public String getCohortName() {
            return cohortName;
        }

        public String getRepositoryName() {
            return repositoryName;
        }

        public String getCaddyNotes() {
            return caddyNotes;
        }

        public String getSlackChannelName() {
            return slackChannelName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProvisionCohortDto that = (ProvisionCohortDto) o;
            return Objects.equals(cohortName, that.cohortName) &&
                    Objects.equals(repositoryName, that.repositoryName) &&
                    Objects.equals(caddyNotes, that.caddyNotes) &&
                    Objects.equals(slackChannelName, that.slackChannelName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cohortName, repositoryName, caddyNotes, slackChannelName);
        }

        @Override
        public String toString() {
            return "ProvisionCohortDto{" +
                    "cohortName='" + cohortName + '\'' +
                    ", repositoryName='" + repositoryName + '\'' +
                    ", caddyNotes='" + caddyNotes + '\'' +
                    ", slackChannelName='" + slackChannelName + '\'' +
                    '}';
        }
    }

}
