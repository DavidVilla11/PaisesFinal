package com.dvj.paises;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.dvj.paises.databinding.FragmentPaisBinding;
import com.dvj.paises.placeholder.PlaceholderContent.Pais;

import java.io.InputStream;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pais}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PaisRecyclerViewAdapter extends RecyclerView.Adapter<PaisRecyclerViewAdapter.ViewHolder> {

    private final List<Pais> mValues;

    public PaisRecyclerViewAdapter(List<Pais> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentPaisBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        InputStream is = getClass().getResourceAsStream("/" + mValues.get(position).bandera);
        holder.mImageView.setImageDrawable(Drawable.createFromStream(is,""));
        holder.mContentView.setText(mValues.get(position).nombre);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;
        public final TextView mContentView;
        public Pais mItem;

        public ViewHolder(FragmentPaisBinding binding) {
            super(binding.getRoot());
            mImageView =binding.imageView;
            mContentView = binding.content;
            binding.getRoot().setOnClickListener(this);
        }

        public void onClick(View v){
            Bundle args = new Bundle();
            args.putParcelable("pais", mItem);
            args.putString("tittle", mItem.nombre);

            Navigation.findNavController(v)
                    .navigate(R.id.action_paisesFragment_to_detallesPaisFragment, args);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}