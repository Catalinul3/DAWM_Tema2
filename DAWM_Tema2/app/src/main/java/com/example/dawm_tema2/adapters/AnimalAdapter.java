package com.example.dawm_tema2.adapters;

import android.app.Activity;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dawm_tema2.R;
import com.example.dawm_tema2.database.Animal;
import com.example.dawm_tema2.database.AnimalDatabase;

import java.util.List;
import android.os.Handler;

public class AnimalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Animal> animals;

Handler handler;

    public AnimalAdapter(List<Animal> animals) {
        this.animals = animals;

    }



    public void setAnimals(List<Animal> animale) {
        animals = animale;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animals_and_continents, parent, false);
        AnimalViewHolder viewHolder = new AnimalViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Animal animal = animals.get(position);
        ((AnimalViewHolder) holder).bind(animal);
        ((AnimalViewHolder)holder).animalAdapter=this;
        handler=new Handler(Looper.getMainLooper());
        ((AnimalViewHolder) holder).deleteButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int pos = holder.getAdapterPosition();
                        Animal animalDeleted = animals.get(pos);
                        AnimalDatabase.getInstance(v.getContext()).animalDao().deleteAnimal(animalDeleted);
                        animals.remove(position);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((AnimalViewHolder)holder).animalAdapter.notifyDataSetChanged();
                            }
                        });



                    }

                });

                thread.start();
            }


        });
    }
@Override
    public int getItemCount()
{
    return animals.size();
}

public class AnimalViewHolder extends RecyclerView.ViewHolder{
    TextView animalTextView;
    TextView continentTextView;
AppCompatButton deleteButtonView;
AnimalAdapter animalAdapter;
Handler handler;
public AppCompatButton getDeleteButtonView()
{
    return deleteButtonView;
}
    public AnimalViewHolder(@NonNull View itemView)
    {
        super(itemView);

        continentTextView=itemView.findViewById(R.id.nameOfContinent);
        animalTextView=itemView.findViewById(R.id.nameOfAnimal);
        deleteButtonView=itemView.findViewById(R.id.delete_button);
    }
    public void bind(Animal animalModel)
    {
        animalTextView.setText(animalModel.getAnimalName());
        continentTextView.setText(animalModel.getContinentName());
    }
}
}
