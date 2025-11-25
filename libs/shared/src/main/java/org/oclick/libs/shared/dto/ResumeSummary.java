package org.oclick.libs.shared.dto;

import java.time.Instant;
import java.util.UUID;

public record ResumeSummary(
        UUID id,
        String externalId,
        String providerAlias,
        String title,
        String url,
        Instant lastUpdated,
        boolean isPublished
) {
}
