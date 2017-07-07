package com.zoftino.room;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LifecycleActivity{

    private PersonViewModel personViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personViewModel = ViewModelProviders.of(this).get(PersonViewModel.class);

        //observe LiveData
        //this LiveData instance is not recreated after every db call, so registering observer in onCreate
        //corresponding DAO method returns List of Person objects, not LiveData
        observerPersonListResults(personViewModel.getPersonsByCityLive());

        //corresponding DAO method call returns LiveData
        //Transformations makes it possible to add observer only once to LiveData returned by room DAO
        observePersonByMobile(personViewModel.getPersonsByMobile());
    }
    private void observerPersonListResults(LiveData<List<Person>> personsLive) {
        //observer LiveData
        personsLive.observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(@Nullable List<Person> person) {
                if(person == null){
                    return;
                }
                Toast.makeText(MainActivity.this, "Number of person objects in the response: "+person.size(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void observePersonByMobile(LiveData<Person> personByMob){
        personByMob.observe(this, new Observer<Person>() {
            @Override
            public void onChanged(@Nullable Person person) {
                if(person == null){
                    return;
                }
                ((TextView)findViewById(R.id.name)).setText(person.getName());
                ((TextView)findViewById(R.id.email)).setText(person.getEmail());
                ((TextView)findViewById(R.id.mobile)).setText(person.getMobile());
                ((TextView)findViewById(R.id.lineOne)).setText(person.getAddress().getLineOne());
                ((TextView)findViewById(R.id.city)).setText(person.getAddress().getCity());
                ((TextView)findViewById(R.id.country)).setText(person.getAddress().getCountry());
                ((TextView)findViewById(R.id.zip)).setText(person.getAddress().getZip());
            }
        });
    }
    public void getAllPerson(View view){
        LiveData<List<Person>> allPersons = personViewModel.getAllPersons();
        observerPersonListResults(allPersons);
    }
    public void getPersonByMobile(View view){
        if((TextView)findViewById(R.id.mobile) == null){
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG);
            return;
        }
        personViewModel.setMobile(((TextView)findViewById(R.id.mobile)).getText().toString());
    }

    public void getPersonByCities(View view){
        if((TextView)findViewById(R.id.mobile) == null){
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG);
            return;
        }
        List<String> cityLst = new ArrayList<>();
        cityLst.add(((TextView)findViewById(R.id.city)).getText().toString());
        personViewModel.getPersonsByCity(cityLst);
    }
    public void addPerson(View view){
        if((TextView)findViewById(R.id.mobile) == null){
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG);
            return;
        }
        personViewModel.addPerson(createPerson());
    }
    public void updatePerson(View view){
        if((TextView)findViewById(R.id.mobile) == null){
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG);
            return;
        }
        personViewModel.updatePerson(createPerson());
    }
    public void deletePerson(View view){
        if((TextView)findViewById(R.id.mobile) == null){
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG);
            return;
        }
        personViewModel.deletePerson(createPerson());
    }
    private Person createPerson(){
        Person p = new Person();
        p.setName(((TextView)findViewById(R.id.name)).getText().toString());
        p.setEmail(((TextView)findViewById(R.id.email)).getText().toString());
        p.setMobile(((TextView)findViewById(R.id.mobile)).getText().toString());

        Address a = new Address();
        a.setLineOne(((TextView)findViewById(R.id.lineOne)).getText().toString());
        a.setCity(((TextView)findViewById(R.id.city)).getText().toString());
        a.setCountry(((TextView)findViewById(R.id.country)).getText().toString());
        a.setZip(((TextView)findViewById(R.id.zip)).getText().toString());

        p.setAddress(a);

        return p;
    }
}
