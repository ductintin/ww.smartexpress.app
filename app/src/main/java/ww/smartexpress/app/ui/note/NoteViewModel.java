package ww.smartexpress.app.ui.note;

import androidx.databinding.ObservableField;

import org.greenrobot.eventbus.EventBus;

import ww.smartexpress.app.MVVMApplication;
import ww.smartexpress.app.constant.Constants;
import ww.smartexpress.app.data.Repository;
import ww.smartexpress.app.data.model.api.response.Note;
import ww.smartexpress.app.ui.base.activity.BaseViewModel;
import ww.smartexpress.app.ui.payment.PaymentActivity;

public class NoteViewModel extends BaseViewModel {
    public ObservableField<String> textNote = new ObservableField<>("");
    public ObservableField<Integer> noteHint = new ObservableField<>(0);
    public NoteViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void doBack(){
        getApplication().getCurrentActivity().onBackPressed();
    }
    public void doSaveNote(){

        if(noteHint.get() == 0){
            Note note = new Note(textNote.get());
            EventBus.getDefault().post(note);
        }
        getApplication().getCurrentActivity().onBackPressed();
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textNote.set(s.toString());
    }

    public void getNote(){
        String note = repository.getSharedPreferences().getStringVal(Constants.CUSTOMER_BOOKING_NOTE);
        if(note != null){
            textNote.set(note);
        }
    }
}
