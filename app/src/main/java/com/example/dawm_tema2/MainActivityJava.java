package com.example.dawm_tema2;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    private Boolean find;
    private Boolean notInputContinent=false;
    private Boolean notInputAnimal=false;
    AnimalAdapter adapter;

    public void setFind(Boolean find) {
        this.find = find;
    }

    @SuppressLint("WrongThread")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatButton submit=findViewById(R.id.submit_button);
        AnimalDatabase db= Room.databaseBuilder(getApplicationContext(),AnimalDatabase.class,"animals").build();
       List<Animal>animalList=new ArrayList<>();
        RecyclerView recyclerView=findViewById(R.id.animals);
find=false;
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
                if(continentInput.equals(""))
                {
                     Toast toast=Toast.makeText(getApplicationContext(),"Please type continent's name",Toast.LENGTH_SHORT);
                     toast.show();
                     notInputContinent=true;
                }
                if(animalInput.equals(""))
                {
                    Toast toast=Toast.makeText(getApplicationContext(),"Please type animal's name",Toast.LENGTH_SHORT);
                    toast.show();
                    notInputAnimal=true;
                }
                else if(!notInputContinent&&!notInputAnimal){
                    final boolean[] findAnimal = {false};

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Animal animal = AnimalDatabase.getInstance(getApplicationContext()).animalDao().findAnimal(animalInput);

                            if (animal != null) {
                                findAnimal[0] = true;
                                Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AnimalDatabase.getInstance(getApplicationContext()).animalDao().update(continentInput, animal.animalName);
                                        final List<Animal> animalList = AnimalDatabase.getInstance(getApplicationContext()).animalDao().getAllAnimals();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.setAnimals(animalList);
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });

                                thread1.start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Animal_gasit", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            } else {
                                Animal newAnimal = new Animal(animalInput, continentInput);

                                    InsertAsyncTask insertAsyncTask = new InsertAsyncTask();
                                    insertAsyncTask.execute(newAnimal);

                            }
                        }
                    });


                    thread.start();
                }
                else {
                    notInputAnimal=false;
                    notInputContinent=false;
                }

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
        {if(!find)
        {AnimalDatabase.getInstance(getApplicationContext()).animalDao().insertAnimal(animals[0]);
        }
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