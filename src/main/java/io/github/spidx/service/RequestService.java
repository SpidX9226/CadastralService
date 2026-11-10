package io.github.spidx.service;

import java.util.UUID;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import io.github.spidx.proxy.RequestProxy;
import io.github.spidx.model.Request;
import io.github.spidx.exception.BusinessException;
import io.github.spidx.exception.RequestNotFoundException;

public class RequestService {
    private static final RequestProxy REQUEST_PROXY = new RequestProxy();

    private RequestService() {
    }

    public static void Add(Request request) {
        Validate(request);

        REQUEST_PROXY.Add(request);
    }

    public static List<Request> GetAll() {
        return REQUEST_PROXY.GetAll();
    }

    @Nullable
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

    public static void Update(Request request) {
        Request oldRequest = GetById(request.GetId());
        oldRequest = oldRequest.Update(request);
    }

    public static void Delete(UUID id) {
        if (id == null) {
            throw new BusinessException("ID поле не может быть пустым");
        }

        REQUEST_PROXY.Delete(id);
    }

    private static void Validate(Request request) {
        if (request.GetId() == null)
            throw new BusinessException("ID field cannot be empty");
        if (REQUEST_PROXY.GetById(request.GetId()) != null)
            throw new BusinessException("Заявка с этим ID уже есть");
        if (request.GetStatus() == null)
            throw new BusinessException("Нельзя установить некорректный статус. [Request#Status is null]");
        if (request.GetCitizenId() == null)
            throw new BusinessException("Нельзя создать заявку без гражданина");
        if (request.GetOpenAt() == null)
            throw new BusinessException("Нельзя создать заявку без даты создания");
    }
};
