package com.developer.abhinavraj.servify_app.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.database.models.ServiceProvider;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    public BookListener onClickListener;
    private List<ServiceProvider> serviceProviders;
    private Context context;

    public ServiceAdapter(Context context, List<ServiceProvider> serviceProviders, BookListener bookListener) {
        this.serviceProviders = serviceProviders;
        this.context = context;
        this.onClickListener = bookListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View countryView = inflater.inflate(R.layout.service_item, parent, false);
        return new ViewHolder(countryView, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ServiceProvider serviceProvider = serviceProviders.get(position);

        ImageView profileImage = holder.profileImage;
        TextView profileName = holder.profileName;
        TextView profileAge = holder.profileAge;
        TextView profileDistance = holder.profileDistance;
        TextView profileRating = holder.profileRating;

//        String mFirstName = serviceProvider.getFirstName();
//        String mLastName = serviceProvider.getLastName();
//        String mProfileName = mFirstName + " " + mLastName;
//        profileName.setText(mProfileName);
//        profileAge.setText(serviceProvider.getAge());
//        profileDistance.setText(serviceProvider.getCity());
//        profileRating.setText(String.valueOf(serviceProvider.getRating()));

        /*profileImage.setImageDrawable(
                AvatarGenerator.Companion.avatarImage(
                        context, 200,
                        AvatarConstants.Companion.getCIRCLE(),
                        serviceProvider.getFirstName()
                ));

         */

        profileName.setText(String.format("%s %s", serviceProvider.getFirstName(), serviceProvider.getLastName()));
        profileAge.setText(serviceProvider.getAge());
        profileDistance.setText(serviceProvider.getGender().equals("0") ? "M" : "F");
        profileRating.setText(String.valueOf(serviceProvider.getRating()));
    }

    @Override
    public int getItemCount() {
        return serviceProviders.size();
    }

    public interface BookListener {
        void OnClick(View v, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        TextView profileName, profileAge, profileDistance, profileRating;
        ImageView profileImage;

        ViewHolder(View view, BookListener onClickListener) {
            super(view);
            cardView = view.findViewById(R.id.cardView);
            profileImage = view.findViewById(R.id.profile_image);
            profileName = view.findViewById(R.id.profile_name);
            profileAge = view.findViewById(R.id.profile_age);
            profileDistance = view.findViewById(R.id.profile_distance);
            profileRating = view.findViewById(R.id.profile_rating);

            cardView.setOnLongClickListener(v -> {
                onClickListener.OnClick(v, getAdapterPosition());
                return true;
            });
        }
    }
}
