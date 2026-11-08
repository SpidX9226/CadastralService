package io.github.spidx.model;

import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.time.OffsetDateTime;

public class Request extends Model {
    private UUID _citizenId;
    private Status _status;
    private OffsetDateTime _openAt;
    @Nullable
    private OffsetDateTime _closedAt;

    public Request(UUID id, UUID citizenId, Status status, OffsetDateTime openAt, @Nullable OffsetDateTime closedAt) {
        super(id);
        _citizenId = citizenId;
        _status = status;
        _openAt = openAt;
        _closedAt = closedAt;
    }

    public UUID GetCitizenId() {
        return _citizenId;
    }
    public void SetCitizenId(UUID citizenId) {
        _citizenId = citizenId;
    }

    public Status GetStatus() {
        return _status;
    }
    public void SetStatus(Status status) {
        _status = status;
    }

    public OffsetDateTime GetOpenAt() {
        return _openAt;
    }
    public void SetOpenAt(OffsetDateTime openAt) {
        _openAt = openAt;
    }

    public OffsetDateTime GetClosedAt() {
        return _closedAt;
    }
    public void SetClosedAt(OffsetDateTime closedAt) {
        _closedAt = closedAt;
    }

    public static Request TryParse(ResultSet set) throws SQLException {
        return new Request(
                set.getObject(1, UUID.class),
                set.getObject(2, UUID.class),
                Status.valueOf(set.getString(3)),
                set.getObject(4, OffsetDateTime.class),
                set.getObject(5, OffsetDateTime.class)
        );
    }
}