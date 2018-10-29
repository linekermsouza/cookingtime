package com.udacity.lineker.cookingtime.home;

import android.arch.lifecycle.LiveData;

import com.udacity.lineker.cookingtime.database.AppDatabase;
import com.udacity.lineker.cookingtime.database.ReceiptEntry;
import com.udacity.lineker.cookingtime.model.Receipt;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import java.util.List;

public class ReceiptsViewModel extends AndroidViewModel {
    private LiveData<List<Receipt>> receiptListObservable;
    private LiveData<List<ReceiptEntry>> receiptDbObservable;

    public ReceiptsViewModel(Application application) {
        super(application);
        receiptListObservable = ReceiptRepository.getInstance().getReceipts();
    }

    public LiveData<List<Receipt>> getReceiptListObservable() {
        return receiptListObservable;
    }

    public LiveData<List<ReceiptEntry>> getReceiptDbObservable(AppDatabase database) {
        receiptDbObservable = database.receiptDao().loadAll();
        return receiptDbObservable;
    }

    public void forceUpdate() {
        ReceiptRepository.getInstance().clearCache();
        receiptListObservable = ReceiptRepository.getInstance().getReceipts();
    }
}

