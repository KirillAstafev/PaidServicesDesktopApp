package com.paidservices.paidservicesdesktopapp.webclient.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paidservices.paidservicesdesktopapp.model.*;
import com.paidservices.paidservicesdesktopapp.webclient.api.Api;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WebClient {
    private static final WebClient client = new WebClient();

    public static WebClient getInstance() {
        return client;
    }

    private final Api apiService;

    public WebClient() {
        ObjectMapper mapper = JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new JavaTimeModule())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/paid-services/")
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        apiService = retrofit.create(Api.class);
    }

    public CompletableFuture<List<Visitation>> getVisitations() {
        CompletableFuture<List<Visitation>> result = new CompletableFuture<>();

        apiService.getVisitations()
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Integer> saveVisitation(Visitation visitation) {
        CompletableFuture<Integer> result = new CompletableFuture<>();

        apiService.saveVisitation(visitation)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Void> updateVisitation(Visitation visitation) {
        CompletableFuture<Void> result = new CompletableFuture<>();

        apiService.updateVisitation(visitation.getId(), visitation)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Void> deleteVisitation(Integer id) {
        CompletableFuture<Void> result = new CompletableFuture<>();

        apiService.deleteVisitation(id)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<List<Staff>> getStaffList() {
        CompletableFuture<List<Staff>> result = new CompletableFuture<>();

        apiService.getStaffList()
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<List<MedicalService>> getMedicalServiceList() {
        CompletableFuture<List<MedicalService>> result = new CompletableFuture<>();

        apiService.getMedicalServiceList()
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Integer> saveMedicalService(MedicalService service) {
        CompletableFuture<Integer> result = new CompletableFuture<>();

        apiService.saveMedicalService(service)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Void> updateMedicalService(MedicalService service) {
        CompletableFuture<Void> result = new CompletableFuture<>();

        apiService.updateMedicalService(service.getId(), service)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Void> deleteMedicalService(Integer id) {
        CompletableFuture<Void> result = new CompletableFuture<>();

        apiService.deleteMedicalService(id)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Person> getPersonBySnils(String snils) {
        CompletableFuture<Person> result = new CompletableFuture<>();

        apiService.getPersonBySnils(snils)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Integer> savePerson(Person person) {
        CompletableFuture<Integer> result = new CompletableFuture<>();

        apiService.savePerson(person)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<List<MedicalRecord>> getRecordsByPatientId(Integer patientId) {
        CompletableFuture<List<MedicalRecord>> result = new CompletableFuture<>();

        apiService.getRecordsByPatientId(patientId)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Integer> saveMedicalRecord(MedicalRecord record) {
        CompletableFuture<Integer> result = new CompletableFuture<>();

        apiService.saveMedicalRecord(record)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Void> updateMedicalRecord(MedicalRecord record) {
        CompletableFuture<Void> result = new CompletableFuture<>();

        apiService.updateMedicalRecord(record.getId(), record)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<Void> deleteMedicalRecord(Integer id) {
        CompletableFuture<Void> result = new CompletableFuture<>();

        apiService.deleteMedicalRecord(id)
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }

    public CompletableFuture<List<Diagnosis>> getDiagnoses() {
        CompletableFuture<List<Diagnosis>> result = new CompletableFuture<>();

        apiService.getDiagnoses()
                .enqueue(new MainServiceCallback<>(result));

        return result;
    }
}
