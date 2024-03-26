package ww.smartexpress.app.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

import ww.smartexpress.app.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.data.model.api.response.OrderOption;
import ww.smartexpress.app.data.model.api.response.OrderOptionDetail;
import ww.smartexpress.app.databinding.ActivityOrderBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;
import ww.smartexpress.app.ui.order.adapter.OrderOptionAdapter;

public class OrderActivity extends BaseActivity<ActivityOrderBinding, OrderViewModel> {
    private OrderOptionAdapter orderOptionAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load();
    }

    public void load(){
        List<OrderOptionDetail> ood1 = new ArrayList<>();
        ood1.add(new OrderOptionDetail("1", "Size M (500ml)", "", false));
        ood1.add(new OrderOptionDetail("2", "Size L (700ml)", "+5.000", false));
        ood1.add(new OrderOptionDetail("1", "Size Khổng Lồ (1L)", "+10.000", false));

        List<OrderOptionDetail> ood2 = new ArrayList<>();
        ood2.add(new OrderOptionDetail("3", "Trân châu hoàng kim", "+5.000", false));
        ood2.add(new OrderOptionDetail("4", "Trân châu đen", "+5.000", false));
        ood2.add(new OrderOptionDetail("5", "Phô mai viên", "+5.000", false));
        ood2.add(new OrderOptionDetail("6", "Thạch thủy tinh", "+5.000", false));

        List<OrderOption> orderOptions = new ArrayList<>();
        orderOptions.add(new OrderOption("1", "Size (chọn 1)", true, ood1));
        orderOptions.add(new OrderOption("2", "Lựa chọn", false, ood2));

        viewBinding.setLifecycleOwner(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()
                ,LinearLayoutManager.VERTICAL, false);

        viewBinding.rcOrderOption.setLayoutManager(layoutManager);
        viewBinding.rcOrderOption.setItemAnimator(new DefaultItemAnimator());
        orderOptionAdapter = new OrderOptionAdapter(orderOptions, getApplicationContext());
        viewBinding.rcOrderOption.setAdapter(orderOptionAdapter);

        orderOptionAdapter.setOnItemClickListener(orderOption -> {
            Log.d("Click", "performDataBinding: ");
        });

        ExpandableTextView expTv = (ExpandableTextView) findViewById(R.id.expand_text_view).findViewById(R.id.expand_text_view);
        expTv.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                viewModel.isExpand.set(isExpanded);
            }
        });


        expTv.setText("It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. " +
                "The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. " +
                "Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy." +
                "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, " +
                "or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't " +
                "anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, " +
                "making this the first true generator on the Internet.");

    }

}
