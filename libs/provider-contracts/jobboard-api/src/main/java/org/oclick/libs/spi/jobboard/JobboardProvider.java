package org.oclick.libs.spi.jobboard;

import org.oclick.libs.shared.dto.ResumeSummary;
import org.pf4j.ExtensionPoint;

import java.util.List;

public interface JobboardProvider extends ExtensionPoint {
    String getAlias();

    List<ResumeSummary> getResumes(String accessToken, String... additionKeys);
}