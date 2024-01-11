package pt.uc.dei.cm.plantsmc.view.adapters;

import pt.uc.dei.cm.plantsmc.model.Greenhouse;

public interface GreenhouseHolder {
    void onAddGreenhouse();
    void onGreenhouseClick(Greenhouse greenhouse);

    void saveGreenhouse(Greenhouse greenhouse);
}
