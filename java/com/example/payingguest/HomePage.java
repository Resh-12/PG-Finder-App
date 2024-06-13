package com.example.payingguest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.payingguest.adapter.HomeAdapter;
import com.example.payingguest.adapter.WishlistAdapter;
import com.example.payingguest.fragments.WishFrag;
import com.example.payingguest.interfaces.WishlistListener;
import com.example.payingguest.model.homesInFeedModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements HomeAdapter.WishlistListener{
    FirebaseAuth mAuth;
TextView sort,filter,text;
    private static final int FILTER_REQUEST_CODE = 1;
//WishlistListener listener;
LinearLayoutManager mLayoutManager;//for sorting
    SharedPreferences mSharedPref;//for saving sort settings
    private DatabaseReference HomeRef;
    private RecyclerView recyclerView;
    HomeAdapter homeAdapter;

    RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    ArrayList<homesInFeedModel> list;
    String ism,isw,isa,ish;
    float minPrice, maxPrice;
    ArrayList<String> selectedCities;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        auth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        sort=findViewById(R.id.sort);
        filter=findViewById(R.id.filter);
        text=findViewById(R.id.t);
        isa= getIntent().getExtras().get("apart").toString();
        ish= getIntent().getExtras().get("home").toString();
        ism= getIntent().getExtras().get("male").toString();
        isw= getIntent().getExtras().get("female").toString();
        mSharedPref=getSharedPreferences("SortSettings",MODE_PRIVATE);
        String mSorting=mSharedPref.getString("Sort","newest");
        if(mSorting.equals("newest")){
            mLayoutManager=new LinearLayoutManager(this);
            //will display newest first
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        } else if (mSorting.equals("oldest")) {
            mLayoutManager=new LinearLayoutManager(this);
            //will display oldest first
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }
        recyclerView =findViewById(R.id.recycler_menu1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(mLayoutManager);
        list=new ArrayList<>();
//        wishlistItems=new ArrayList<>();

        homeAdapter=new HomeAdapter(this,list);
//        homeAdapter=new HomeAdapter( this,list );

        recyclerView.setAdapter(homeAdapter);
        mAuth = FirebaseAuth.getInstance();
        HomeRef = FirebaseDatabase.getInstance().getReference().child("Rent_posts");
        auth= FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users");
        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(HomePage.this,Filter.class);
                startActivityForResult(in, FILTER_REQUEST_CODE);
            }
        });

        HomeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<homesInFeedModel> tempList = new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    homesInFeedModel pgs = dataSnapshot.getValue(homesInFeedModel.class);
////                    pgs.setAvailable(true);
//                    String pid = dataSnapshot.child("pId").getValue(String.class);
//                    isItemBooked(pid, new ItemBookedCallback() {
//                        @Override
//                        public void onItemBooked(boolean isBooked) {
////                            if (isBooked) {
////                                pgs.setAvailable(false);// Set isAvailable to false
////                                Toast.makeText(HomePage.this, "false", Toast.LENGTH_SHORT).show();
////                            }
//                            pgs.setBooked(isBooked);
////                            list.add(pgs);
////                            homeAdapter.notifyDataSetChanged();
//                        }
//                    });
                    if(ism.equals("true")){
                        if(isa.equals("true") && ish.equals("true")) {
                            if (dataSnapshot.child("gender").getValue().equals("Male")) {
                                homesInFeedModel pg = dataSnapshot.getValue(homesInFeedModel.class);
                                list.add(pg);
                                text.setVisibility(View.INVISIBLE);
                            }
                        }
                        else if(isa.equals("true")){
                            if (dataSnapshot.child("gender").getValue().equals("Male") && dataSnapshot.child("roomType").getValue().equals("Apartment")) {
                                homesInFeedModel pg = dataSnapshot.getValue(homesInFeedModel.class);
                                list.add(pg);
                                text.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            if (dataSnapshot.child("gender").getValue().equals("Male") && dataSnapshot.child("roomType").getValue().equals("Home")) {
                                homesInFeedModel pg = dataSnapshot.getValue(homesInFeedModel.class);
                                list.add(pg);
                                text.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    if(isw.equals("true")){
                        if(isa.equals("true") && ish.equals("true")) {
                            if (dataSnapshot.child("gender").getValue().equals("Female")) {
                                homesInFeedModel pg = dataSnapshot.getValue(homesInFeedModel.class);
                                list.add(pg);
                                text.setVisibility(View.INVISIBLE);
                            }
                        }
                        else if(isa.equals("true")){
                            if (dataSnapshot.child("gender").getValue().equals("Female") && dataSnapshot.child("roomType").getValue().equals("Apartment")) {
                                homesInFeedModel pg = dataSnapshot.getValue(homesInFeedModel.class);
                                list.add(pg);
                                text.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            if (dataSnapshot.child("gender").getValue().equals("Female") && dataSnapshot.child("roomType").getValue().equals("Home")) {
                                homesInFeedModel pg = dataSnapshot.getValue(homesInFeedModel.class);
                                list.add(pg);
                                text.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                }
//                list.addAll(tempList);
                homeAdapter.notifyDataSetChanged();
//                return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void isItemBooked(String pid,ItemBookedCallback callback) {
//        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference().child("Booked_pg");
//        Query query = bookingRef.orderByChild("pId").equalTo(pid);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean isBooked = snapshot.exists();
//                callback.onItemBooked(isBooked);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(HomePage.this, "error", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }

//    private void applyFilter(float minValue, float maxValue, boolean isUdupiSelected, boolean isKundapurSelected, boolean isKarkalSelected, boolean isBhrSelected) {
//        ArrayList<homesInFeedModel> filteredList = new ArrayList<>();
//
//        for (homesInFeedModel pg : list) {
//            String city = pg.getCity();
//            float rent = Float.parseFloat(pg.getMonthlyRent());
//            boolean isPriceInRange = (rent >= minValue && rent <= maxValue);
//            if (isPriceInRange) {
//                // Check if any cities are selected
//                if (!isUdupiSelected && !isKundapurSelected && !isKarkalSelected && !isBhrSelected) {
//                    filteredList.add(pg);
//                } else {
//                    // Check if the home matches the selected criteria
////                    boolean isCitySelected =
//                    if ((isUdupiSelected && city.equals("Udupi"))
//                            || (isKundapurSelected && city.equals("Kundapur"))
//                            || (isKarkalSelected && city.equals("Karkala"))
//                            || (isBhrSelected && city.equals("Bhramavar"))) {
//                        filteredList.add(pg);
//
////            boolean isPriceInRange = (rent >= minValue && rent <= maxValue);
//
////            if (isCitySelected && isPriceInRange) {
////                filteredList.add(pg);
////                text.setVisibility(View.INVISIBLE);
////            }
//                }
//                }
//            }
//
//        }
//
//        homeAdapter.updateData(filteredList); // Update the adapter with the filtered list
//    }
private void applyFilter(float minValue, float maxValue, boolean isUdupiSelected, boolean isKundapurSelected, boolean isKarkalSelected, boolean isBhrSelected) {
    ArrayList<homesInFeedModel> filteredList = new ArrayList<>();

    for (homesInFeedModel pg : list) {
        String city = pg.getCity();
        float rent = Float.parseFloat(pg.getMonthlyRent());

        // Check if any cities are selected
        if ((!isUdupiSelected && !isKundapurSelected && !isKarkalSelected && !isBhrSelected) ||
                (isUdupiSelected && isKundapurSelected && isKarkalSelected && isBhrSelected)) {
            // No cities selected or all cities selected, apply filter based on price range only
            boolean isPriceInRange = (rent >= minValue && rent <= maxValue);
            if (isPriceInRange) {
                filteredList.add(pg);
            }
        } else {
            // Cities are selected, apply filter based on both price range and cities
            boolean isCitySelected = (isUdupiSelected && city.equals("Udupi"))
                    || (isKundapurSelected && city.equals("Kundapur"))
                    || (isKarkalSelected && city.equals("Karkala"))
                    || (isBhrSelected && city.equals("Bhramavar"));

            boolean isPriceInRange = (rent >= minValue && rent <= maxValue);

            if (isCitySelected && isPriceInRange) {
                filteredList.add(pg);
            }
        }
    }

    homeAdapter.updateData(filteredList); // Update the adapter with the filtered list
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Retrieve the filter criteria from the returned intent
            float minPrice = data.getFloatExtra("minPrice", 2000f);
            float maxPrice = data.getFloatExtra("maxPrice", 10000f);
            ArrayList<String> selectedCities = data.getStringArrayListExtra("selectedCities");

            // Apply the filter with the provided price range and selected cities
            applyFilter(minPrice, maxPrice, selectedCities.contains("Udupi"), selectedCities.contains("Kundapur"), selectedCities.contains("Karkala"), selectedCities.contains("Bhramavar"));
        }
    }



//    private void applyFilter(float minPrice, float maxPrice, ArrayList<String> selectedCities) {
//        List<homesInFeedModel> filteredList = new ArrayList<>();
//
//        for (homesInFeedModel item : filteredList) {
//            float price = Float.parseFloat(item.getMonthlyRent());
//            String city = item.getCity();
//
//            // Apply the price range filter
//            if (price >= minPrice && price <= maxPrice) {
//                // Apply the city filter
//                if (selectedCities.isEmpty() || selectedCities.contains(city)) {
//                    // Item matches the filter criteria
//                    filteredList.add(item);
//                }
//            }
//        }
//
//        // Update the list used by the HomeAdapter with the filtered data
//        homeAdapter.updateData(filteredList);
//    }
//    public void onItemChecked(homesInFeedModel item, boolean isChecked) {
//        if (isChecked) {
//            wishlistItems.add(item);
//        } else {
//            wishlistItems.remove(item);
//        }
//        // Notify the adapter to update the UI
//        homeAdapter.notifyDataSetChanged();
//    }

    private void showSortDialog() {
        String[] sortoptions={"Newest","Oldest"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setIcon(R.drawable.sort)
                .setItems(sortoptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     if(which==0){
                         //sort by newest
                         SharedPreferences.Editor editor=mSharedPref.edit();
                         editor.putString("Sort","newest");
                         editor.apply();
                         recreate();
                     } else if (which==1) {
                         //sort by oldest
                         SharedPreferences.Editor editor=mSharedPref.edit();
                         editor.putString("Sort","oldest");
                         editor.apply();
                         recreate();
                     }
                    }
                });
        builder.show();
    }

    @Override
    public void onItemAddedToWishlist(homesInFeedModel item) {
        String uid=getCurrentUser();
        String itemId = item.getpId(); // Assuming you have an ID field in homesInFeedModel
        DatabaseReference wishRef = FirebaseDatabase.getInstance().getReference().child("wish");

        DatabaseReference itemRef = wishRef.child(itemId);

        item.setUser(uid);



        itemRef.setValue(item);
        Toast.makeText(this, "Pg added to wishlist", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemRemovedFromWishlist(homesInFeedModel item) {
        String itemId = item.getpId(); // Assuming you have an ID field in homesInFeedModel
        DatabaseReference wishRef = FirebaseDatabase.getInstance().getReference().child("wish");
        wishRef.child(itemId).removeValue();
        Toast.makeText(this, "Pg removed from wishlist", Toast.LENGTH_SHORT).show();
    }
    public String getCurrentUser() {

        user=auth.getCurrentUser();
        if(user!=null){
            return user.getEmail();
        }else{
            return null;
        }

    }
    private interface ItemBookedCallback {
        void onItemBooked(boolean isBooked);
    }


    //   @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<homesInFeedModel>  option = new FirebaseRecyclerOptions.Builder<homesInFeedModel>().setQuery(HomeRef, homesInFeedModel.class).build();
//        FirebaseRecyclerAdapter<homesInFeedModel, homesInFeed> adapter =new FirebaseRecyclerAdapter<homesInFeedModel, homesInFeed>(option) {
//            @Override
//            protected void onBindViewHolder(@NonNull homesInFeed holder, int position, @NonNull homesInFeedModel model) {
//                holder.txtrent.setText(model.getMonthlyRent());
//                holder.txtroomtype.setText(model.getRoomType());
//                holder.txtloc.setText(model.getLocality());
//                holder.txtcity.setText(model.getCity());
//                Picasso.get().load(model.getImage()).into(holder.HIFpic);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(HomePage.this, HomeDetails.class);
//                        intent.putExtra("pId", model.getpId());
//                        startActivity(intent);
//                        //Toast.makeText(HomePage.this, ""+model.getpId(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public homesInFeed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_in_feed_design, parent, false);
//                homesInFeed holder = new homesInFeed(view);
//                return holder;
//            }
//        };
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }
}