package com.paidservices.paidservicesdesktopapp.webclient.api;

import com.paidservices.paidservicesdesktopapp.visitation.model.MedicalService;
import com.paidservices.paidservicesdesktopapp.visitation.model.Staff;
import com.paidservices.paidservicesdesktopapp.visitation.model.Visitation;
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
}
