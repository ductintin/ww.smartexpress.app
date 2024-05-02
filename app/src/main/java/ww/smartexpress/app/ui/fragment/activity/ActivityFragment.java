package ww.smartexpress.app.ui.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.Shimmer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.ApiModelUtils;
import ww.smartexpress.app.data.model.api.response.BookingResponse;
import ww.smartexpress.app.data.model.api.response.Order;
import ww.smartexpress.app.databinding.FragmentActivityBinding;
import ww.smartexpress.app.di.component.FragmentComponent;
import ww.smartexpress.app.ui.base.fragment.BaseFragment;
import ww.smartexpress.app.ui.delivery.order.DeliveryActivity;
import ww.smartexpress.app.ui.fragment.activity.adapter.BookingAdapter;
import ww.smartexpress.app.ui.fragment.activity.adapter.OrderAdapter;
import ww.smartexpress.app.ui.fragment.activity.adapter.OrderBookWinAdapter;
import ww.smartexpress.app.ui.fragment.activity.adapter.OrderShoppingAdapter;
import ww.smartexpress.app.ui.order.details.OrderDetailsActivity;
import ww.smartexpress.app.ui.search.SearchActivity;
import ww.smartexpress.app.ui.trip.TripActivity;
import ww.smartexpress.app.ui.trip.detail.TripDetailActivity;

public class ActivityFragment extends BaseFragment<FragmentActivityBinding, ActivityFragmentViewModel> {
    OrderAdapter orderAdapter;
    OrderBookWinAdapter orderBookWinAdapter;
    OrderShoppingAdapter orderShoppingAdapter;
    BookingAdapter bookingAdapter;
    List<BookingResponse> bookingResponses = new ArrayList<>();

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_activity;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.shimmerViewContainer.setShimmer(new Shimmer.AlphaHighlightBuilder()
                .setDuration(2000) // Đặt thời gian shimmer là 2000 milliseconds (2 giây)
                .build());
        getBooking();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.rcOrder.setLayoutManager(manager);

//        binding.rcOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//
//                Log.d("TAG", "onScrolled: ");
//                int visibleItemCount = manager.getChildCount();
//                int totalCount = manager.getItemCount();
//                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
//
//                if (dy == recyclerView.getChildAt(0).getMeasuredHeight() - recyclerView.getMeasuredHeight()) {
//                    Log.d("TAG", "onScrolled: bt");
//                }
//                if(firstVisibleItemPosition >= 0 && (visibleItemCount + firstVisibleItemPosition) >= totalCount){
//                    Log.d("TAG", "onScrolled: d");
//                }
//
//                if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == bookingResponses.size() - 1){
//                    Log.d("TAG", "onScrolled: enddd");
//                }
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    Log.d("TAG", "onScrollChange: endndd");
                    if(viewModel.pageNumber.get() < viewModel.pageTotal.get()){
                        loadMore();
                    }
                }
            }
        });
        binding.swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.pageNumber.set(0);
                viewModel.pageTotal.set(0);
                if(bookingAdapter != null){
                    bookingAdapter.clearItems();
                }
                getBooking();
                binding.swRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        //loadOrder();
//        binding.setLifecycleOwner(this);
//        bookingAdapter = new BookingAdapter();
//        bookingResponses = new ArrayList<>();
//
//        viewModel.bookingList.observe(this, bookings -> {
//            if(bookingResponses == null || bookingResponses.isEmpty()){
//                bookingResponses = bookings;
//            }else {
//                bookingResponses.addAll(bookings);
//            }
//            bookingAdapter.setBookings(bookingResponses);
//            if(bookingResponses == null || bookingResponses.isEmpty()){
//                viewModel.isEmpty.set(true);
//            }
//
//        });
//
//
//        loadBooking();

        //getMyBooking();
        binding.txtBookWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.orderStatus.setValue(0);
                loadOrder();
            }
        });
        binding.txtDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.orderStatus.setValue(1);
                loadOrder();
            }
        });
        binding.txtShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.orderStatus.setValue(2);
                loadOrder();
            }
        });
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    private void loadOrder(){
        //
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("a"));
        orders.add(new Order("a"));
        orders.add(new Order("a"));
        orders.add(new Order("a"));
        orders.add(new Order("a"));

        binding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);
        binding.rcOrder.setLayoutManager(layoutManager);

        if(viewModel.orderStatus.getValue()==0){
            orderBookWinAdapter = new OrderBookWinAdapter(orders);
            binding.rcOrder.setAdapter(orderBookWinAdapter);
            orderBookWinAdapter.setOnItemClickListener(order -> {
                startActivity(new Intent(getActivity(), TripActivity.class));
            });
        }else if(viewModel.orderStatus.getValue()==1) {
            orderAdapter = new OrderAdapter(orders);
            binding.rcOrder.setAdapter(orderAdapter);
            orderAdapter.setOnItemClickListener(order -> {
                startActivity(new Intent(getActivity(), DeliveryActivity.class));
            });
        }else{
            orderShoppingAdapter = new OrderShoppingAdapter(orders);
            binding.rcOrder.setAdapter(orderShoppingAdapter);
            orderShoppingAdapter.setOnItemClickListener(order -> {
                startActivity(new Intent(getActivity(), OrderDetailsActivity.class));
            });
        }

    }

    public void getMyBooking(){
        viewModel.showLoading();
        viewModel.compositeDisposable.add(viewModel.getMyBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    viewModel.hideLoading();
                    if(response.isResult()){
                        if (response.getData().getContent() == null){
                            viewModel.isEmpty.set(true);
                        }else{
                            bookingResponses = response.getData().getContent();
                            viewModel.isEmpty.set(false);
                            loadBooking();
                        }
                    }else{
                        viewModel.showErrorMessage(response.getMessage());
                    }

                },error->{
                    viewModel.isEmpty.set(true);
                    viewModel.hideLoading();
                    viewModel.showErrorMessage(getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void loadBooking(){
        binding.setLifecycleOwner(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);
        binding.rcOrder.setLayoutManager(layoutManager);

        bookingAdapter = new BookingAdapter(bookingResponses);
        binding.rcOrder.setAdapter(bookingAdapter);

        bookingAdapter.setOnItemClickListener(booking -> {
            Intent intent = new Intent(getActivity(), TripDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.CUSTOMER_BOOKING_DETAIL_ID, booking.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        //getMyBooking();
    }

    public void getBooking(){
        if(viewModel.pageNumber.get() == 0){
            binding.shimmerViewContainer.setVisibility(View.VISIBLE);
            binding.shimmerViewContainer.startShimmer();
        }
        viewModel.compositeDisposable.add(viewModel.getBooking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        if(viewModel.pageNumber.get() == 0){
                            binding.shimmerViewContainer.setVisibility(View.GONE);
                            binding.shimmerViewContainer.stopShimmer();
                        }
                        viewModel.bookingList.setValue(response.getData().getContent());
                        viewModel.pageTotal.set(response.getData().getTotalPages());
                        if(bookingResponses.isEmpty()){
                            bookingResponses = response.getData().getContent();
                        }else{
                            bookingResponses.addAll(response.getData().getContent());
                        }
                        if(bookingResponses.size() == 0 && viewModel.pageNumber.get() == 0){
                            viewModel.isEmpty.set(true);
                        }else{
                            loadBooking();
                        }
                        Log.d("TAG", "getMyBooking: "+ viewModel.pageTotal.get());
                    }else {
                        viewModel.showErrorMessage(response.getMessage());
                    }
                },error->{
                    viewModel.showErrorMessage(getActivity().getString(R.string.network_error));
                    error.printStackTrace();
                })
        );
    }

    public void loadMore(){
        int pageCurrent = viewModel.pageNumber.get();
        viewModel.pageNumber.set(pageCurrent+1);
        getBooking();
    }
}
