package org.oclick.libs.spi.jobboard;

import org.oclick.libs.shared.dto.ResumeSummary;

import java.util.List;

public interface JobboardProvider {
    String getAlias();

    List<ResumeSummary> getResumes(String accessToken, String... additionKeys);
}