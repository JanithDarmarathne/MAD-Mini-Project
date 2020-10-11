package com.example.eshoppingmanagementsytemuser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eshoppingmanagementsytemuser.Models.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageTypeViewHolder>  {

    private ArrayList<Product> dataList;
    private Context context;
    public onItemClicked mListner;

    public interface onItemClicked{
        void OnItemClick(int index);

    }

    public void SetOnItemClickListener(onItemClicked listner){
        mListner = listner;
    }

    public CartAdapter(ArrayList<Product> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public class ImageTypeViewHolder extends RecyclerView.ViewHolder{

        TextView productName,productMade,productPrice;
        ImageView imageView;

        public ImageTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            this.productName = itemView.findViewById(R.id.cartProduct);
            this.productMade = itemView.findViewById(R.id.cartMade);
            this.productPrice = itemView.findViewById(R.id.cartPrice);
            this.imageView = itemView.findViewById(R.id.cartImage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListner.OnItemClick(position);
                        }
                    }
                }
            });


        }
    }


    @NonNull
    @Override
    public ImageTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cartlist,viewGroup,false);

        return new ImageTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageTypeViewHolder imageTypeViewHolder, int i) {


        Product model = dataList.get(i);

        imageTypeViewHolder.productName.setText(model.Name);
        imageTypeViewHolder.productMade.setText(model.productMade);
        String imageUrl = model.ImageUrl;
        imageTypeViewHolder.productPrice.setText(model.productPrice);

        // Picasso.get().load(imageUrl).fit().centerInside().into(imageTypeViewHolder.imageView);
        //    imageTypeViewHolder.imageView.setImageURI(imageUrl);
        Glide.with(context).load(imageUrl).into(imageTypeViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

