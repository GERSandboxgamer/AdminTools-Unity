package de.sbg.unity.admintools.Events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EventElements {
    
    private final List<String> WeatherList, TimeList, AnimalList;
    
    public EventElements() {
        this.TimeList = new ArrayList<>();
        TimeList.add("day");
        TimeList.add("night");
        TimeList.add("middnight");
        TimeList.add("evening");
        TimeList.add("noon");
        TimeList.add("afternoon");
        Collections.sort(TimeList);

        WeatherList = new ArrayList<>();
        WeatherList.add("clear");
        WeatherList.add("sun");
        WeatherList.add("overcase");
        WeatherList.add("rain");
        WeatherList.add("storm");
        WeatherList.add("snow");
        WeatherList.add("heavyrain");
        WeatherList.add("lightsnow");
        WeatherList.add("breeze");
        WeatherList.add("cold");
        WeatherList.add("heavysnow");
        WeatherList.add("fog");
        WeatherList.add("densefog");
        WeatherList.add("default");
        Collections.sort(WeatherList);
        
        AnimalList = new ArrayList<>();
        AnimalList.add("sheep");
        AnimalList.add("cow");
        AnimalList.add("pig");
        AnimalList.add("goat");
        AnimalList.add("rabbit");
        AnimalList.add("chicken");
        AnimalList.add("chicken");
    }
    
    public int toNumber(String nr) {
        int i;
        try {
            i = Integer.parseInt(nr);
        } catch (NumberFormatException ex) {
            return -1;
        }
        return i;
    }

    public float toFlaot(String nr) {
        float i;
        try {
            i = Float.parseFloat(nr);
        } catch (NumberFormatException ex) {
            return -1;
        }
        return i;
    }

    public List<String> getAnimalList() {
        return AnimalList;
    }

    public List<String> getTimeList() {
        return TimeList;
    }

    public List<String> getWeatherList() {
        return WeatherList;
    }
    
    
    
    
}
