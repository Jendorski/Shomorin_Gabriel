package com.shomoringabriel.filterRepository;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.shomoringabriel.adapters.FilterRecyclerAdapter;
import com.shomoringabriel.database.DecagonDatabase;
import com.shomoringabriel.models.filterModel.colorModel.FilterColorModel;
import com.shomoringabriel.models.filterModel.countryModel.FilterCountryModel;
import com.shomoringabriel.models.filterModel.userModel.FilterUserModel;
import com.shomoringabriel.retrofit.ApiClient;
import com.shomoringabriel.retrofit.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.shomoringabriel.utils.Utils.getAppContext;

public class FilterRepository {

    private static final String TAG = FilterRepository.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AVLoadingIndicatorView avLoadingIndicatorView;

    private TextView errorText;

    private RecyclerView recyclerView;

    private List<FilterUserModel> filterUserModels;

    private DatabaseDefinition database;

    public FilterRepository() {
        filterUserModels = new ArrayList<>();
        filterUserModels.clear();
    }

    public FilterRepository getRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;

        return this;
    }

    public FilterRepository getErrorText(TextView textView){
        this.errorText = textView;
        return this;
    }

    public FilterRepository getAVL(AVLoadingIndicatorView avl){
        this.avLoadingIndicatorView = avl;
        return this;
    }

    public void g(){
        long u = countUserFilters();
        long col = countColorFilter();
        long cou = countCountryFilter();

        if(u>0 && col > 0 && cou > 0){
            getDBFilterParameters();
        }else {
            getNetworkFilterParameters();
        }
    }

    public void filterTheParameters(String dateRange, String gender, List<String> countries, List<String> colors){
        long u = countUserFilters();
        long col = countColorFilter();
        long cou = countCountryFilter();

        if(u > 0 && col > 0 && cou > 0){
            RXSQLite.rx(SQLite.select().from(FilterUserModel.class))
                    .queryList()
                    .toObservable()
                    .flatMapIterable((Function<List<FilterUserModel>, Iterable<FilterUserModel>>) filterUserModels -> filterUserModels)
                    .flatMap((Function<FilterUserModel, ObservableSource<FilterUserModel>>) filterUserModel -> Observable.just(filterUserModel))
                    .filter(new Predicate<FilterUserModel>() {
                @Override
                public boolean test(FilterUserModel filterUserModel) throws Exception {
                    return filterUserModel.getGender().equalsIgnoreCase(gender);
                }
            }).subscribe();
        }

    }

    public void getDBFilterParameters(){

        if(avLoadingIndicatorView != null){
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
            avLoadingIndicatorView.show();
        }

        RXSQLite.rx(SQLite.select().from(FilterUserModel.class))
                .queryList()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(new SingleObserver<List<FilterUserModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<FilterUserModel> filterUserModels) {
                        Log.e(TAG, " List<FilterUserModel>: " + filterUserModels.size());
                        if(recyclerView != null
                        && avLoadingIndicatorView != null){

                            FilterRecyclerAdapter adapter = new FilterRecyclerAdapter(filterUserModels);
                            adapter.notifyDataSetChanged();

                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getAppContext()));

                            avLoadingIndicatorView.setVisibility(View.INVISIBLE);
                            avLoadingIndicatorView.hide();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public void getNetworkFilterParameters(){

        //serviceCaller is the interface initialized with retrofit.create...
        Retrofit retrofit = ApiClient.getClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getAccounts().enqueue(new Callback<List<FilterUserModel>>() {
            @Override
            public void onResponse(Call<List<FilterUserModel>> call, Response<List<FilterUserModel>> response) {
                Observable.just(Objects.requireNonNull(response.body()))
                        .subscribeOn(Schedulers.io())
                        .flatMapIterable((Function<List<FilterUserModel>, Iterable<FilterUserModel>>) filterUserModels -> {
                            /**Save to DB*/
                            database = FlowManager.getDatabase(DecagonDatabase.class);

                            FastStoreModelTransaction<FilterUserModel> transaction = FastStoreModelTransaction
                                    .saveBuilder(FlowManager.getModelAdapter(FilterUserModel.class))
                                    .addAll(filterUserModels)
                                    .build();

                            database.executeTransaction(transaction);

                            return filterUserModels;
                        }).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .flatMap((Function<FilterUserModel, ObservableSource<List<FilterCountryModel>>>) filterUserModel -> {

                            Log.e(TAG," LIST<COLORS> : " + filterUserModel.getColors().size() + " LIST<COUNTRIES>: " + filterUserModel.getCountries().size());

                            /**Should add a condition here, if the size is zero, no need to resolve jack*/
                            resolveColor(filterUserModel.getColors(), filterUserModel.getId());

                            return resolveCountries(filterUserModel.getCountries(), filterUserModel.getId());
                        }).subscribe(new Observer<List<FilterCountryModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<FilterCountryModel> filterCountryModels) {

                        Log.e(TAG, "LIST OF FILTER COUNTRY MODEL: " + filterCountryModels.size());

                        /**Save to DB*/
                        database = FlowManager.getDatabase(DecagonDatabase.class);

                        FastStoreModelTransaction<FilterCountryModel> transaction = FastStoreModelTransaction
                                .saveBuilder(FlowManager.getModelAdapter(FilterCountryModel.class))
                                .addAll(filterCountryModels)
                                .build();

                        database.executeTransaction(transaction);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<FilterUserModel>> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
            }
        });

    }

    private void resolveColor(List<String> colors, String id){
        Observable.just(colors)
                .subscribeOn(Schedulers.single())
                .flatMapIterable((Function<List<String>, Iterable<String>>) strings -> strings)
                .observeOn(Schedulers.single())
                .flatMap((Function<String, ObservableSource<FilterColorModel>>) s -> {
                    FilterColorModel colorModel = new FilterColorModel();
                    colorModel.setColorName(s);
                    colorModel.setUserId(id);
                    return Observable.just(colorModel);
                }).toList()
                .observeOn(Schedulers.single())
                .subscribe(new SingleObserver<List<FilterColorModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<FilterColorModel> filterColorModels) {
                        /**Save to DB*/
                        DatabaseDefinition database = FlowManager.getDatabase(DecagonDatabase.class);

                        FastStoreModelTransaction<FilterColorModel> transaction = FastStoreModelTransaction
                                .saveBuilder(FlowManager.getModelAdapter(FilterColorModel.class))
                                .addAll(filterColorModels)
                                .build();

                        database.executeTransaction(transaction);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Observable<List<FilterCountryModel>> resolveCountries(List<String> countries, String id){
        return Observable.just(countries)
                .subscribeOn(Schedulers.newThread())
                .flatMapIterable(new Function<List<String>, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(List<String> strings) throws Exception {
                        return strings;
                    }
                }).flatMap(new Function<String, ObservableSource<FilterCountryModel>>() {
            @Override
            public ObservableSource<FilterCountryModel> apply(String s) throws Exception {
                FilterCountryModel countryModel = new FilterCountryModel();
                countryModel.setCountryName(s);
                countryModel.setUserId(id);
                return Observable.just(countryModel);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .toList().toObservable();

    }

    private long countCountryFilter(){
        return RXSQLite.rx(SQLite.select()
                .from(FilterCountryModel.class))
                .count()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .blockingGet();
    }

    private long countColorFilter(){
        return RXSQLite.rx(SQLite.select()
                .from(FilterColorModel.class))
                .count()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .blockingGet();
    }

    public static long countUserFilters(){
        return RXSQLite.rx(SQLite.select()
                .from(FilterUserModel.class))
                .count()
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .blockingGet();
    }

    public void clearDisposables(){
        compositeDisposable.clear();
    }

}
