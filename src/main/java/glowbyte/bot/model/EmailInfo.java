package glowbyte.bot.model;

import lombok.Data;

@Data
public class EmailInfo {
    private final Integer incidentNumber;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String clusterName;
    private final String incidentPriority;
    private final String incidentDescription;
}
