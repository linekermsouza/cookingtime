package com.udacity.lineker.cookingtime.home;

import android.arch.lifecycle.LiveData;
import com.udacity.lineker.cookingtime.model.Receipt;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import java.util.List;

public class ReceiptsViewModel extends AndroidViewModel {
    private LiveData<List<Receipt>> receiptListObservable;

    public ReceiptsViewModel(Application application) {
        super(application);
        receiptListObservable = ReceiptRepository.getInstance().getReceipts();
    }

    public LiveData<List<Receipt>> getReceiptListObservable() {
        return receiptListObservable;
}

    public void forceUpdate() {
        ReceiptRepository.getInstance().clearCache();
        receiptListObservable = ReceiptRepository.getInstance().getReceipts();
    }
}

