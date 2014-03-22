package com.tomclaw.mandarin.main;

import android.content.Context;
import android.os.RemoteException;
import android.widget.Toast;
import com.tomclaw.mandarin.R;
import com.tomclaw.mandarin.core.PleaseWaitTask;
import com.tomclaw.mandarin.core.ServiceInteraction;
import com.tomclaw.mandarin.core.exceptions.AccountNotFoundException;

import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * Created by solkin on 12/27/13.
 */
public class AccountsRemoveTask extends PleaseWaitTask {

    private final Collection<Integer> selectedAccounts;
    protected final WeakReference<ChiefActivity> weakChiefActivity;

    public AccountsRemoveTask(ChiefActivity activity, Collection<Integer> selectedAccounts) {
        super(activity);
        this.selectedAccounts = selectedAccounts;
        this.weakChiefActivity = new WeakReference<ChiefActivity>(activity);
    }

    @Override
    public void executeBackground() throws AccountNotFoundException, RemoteException {
        ChiefActivity chiefActivity = weakChiefActivity.get();
        if (chiefActivity != null) {
            ServiceInteraction serviceInteraction = chiefActivity.getServiceInteraction();
            // Iterating for all selected positions.
            for (int accountDbId : selectedAccounts) {
                // Trying to remove account.
                serviceInteraction.removeAccount(accountDbId);
            }
        }
    }

    @Override
    public void onFailMain() {
        Context context = getWeakContext().get();
        if (context != null) {
            // Show error.
            Toast.makeText(context, R.string.error_remove_account, Toast.LENGTH_LONG).show();
        }
    }
}
