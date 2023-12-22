package pt.uc.dei.cm.plantsmc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {
    private MutableLiveData<FirebaseUser> currentUser;

    public UserViewModel() {
        currentUser = new MutableLiveData<>();
        currentUser.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    // You can add methods to update this user data as needed
    // For example, after a successful login or logout
    public void setUser(FirebaseUser user) {
        currentUser.setValue(user);
    }
}

