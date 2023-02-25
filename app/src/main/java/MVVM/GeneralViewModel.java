package MVVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import api.DetailsPlaces;
import api.JSONResponse;
import models.Restaurant;
import models.User;

public class GeneralViewModel extends ViewModel{

    private final FirebaseRepository firebaseRepository = new FirebaseRepository();
    private final RetrofitRepository retrofitRepository = new RetrofitRepository();


    public GeneralViewModel(){
        firebaseRepository.getUsers();
    }

    //GETTER
    public MutableLiveData<List<User>> getUserList() {
        return firebaseRepository.users;
    }
    public LiveData<User> getCurrentUser(String id){
        firebaseRepository.getCurrentUser(id);
        return firebaseRepository.currentUser;
    }
    public LiveData<JSONResponse> getPlaceResultLiveData(String loc){
        return retrofitRepository.getPlaceResultsLiveData(loc);
    }
    public LiveData<DetailsPlaces> getDetailsPlaceLiveData(String place_id){
        return retrofitRepository.getRestaurantsDetails(place_id);
    }
    public LiveData<Boolean> isFavorite(Restaurant restaurant){
        return firebaseRepository.isFavorite(restaurant);
    }
    public LiveData<Boolean> isSelected(Restaurant restaurant){
        return firebaseRepository.isSelected(restaurant);
    }
    public LiveData<Integer> getWorkmatesCount(Restaurant restaurant){
        return firebaseRepository.getWorkmatesCount(restaurant);
    }

    //SETTER
    public void setUserList(User user){
        firebaseRepository.setUser(user);
    }
    public void updateFavoritesUser(User user, String field,String value){
        firebaseRepository.updateFavoritesUser(user,field,value);
    }
    public void updateUser(User user,String field,Object value){
        firebaseRepository.updateUser(user,field,value);
    }
    public void deleteFavoritesField(User user, String field, String value){
        firebaseRepository.deleteFavoritesField(user,field,value);
    }
    public void deleteField(User user,String field,Object value){
        firebaseRepository.deleteField(user,field,value);
    }
    public LiveData<Boolean> setChoice(Restaurant restaurant){
        return firebaseRepository.setChoice(restaurant);
    }
    public  LiveData<Boolean> setFavorite(Restaurant restaurant){
        return firebaseRepository.setFavorites(restaurant);
    }

    //CHECKER
    public void userAlreadyExist(FirebaseUser user){
        firebaseRepository.userAlreadyExist(user);
    }

}
