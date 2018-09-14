package io.pivotal.cnde.portal.spike.cohort;

import io.pivotal.cnde.portal.spike.JobId;

import java.util.Objects;

public class ProvisionCohortCommand {

    private final JobId jobId;
    private final String cohortName;
    private final String repositoryName;
    private final String caddyNotes;
    private final String slackChannelName;

    public ProvisionCohortCommand(JobId jobId, String cohortName, String repositoryName, String caddyNotes, String slackChannelName) {
        this.jobId = jobId;
        this.cohortName = cohortName;
        this.repositoryName = repositoryName;
        this.caddyNotes = caddyNotes;
        this.slackChannelName = slackChannelName;
    }

    public JobId getJobId() {
        return jobId;
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
        ProvisionCohortCommand that = (ProvisionCohortCommand) o;
        return Objects.equals(jobId, that.jobId) &&
                Objects.equals(cohortName, that.cohortName) &&
                Objects.equals(repositoryName, that.repositoryName) &&
                Objects.equals(caddyNotes, that.caddyNotes) &&
                Objects.equals(slackChannelName, that.slackChannelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, cohortName, repositoryName, caddyNotes, slackChannelName);
    }

    @Override
    public String toString() {
        return "ProvisionCohortCommand{" +
                "jobId=" + jobId +
                ", cohortName='" + cohortName + '\'' +
                ", repositoryName='" + repositoryName + '\'' +
                ", caddyNotes='" + caddyNotes + '\'' +
                ", slackChannelName='" + slackChannelName + '\'' +
                '}';
    }

}
