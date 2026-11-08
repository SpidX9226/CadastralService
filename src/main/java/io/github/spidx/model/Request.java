package io.github.spidx.model;

import java.util.UUID;
import java.time.OffsetDateTime;

public class Request extends Model {
    private UUID _citizenId;
    private String _status;
    private OffsetDateTime _openAt;
    private OffsetDateTime _closedAt;

    public Request(UUID id) {
        super(id);
    }

    public UUID GetCitizenId() {
        return _citizenId;
    }

    public void SetCitizenId(UUID citizenId) {
        _citizenId = citizenId;
    }

    public String GetStatus() {
        return _status;
    }

    public void SetStatus(String status) {
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
}