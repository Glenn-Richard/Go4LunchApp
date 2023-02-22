package MVVM;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import models.User;

public class FirebaseRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reference = db.collection("users");
    public MutableLiveData<List<User>> users = new MutableLiveData<>();
    public final MutableLiveData<User> currentUser = new MutableLiveData<>();

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
    public void updateUser(User user,String field,String value){
        reference.document(user.getId())
                .update(field,FieldValue.arrayUnion(value))
                .addOnCompleteListener(task -> Log.d("USER:"+ user.getId(), "UPDATE"))
                .addOnFailureListener(e -> Log.e("USER_UPDATE: ",e.toString()));
    }
    public void deleteField(User user,String field,String value){
        reference.document(user.getId())
                .update(field,FieldValue.arrayRemove(value))
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
    public void userAlreadyExist(FirebaseUser user){

        List<User> users = this.users.getValue();
        boolean found = false;
        for (int i = 0; i< Objects.requireNonNull(users).size()-1; i++){
            if (users.get(i).getId().equals(user.getUid())){
                found = true;
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

