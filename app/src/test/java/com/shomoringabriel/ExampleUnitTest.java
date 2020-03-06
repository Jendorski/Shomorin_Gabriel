package com.shomoringabriel;

import com.shomoringabriel.models.filterModel.countryModel.FilterCountryModel;
import com.shomoringabriel.models.filterModel.userModel.FilterUserModel;
import com.shomoringabriel.retrofit.ApiInterface;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRetrofit() throws IOException {
        MockWebServer webServer = new MockWebServer();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(webServer.url("https://android-json-test-api.herokuapp.com/").toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Schedule some responses.
        webServer.enqueue(new MockResponse().setBody("[\n" +
                "  {\n" +
                "    \"id\": \"5e5a3027019097b6c1978f2e\",\n" +
                "    \"avatar\": \"https://randomuser.me/api/portraits/women/55.jpg\",\n" +
                "    \"fullName\": \"Orr Alvarado\",\n" +
                "    \"createdAt\": \"Sun, 01 Mar 2020 05:34:31 GMT\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"colors\": [],\n" +
                "    \"countries\": [\n" +
                "      \"China\",\n" +
                "      \"South Africa\",\n" +
                "      \"france\",\n" +
                "      \"Mexico\",\n" +
                "      \"Japan\",\n" +
                "      \"Estonia\",\n" +
                "      \"Colombia\",\n" +
                "      \"China\"\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"5e5a30272dd31ceb96896179\",\n" +
                "    \"avatar\": \"https://randomuser.me/api/portraits/men/20.jpg\",\n" +
                "    \"fullName\": \"Carla Roberta\",\n" +
                "    \"createdAt\": \"Wed, 04 Mar 2020 13:34:31 GMT\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"colors\": [\n" +
                "      \"Green\",\n" +
                "      \"Violet\",\n" +
                "      \"Yellow\",\n" +
                "      \"Blue\",\n" +
                "      \"Teal\",\n" +
                "      \"Maroon\",\n" +
                "      \"Red\",\n" +
                "      \"Aquamarine\",\n" +
                "      \"Orange\",\n" +
                "      \"Mauv\"\n" +
                "    ],\n" +
                "    \"countries\": [\n" +
                "      \"China\",\n" +
                "      \"South Africa\",\n" +
                "      \"france\",\n" +
                "      \"Mexico\",\n" +
                "      \"Japan\",\n" +
                "      \"Estonia\",\n" +
                "      \"Colombia\",\n" +
                "      \"China\"\n" +
                "    ]\n" +
                "  }\n" +
                "  ]"));

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        //With your service created you can now call its method that should
        //consume the MockResponse above. You can then use the desired
        //assertion to check if the result is as expected. For example:
        Call<List<FilterUserModel>> call = apiInterface.getAccounts();
        assertNotNull(call.execute());

        //Finish web server
        webServer.shutdown();
    }

    //For the filteration
    @Test
    public void testFiltering() throws Exception{

        TestObserver<List<FilterUserModel>> testSubscriber = new TestObserver<>();

        List<FilterCountryModel> inputCountries = new ArrayList<>();
        List<String> c = new ArrayList<>();
        c.add("Nigeria");
        c.add("China");
        c.add("France");

        inputCountries.add(new FilterCountryModel("1","Nigeria"));
        inputCountries.add(new FilterCountryModel("2","France"));
        inputCountries.add(new FilterCountryModel("3","China"));

        Observable<List<FilterCountryModel>> mockObservable = Observable.just(inputCountries);

        List<FilterCountryModel> expectedCountries = new ArrayList<>();
        expectedCountries.add(new FilterCountryModel("1","Nigeria"));
        expectedCountries.add(new FilterCountryModel("3","China"));

/*
        doReturn(mockObservable).when(mockObservable).filter(new Predicate<List<FilterCountryModel>>() {
            @Override
            public boolean test(List<FilterCountryModel> filterCountryModels) throws Exception {
                return filterCountryModels.containsAll(expectedCountries);
            }
        }).subscribe();
*/

        testSubscriber.assertNotSubscribed();

    }

}