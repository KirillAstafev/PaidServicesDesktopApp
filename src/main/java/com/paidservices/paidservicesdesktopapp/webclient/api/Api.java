package com.paidservices.paidservicesdesktopapp.webclient.api;

import com.paidservices.paidservicesdesktopapp.model.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface Api {
    @GET("visitations")
    Call<List<Visitation>> getVisitations();

    @POST("visitations")
    Call<Integer> saveVisitation(@Body Visitation visitation);

    @PUT("visitations/{id}")
    Call<Void> updateVisitation(@Path("id") Integer id, @Body Visitation visitation);

    @DELETE("visitations/{id}")
    Call<Void> deleteVisitation(@Path("id") Integer id);

    @GET("staffs")
    Call<List<Staff>> getStaffList();

    @GET("services")
    Call<List<MedicalService>> getMedicalServiceList();

    @GET("persons")
    Call<Person> getPersonBySnils(@Query("snils") String snils);

    @POST("persons")
    Call<Integer> savePerson(@Body Person person);

    @GET("records")
    Call<List<MedicalRecord>> getRecordsByPatientId(@Query("patientId") Integer patientId);
}
