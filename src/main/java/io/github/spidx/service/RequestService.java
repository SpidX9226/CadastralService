package io.github.spidx.service;

import java.util.UUID;
import java.util.List;

import io.github.spidx.model.Status;
import org.jetbrains.annotations.Nullable;

import io.github.spidx.proxy.RequestProxy;
import io.github.spidx.model.Request;
import io.github.spidx.exception.BusinessException;
import io.github.spidx.exception.RequestNotFoundException;

public class RequestService {
    private static final RequestProxy REQUEST_PROXY = new RequestProxy();

    private RequestService() {
    }

    public static List<Request> GetAll() {
        return REQUEST_PROXY.GetAll();
    }

    public static Request GetById(UUID id) {
        if (id == null) {
            throw new BusinessException("ID поле не может быть пустым");
        }

        var result = REQUEST_PROXY.GetById(id);

        if (result == null) {
            throw new RequestNotFoundException("Заявки с таким ID не существует");
        }

        return result;
    }

    public static void Add(Request request) {
        Validate(request);

        if (!REQUEST_PROXY.Add(request)) {
            throw new BusinessException("Unable to create request");
        }
    }

    public static void Update(Request request) {
        if (request == null || request.GetId() == null) {
            throw new BusinessException("Request ID cannot be empty");
        }

        // Ensures the request exists.
        GetById(request.GetId());

        ValidateFields(request);

        if (!REQUEST_PROXY.Update(request)) {
            throw new RequestNotFoundException(
                    "Request with this ID does not exist"
            );
        }
    }

    public static void Delete(UUID id) {
        GetById(id);

        if (!REQUEST_PROXY.Delete(id)) {
            throw new RequestNotFoundException(
                    "Request with this ID does not exist"
            );
        }
    }

    private static void Validate(Request request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }

        if (request.GetId() == null) {
            throw new BusinessException("Request ID cannot be empty");
        }

        if (REQUEST_PROXY.GetById(request.GetId()) != null) {
            throw new BusinessException("Request with this ID already exists");
        }

        ValidateFields(request);
    }

    private static void ValidateFields(Request request) {
        if (request.GetStatus() == null) {
            throw new BusinessException("Request status cannot be empty");
        }

        if (request.GetCitizenId() == null) {
            throw new BusinessException("Citizen ID cannot be empty");
        }

        if (request.GetOpenAt() == null) {
            throw new BusinessException("Opening date cannot be empty");
        }

        boolean isActive =
                request.GetStatus() == Status.NEW ||
                        request.GetStatus() == Status.IN_PROGRESS;

        if (isActive && request.GetClosedAt() != null) {
            throw new BusinessException(
                    "An active request cannot have a closing date"
            );
        }

        if (!isActive && request.GetClosedAt() == null) {
            throw new BusinessException(
                    "A completed or cancelled request must have a closing date"
            );
        }

        if (request.GetClosedAt() != null &&
                request.GetClosedAt().isBefore(request.GetOpenAt())) {
            throw new BusinessException(
                    "Closing date cannot be earlier than opening date"
            );
        }
    }
};
