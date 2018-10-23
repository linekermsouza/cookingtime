package com.udacity.lineker.cookingtime.ui;

import com.udacity.lineker.cookingtime.model.MasterListItem;
import com.udacity.lineker.cookingtime.model.Receipt;

public interface ItemClickCallback {
    void onClick(MasterListItem item);
}