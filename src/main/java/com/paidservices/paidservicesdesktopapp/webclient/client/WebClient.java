package com.paidservices.paidservicesdesktopapp.webclient.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.visitation.model.Person;
import com.paidservices.paidservicesdesktopapp.visitation.model.Staff;
import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
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
}
