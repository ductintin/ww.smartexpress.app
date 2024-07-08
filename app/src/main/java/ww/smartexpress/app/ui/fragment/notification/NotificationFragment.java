package ww.smartexpress.app.ui.fragment.notification;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.NotificationResponse;
import ww.smartexpress.app.databinding.FragmentNotificationBinding;
import ww.smartexpress.app.di.component.FragmentComponent;
import ww.smartexpress.app.ui.base.fragment.BaseFragment;
import ww.smartexpress.app.ui.notification.details.NotificationDetailsActivity;
import ww.smartexpress.app.ui.view.ProgressItem;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding, NotificationFragmentViewModel>
        implements FlexibleAdapter.EndlessScrollListener, EmptyViewHelper.OnEmptyViewListener, FlexibleAdapter.OnItemClickListener{


    FlexibleAdapter mFlexibleAdapter;
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void performDataBinding() {
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMyNotification();

        binding.swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.pageNumber.set(0);
                getMyNotification();
                binding.swRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void noMoreLoad(int newItemsSize) {

    }

    @Override
    public void onLoadMore(int lastPosition, int currentPage) {
        new Handler().postDelayed(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {

                viewModel.pageNumber.set(viewModel.pageNumber.get()+1);
                getMyNotification();
            }
        }, 1500);
    }

    @Override
    public boolean onItemClick(View view, int position) {
        NotificationResponse notificationResponse = (NotificationResponse) mFlexibleAdapter.getItem(position);
        Log.d("TAG", "onItemClick: "+notificationResponse.getMsg());
        Intent intent;
        switch (notificationResponse.getKind()){
            case Constants.NOTIFICATION_KIND_PROMOTION:
            case Constants.NOTIFICATION_KIND_WARNING:
            case Constants.NOTIFICATION_KIND_SYSTEM:
                viewModel.readNotification(notificationResponse.getId());
                intent = new Intent(getContext(), NotificationDetailsActivity.class);
                intent.putExtra("messageNoti", notificationResponse.getMsg());
                intent.putExtra("notificationId",notificationResponse.getId());
                startActivity(intent);
                notificationResponse.setState(1);
                mFlexibleAdapter.updateItem(notificationResponse);
                viewModel.totalUnread.set(viewModel.totalUnread.get()-1);
                break;
            case Constants.NOTIFICATION_KIND_APPROVE_PAYOUT:
                break;
            case Constants.NOTIFICATION_KIND_REJECT_PAYOUT:
                break;
            case Constants.NOTIFICATION_KIND_DEPOSIT_SUCCESSFULLY:
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onUpdateEmptyDataView(int size) {
        if(size>0){
            binding.layoutEmpty.emptyView.setVisibility(View.GONE);
        }else {
            binding.layoutEmpty.emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onUpdateEmptyFilterView(int size) {

    }

    private void getMyNotification(){
        if (viewModel.pageNumber.get() == 0){
            viewModel.showLoading();
        }
        viewModel.compositeDisposable.add(viewModel.getMyNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.totalUnread.set(Math.toIntExact(response.getData().getTotalUnread()));
                        viewModel.pageTotal.set(response.getData().getTotalPages());
                        viewModel.totalElement.set(Math.toIntExact(response.getData().getTotalElements()));
                        if (response.getData().getContent()!=null){
                            if(viewModel.pageNumber.get() == 0){

                                mFlexibleAdapter = new FlexibleAdapter(response.getData().getContent(), this);

                                if(viewModel.totalElement.get()> viewModel.pageSize.get()){
                                    mFlexibleAdapter
                                            .setLoadingMoreAtStartUp(false)
                                            .setEndlessScrollListener(this, new ProgressItem())
                                            .setEndlessScrollThreshold(1)
                                            .setEndlessTargetCount(viewModel.totalElement.get())
                                            .setEndlessPageSize(viewModel.pageSize.get());
                                }
                                EmptyViewHelper.create(mFlexibleAdapter, binding.layoutEmpty.emptyView, null, this);

                                binding.rcNotification.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                                binding.rcNotification.setAdapter(mFlexibleAdapter);
                            }else {
                                mFlexibleAdapter.onLoadMoreComplete(response.getData().getContent());
                            }
                        }
                    }else {
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }
                    viewModel.hideLoading();
                },error->{
                    viewModel.showErrorMessage(getActivity().getString(R.string.network_error));
                    error.printStackTrace();
                    viewModel.hideLoading();
                })
        );
    }

    public void readAllNotification(){
        viewModel.compositeDisposable.add(viewModel.readAllNotification()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.isResult()){
                        viewModel.pageNumber.set(0);
                        getMyNotification();
                    }else{
                        viewModel.getApplication().getErrorUtils().handelError(response.getCode());
                    }
                },error->{
                    error.printStackTrace();
                })
        );
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
