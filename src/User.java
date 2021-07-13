import java.util.UUID;

public interface User {
    public UUID uuid = null;

    public void showStats(int _userID);

    public UUID getUuid();

    public void setUuid(UUID id);
}
