package org.oclick.providers.jobboards.hh;

import org.oclick.libs.shared.dto.ResumeSummary;
import org.oclick.libs.spi.jobboard.JobboardProvider;
import org.pf4j.Extension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Extension
public class HhProvider implements JobboardProvider {
    @Override
    public String getAlias() {
        return "hh";
    }

    @Override
    public List<ResumeSummary> getResumes(String accessToken, String... additionKeys) {
        return List.of(
                new ResumeSummary(UUID.randomUUID(), "HH12345", "hh", "Java Developer", "...", Instant.now(), true)
        );
    }
}