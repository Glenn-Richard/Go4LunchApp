package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunchapp.R;

import java.util.List;

import models.User;

public class WorkmatesFragmentAdapter extends RecyclerView.Adapter<WorkmatesFragmentAdapter.WorkmatesViewHolder>{

    Context context;
    List<User> users;

    public WorkmatesFragmentAdapter(Context context) {
        this.context = context;
    }

    public void setAdapter(List<User> users){
        this.users = users;
    }
    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_workmates,parent,false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {

        holder.name.setText(users.get(position).getName());
        if (!(users.get(position).getPhoto()==null)){
            Glide.with(context)
                    .load(users.get(position).getPhoto())
                    .fitCenter()
                    .into(holder.photo_user);
        }

    }

    @Override
    public int getItemCount() {

        if (users == null){
            return 0;
        }else{
            return users.size();
        }
    }

    public static class WorkmatesViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView photo_user;
        ConstraintLayout container;

        public WorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);

            photo_user = itemView.findViewById(R.id.user_photo);

            container = itemView.findViewById(R.id.workmates_item_view);
        }
    }
}
