package agh.cs.EvolutionSimulator;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class World {
    public static void main (String [] args){
        JSONParser parser = new JSONParser();
        String dir = System.getProperty("user.dir");
        try {
            Object object = parser.parse(new FileReader(dir + "\\parameters.json"));
            JSONObject jsonObject = (JSONObject) object;
            int mapWidth = ((Long) jsonObject.get("width")).intValue();
            int mapHeight = ((Long) jsonObject.get("height")).intValue();
            int startEnergy = ((Long) jsonObject.get("startEnergy")).intValue();
            int moveEnergy = ((Long) jsonObject.get("moveEnergy")).intValue();
            int plantEnergy = ((Long) jsonObject.get("plantEnergy")).intValue();
            double jungleRatio = (Double) jsonObject.get("jungleRatio");
            int startNumberOfAnimals = ((Long) jsonObject.get("startNumberOfAnimals")).intValue();


            WorldMap map = new WorldMap (mapWidth,mapHeight, jungleRatio, plantEnergy,moveEnergy,startEnergy,startNumberOfAnimals);
            Simulation simulation = new Simulation(map);
            simulation.startSimulation();
        } catch (IOException | NullPointerException | IllegalArgumentException | org.json.simple.parser.ParseException ex){
            if(ex instanceof FileNotFoundException) System.out.println("File not found!");
            if(ex instanceof ParseException) System.out.println("ParseException");
            if(ex instanceof IllegalArgumentException) System.out.println(((IllegalArgumentException) ex).toString());
        };
    }
}
