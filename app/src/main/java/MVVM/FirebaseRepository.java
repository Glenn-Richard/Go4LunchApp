package MVVM;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import models.Restaurant;
import models.User;

public class FirebaseRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reference = db.collection("users");
    public final MutableLiveData<List<User>> users = new MutableLiveData<>();
    public final MutableLiveData<User> currentUser = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isFav = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isSelect = new MutableLiveData<>();
    public  final MutableLiveData<Integer> count = new MutableLiveData<>();

    public FirebaseRepository() {
    }

    public void getUsers(){
        reference
                .get()
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<User> users = task.getResult().toObjects(User.class);
                this.users.postValue(users);
                Log.d("GET_USERS", "getUsers: data had been catch size: "+task.getResult().size());
            }else{
                Log.e("FIR_REPO: ","ERROR");
            }
        })
                .addOnFailureListener(e -> Log.e("Firestore Reading:",e.toString()));
    }
    public void setUser(User user){
        reference.document(user.getId())
                .set(user)
                .addOnSuccessListener(documentReference ->
                        Log.d("FIRESTORE_WRITING","USER_ADDED"))
                .addOnFailureListener(e ->
                        Log.d("FIRESTORE_ERROR",e.toString()));
    }
    public void updateFavoritesUser(User user, String field, String value){
        reference.document(user.getId())
                .update(field,FieldValue.arrayUnion(value))
                .addOnCompleteListener(task -> Log.d("USER:"+ user.getId(), "UPDATE"))
                .addOnFailureListener(e -> Log.e("USER_UPDATE: ",e.toString()));
    }
    public void updateUser(User user, String field, Object value){
        reference.document(user.getId())
                .update(field,value)
                .addOnCompleteListener(task -> Log.d("USER:"+ user.getId(), "UPDATE"))
                .addOnFailureListener(e -> Log.e("USER_UPDATE: ",e.toString()));
    }
    public void deleteFavoritesField(User user, String field, String value){
        reference.document(user.getId())
                .update(field,FieldValue.arrayRemove(value))
                .addOnCompleteListener(task -> Log.d("DELETE: ", String.valueOf(task.isSuccessful())))
                .addOnFailureListener(e -> Log.e("DELETE: ",e.toString()));
    }
    public void deleteField(User user,String field,Object value){
        reference.document(user.getId())
                .update(field,value)
                .addOnCompleteListener(task -> Log.d("DELETE: ", String.valueOf(task.isSuccessful())))
                .addOnFailureListener(e -> Log.e("DELETE: ",e.toString()));
    }
    public void getCurrentUser(String id) {
        reference.document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        currentUser.postValue(user);
                    } else {
                    Log.d(TAG, "Aucun utilisateur trouvé avec l'ID $userId");
                    }
                });
    }
    public LiveData<Boolean> isFavorite(Restaurant restaurant){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();
        reference.document(Objects.requireNonNull(user1).getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        if (Objects.requireNonNull(user).getFavorites()!=null){
                            if (Objects.requireNonNull(user).getFavorites().contains(restaurant.getPlace_id())){
                                isFav.postValue(true);
                            }
                        }
                    } else {
                        isFav.postValue(false);
                    }
                });
        return isFav;
    }
    public LiveData<Boolean> isSelected(Restaurant restaurant){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();
        reference.document(Objects.requireNonNull(user1).getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        if (Objects.requireNonNull(user).getChoice()!=null){
                            if (Objects.requireNonNull(user)
                                    .getChoice()
                                    .getPlace_id()
                                    .equals(restaurant.getPlace_id())){
                                isSelect.postValue(true);
                            }
                        }
                    } else {
                        isSelect.postValue(false);
                    }
                });
        return isSelect;
    }
    public LiveData<Boolean> setChoice(Restaurant restaurant){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();
        reference.document(Objects.requireNonNull(user1).getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        if (user.getChoice()==null){
                            restaurant.setNumber_of_workmates(restaurant.getNumber_of_workmates()+1);
                            updateUser(user,"choice",restaurant);
                            isSelect.postValue(true);
                        }else{
                            if(user.getChoice().getPlace_id().equals(restaurant.getPlace_id())){
                                restaurant.setNumber_of_workmates(restaurant.getNumber_of_workmates()-1);
                                deleteField(user,"choice",FieldValue.delete());
                                isSelect.postValue(false);
                            }else{
                                restaurant.setNumber_of_workmates(restaurant.getNumber_of_workmates()+1);
                                updateUser(user,"choice",restaurant);
                                isSelect.postValue(true);
                            }
                        }
                    }
                });
        return isSelect;

    }
    public LiveData<Boolean> setFavorites(Restaurant restaurant){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();
        String value = restaurant.getPlace_id();

        reference.document(Objects.requireNonNull(user1).getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        if (user.getFavorites()!=null){
                            if (user.getFavorites().contains(restaurant.getPlace_id())){
                                deleteFavoritesField(user,"favorites",value);
                                isFav.postValue(false);
                            }else{
                                updateFavoritesUser(user, "favorites", value);
                                isFav.postValue(true);
                            }
                        }else{
                            updateFavoritesUser(user, "favorites", value);
                            isFav.postValue(true);
                        }
                    }
                });
        return isFav;
    }
    public LiveData<Integer> getWorkmatesCount(Restaurant restaurant){
        reference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<User> users = task.getResult().toObjects(User.class);
                        int i = 0;
                        for (User user : users) {
                            if (user.getChoice()!=null){
                                if (user.getChoice().getPlace_id().equals(restaurant.getPlace_id())){
                                    i++;
                                    count.setValue(i);
                                }
                            }
                        }
                        restaurant.setNumber_of_workmates(i);
                    }
                })
                .addOnFailureListener(e ->
                        Log.e("COUNT",e.getLocalizedMessage()
                        ));
        return count;
    }
    public void userAlreadyExist(FirebaseUser user){

        List<User> users = this.users.getValue();
        boolean found = false;

        if (users != null) {
            for (User user1 : users){
                if (user1.getId().equals(user.getUid())){
                    found = true;
                }
            }
        }
        if(!found){
            User user1 = new User();
            user1.setId(Objects.requireNonNull(user).getUid());
            user1.setName(user.getDisplayName());
            user1.setEmail(user.getEmail());
            user1.setPhoto(Objects.requireNonNull(user.getPhotoUrl()).toString());
            setUser(user1);
        }
    }
}

