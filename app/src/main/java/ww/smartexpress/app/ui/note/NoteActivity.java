package ww.smartexpress.app.ui.note;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import eu.davidea.flexibleadapter.databinding.BR;
import ww.smartexpress.app.R;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.model.api.response.Note;
import ww.smartexpress.app.databinding.ActivityNoteBinding;
import ww.smartexpress.app.di.component.ActivityComponent;
import ww.smartexpress.app.ui.base.activity.BaseActivity;

public class NoteActivity extends BaseActivity<ActivityNoteBinding, NoteViewModel> {
    @Override
    public int getLayoutId() {
        return R.layout.activity_note;
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
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            viewModel.noteHint.set(bundle.getInt("noteHint"));
            viewModel.textNote.set(bundle.getString(Constants.CUSTOMER_BOOKING_NOTE));
        }

        viewBinding.bntNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.noteHint.get() == 0){
                    Note note = new Note(viewModel.textNote.get());
                    EventBus.getDefault().postSticky(note);
                }
                onBackPressed();
            }
        });

        //viewModel.getNote();
    }

}
