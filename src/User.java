import java.util.UUID;

public interface User {
    public UUID uuid = null;

    public void showStats();

    public UUID getUuid();

    public void setUuid(UUID id);
}
