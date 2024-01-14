package pt.uc.dei.cm.plantsmc.view.sensors;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.core.cartesian.series.Line;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.graphics.vector.Stroke;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

import pt.uc.dei.cm.plantsmc.R;
import pt.uc.dei.cm.plantsmc.model.SensorDataObject;
import pt.uc.dei.cm.plantsmc.model.SensorHolderType;
import pt.uc.dei.cm.plantsmc.model.SensorType;
import pt.uc.dei.cm.plantsmc.view.adapters.SensorVMHolder;
import pt.uc.dei.cm.plantsmc.viewmodel.GreenhouseViewModel;
import pt.uc.dei.cm.plantsmc.viewmodel.PlantViewModel;


public class SensorDetailFragment extends Fragment {

    private static final String ARG_PARENT_ID = "arg_parent_id";
    private static final String ARG_SENSOR_TYPE = "arg_sensor_type";
    private static final String ARG_PARENT_TYPE = "arg_parent_type";
    private SensorHolderType parentType;
    private SensorType sensorType;
    private String parentId;
    private SensorVMHolder sensorViewModel;

    public SensorDetailFragment() {
        // Required empty public constructor
    }

    public static SensorDetailFragment newInstance(String parentId, SensorHolderType parentType, SensorType sensorType) {
        SensorDetailFragment fragment = new SensorDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARENT_ID, parentId);
        args.putString(ARG_SENSOR_TYPE, sensorType.toString());
        args.putString(ARG_PARENT_TYPE, parentType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize your SensorViewModel
        if (getArguments() != null) {
            parentId = getArguments().getString(ARG_PARENT_ID);
            sensorType = SensorType.valueOf(getArguments().getString(ARG_SENSOR_TYPE));
            parentType = SensorHolderType.valueOf(getArguments().getString(ARG_PARENT_TYPE));
        }
        
        if (parentType == SensorHolderType.GREENHOUSE) {
            sensorViewModel = new ViewModelProvider(requireActivity()).get(GreenhouseViewModel.class);
        } else if (parentType == SensorHolderType.PLANT) {
            sensorViewModel = new ViewModelProvider(requireActivity()).get(PlantViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sensor_details, container, false);

        setup_sensor_details(view);

        setup_chart(view);


        return view;
    }

    private void setup_sensor_details(View view) {
        TextView textViewSensorTypeDetails = view.findViewById(R.id.textViewSensorTypeDetails);
        TextView textViewSensorValueDetails = view.findViewById(R.id.textViewSensorValueDetails);
        TextView textViewSensorDateDetails = view.findViewById(R.id.textViewSensorDateDetails);

        textViewSensorTypeDetails.setText(sensorType.toString());

        sensorViewModel.getSensorsObjectByType(parentId, sensorType).observe(getViewLifecycleOwner(), sensorDataObjects -> {
            int lastSensorDataIndex = sensorDataObjects.size() - 1;
            SensorDataObject lastSensor = sensorDataObjects.get(lastSensorDataIndex);

            textViewSensorValueDetails.setText(String.format("%s", lastSensor.getMeasurement()));
            textViewSensorDateDetails.setText(lastSensor.getTimestamp().replace(" ", "\n"));
        });

        textViewSensorTypeDetails.setText(sensorType.toString());

        ImageView imageViewSensor = (ImageView) view.findViewById(R.id.imageViewSensor);
        if (sensorType == SensorType.TEMPERATURE) {
            imageViewSensor.setImageResource(R.drawable.thermometer_temperature_svgrepo_com);
        } else if (sensorType == SensorType.HUMIDITY) {
            imageViewSensor.setImageResource(R.drawable.humidity);
        }
    }

    private void setup_chart(View view) {
        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progressBar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.background().enabled(true);
        cartesian.background().fill("#ECECEC");

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.xAxis(0).title("Time");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new ValueDataEntry("", 0));

        Line series1 = cartesian.line(seriesData);
        series1.name();
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(false);

        anyChartView.setChart(cartesian);

        sensorViewModel.getSensorsObjectByType(parentId, sensorType).observe(getViewLifecycleOwner(), sensorDataObjects -> {
            seriesData.clear();
            for (SensorDataObject sensorDataObject : sensorDataObjects) {
                seriesData.add(new ValueDataEntry(sensorDataObject.getTimestamp(), sensorDataObject.getMeasurement()));
            }

            series1.data(seriesData);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}