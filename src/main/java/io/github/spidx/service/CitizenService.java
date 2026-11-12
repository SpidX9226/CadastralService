package io.github.spidx.service;

import java.lang.RuntimeException;
import java.util.List;
import java.util.UUID;

import io.github.spidx.exception.BusinessException;
import io.github.spidx.exception.CitizenNotFoundException;
import org.jetbrains.annotations.NotNull;

import io.github.spidx.proxy.*;
import io.github.spidx.util.StringExtension;
import io.github.spidx.model.*;

public class CitizenService {
    private static final CitizenProxy _citizenProxy = new CitizenProxy();

    public CitizenService(CitizenProxy citizenProxy, RequestProxy requestProxy) {
    }

    public static void Add(Citizen citizen) {
        if (StringExtension.IsNullOrEmpty(citizen.GetFullName())) {
            throw new BusinessException("Citizen's name cannot be empty");
        }

        if (citizen.GetId() != null) {
            if (_citizenProxy.GetById(citizen.GetId()) != null) {
                throw new BusinessException("Citizen's Id cannot be empty");
            }
        } else {
            citizen.SetId(UUID.randomUUID());
        }

        if (!(_citizenProxy.Add(citizen))) {
            throw new BusinessException("Unable to Add the Citizen");
        }
    }

    public static List<Citizen> GetAll() {
        return _citizenProxy.GetAll();
    }

    @NotNull
    public static Citizen GetById(UUID id) {
        if (id == null) {
            throw new BusinessException("There is cannot be a citizen with id: null");
        }

        var citizen = _citizenProxy.GetById(id);

        if (citizen == null) {
            throw new CitizenNotFoundException("temp");
        }

        return citizen;
    }

    public static void Update(Citizen citizen) {
        var oldCitizen = GetById(citizen.GetId());
        citizen = oldCitizen.Update(citizen);

        _citizenProxy.Update(citizen);
    }

    public static void Delete(UUID id) {
        GetById(id);

        _citizenProxy.Delete(id);
    }
}
