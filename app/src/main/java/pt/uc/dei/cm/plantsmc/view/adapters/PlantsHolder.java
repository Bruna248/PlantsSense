package pt.uc.dei.cm.plantsmc.view.adapters;

import pt.uc.dei.cm.plantsmc.model.Greenhouse;
import pt.uc.dei.cm.plantsmc.model.Plant;

public interface PlantsHolder {
    void onAddPlant();
    void onPlantClick(Plant plant);

    void savePlant(Plant plant);
}
