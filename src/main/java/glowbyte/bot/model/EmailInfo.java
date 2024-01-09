package glowbyte.bot.model;

public record EmailInfo(Integer incidentNumber, String customerName, String name, String email, String phoneNumber,
                        String clusterName, String incidentPriority, String incidentDescription) {
}
