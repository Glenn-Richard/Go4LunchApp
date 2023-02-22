package MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import api.DetailsPlaces;
import api.JSONResponse;
import models.User;

public class FirebaseViewModel extends ViewModel{

    private final FirebaseRepository firebaseRepository = new FirebaseRepository();
    private final RetrofitRepository retrofitRepository = new RetrofitRepository();


    public FirebaseViewModel(){
        firebaseRepository.getUsers();
    }

    //GETTER
    public MutableLiveData<List<User>> getUserList() {
        return firebaseRepository.users;
    }
    public LiveData<User> getCurrentUser(String id){
        firebaseRepository.getCurrentUser(id);
        synchronized (firebaseRepository.currentUser){
            firebaseRepository.currentUser.notify();
        }
        return firebaseRepository.currentUser;
    }
    public LiveData<JSONResponse> getPlaceResultLiveData(String loc){
        return retrofitRepository.getPlaceResultsLiveData(loc);
    }
    public LiveData<DetailsPlaces> getDetailsPlaceLiveData(String place_id){
        return retrofitRepository.getRestaurantsDetails(place_id);
    }

    //SETTER
    public void setUserList(User user){
        firebaseRepository.setUser(user);
    }
    public void updateUser(User user, String field,String value){
        firebaseRepository.updateUser(user,field,value);
        synchronized (user){
            user.notify();
        }
    }
    public void deleteField(User user,String field,String value){
        firebaseRepository.deleteField(user,field,value);
        synchronized (user){
            user.notify();
        }
    }

    //CHECKER
    public void userAlreadyExist(FirebaseUser user){
        firebaseRepository.userAlreadyExist(user);
    }

}
