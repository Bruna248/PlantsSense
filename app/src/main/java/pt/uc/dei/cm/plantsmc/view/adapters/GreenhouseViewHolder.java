package pt.uc.dei.cm.plantsmc.view.adapters;

import pt.uc.dei.cm.plantsmc.model.Greenhouse;

public interface GreenhouseViewHolder {
    void onAddGreenhouse();
    void onGreenhouseClick(Greenhouse greenhouse);
    void onEditGreenhouse(Greenhouse greenhouse);
    void saveGreenhouse(Greenhouse greenhouse);
}
