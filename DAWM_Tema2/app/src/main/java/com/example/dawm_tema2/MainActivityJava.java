package com.example.dawm_tema2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.dawm_tema2.adapters.AnimalAdapter;
import com.example.dawm_tema2.database.Animal;
import com.example.dawm_tema2.database.AnimalDao;
import com.example.dawm_tema2.database.AnimalDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivityJava extends AppCompatActivity {
    private AnimalDao m_animalDao;
    private List<Animal>animalsList;
    AnimalAdapter adapter;
    @SuppressLint("WrongThread")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatButton submit=findViewById(R.id.submit_button);
        AnimalDatabase db= Room.databaseBuilder(getApplicationContext(),AnimalDatabase.class,"animals").build();
       List<Animal>animalList=new ArrayList<>();
        RecyclerView recyclerView=findViewById(R.id.animals);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                List<Animal>animals=AnimalDatabase.getInstance(getApplicationContext()).animalDao().getAllAnimals();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        adapter=new AnimalAdapter(animals);
                        recyclerView.setAdapter(adapter);

                    }
                });
            }


        });
        thread.start();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText animalName=findViewById(R.id.animal_name_input);
                EditText continentName=findViewById(R.id.continent_input);
                String animalInput=animalName.getText().toString();
                String continentInput=continentName.getText().toString();

                Animal animal=new Animal(animalInput,continentInput);
                InsertAsyncTask insertAsyncTask=new InsertAsyncTask();
                insertAsyncTask.execute(animal);

            }

        });

    }
    public void getAllAnimals(View view)
    {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                List<Animal>animals=AnimalDatabase.getInstance(getApplicationContext()).animalDao().getAllAnimals();
                Log.e("TheBossCatalin","run"+animals.toString());
            }
        });
        thread.start();
    }
    class InsertAsyncTask extends AsyncTask<Animal,Void,Void>{
        @Override
        protected Void doInBackground(Animal...animals)
        {
            AnimalDatabase.getInstance(getApplicationContext()).animalDao().insertAnimal(animals[0]);

            Log.e("SUCCES",animals[0].animalName);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<Animal>animalList=AnimalDatabase.getInstance(getApplicationContext()).animalDao().getAllAnimals();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setAnimals(animalList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }

    }
}