package com.mapzen.android.sample;

import com.mapzen.android.MapFragment;
import com.mapzen.android.MapzenMap;
import com.mapzen.android.OnMapReadyCallback;
import com.mapzen.android.model.MapStyle;

import com.mapzen.tangram.TouchInput.DoubleTapResponder;
import com.mapzen.tangram.LngLat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Demonstrates switching the map's style.
 */
public class SwitchStyleActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, DoubleTapResponder {

    private MapFragment mapFragment;
    private MapzenMap mapzenMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_style);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.style_array, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(MapStyle.BUBBLE_WRAP, new OnMapReadyCallback() {
            @Override public void onMapReady(MapzenMap mapzenMap) {
                SwitchStyleActivity.this.mapzenMap = mapzenMap;
                mapzenMap.getMapController().setDoubleTapResponder(SwitchStyleActivity.this);
            }
        });
    }

    private void changeMapStyle(MapStyle style) {
        if (mapzenMap != null) {
            mapzenMap.setStyle(style);
        }
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                changeMapStyle(MapStyle.BUBBLE_WRAP);
                break;
            case 1:
                changeMapStyle(MapStyle.CINNABAR);
                break;
            case 2:
                changeMapStyle(MapStyle.REFILL);
                break;
            case 3:
                changeMapStyle(MapStyle.OUTDOOR);
                break;
            case 4:
                changeMapStyle(MapStyle.GOTHAM);
                break;
            case 5:
                changeMapStyle(MapStyle.BLUEPRINT);
                break;
            default:
                changeMapStyle(MapStyle.BUBBLE_WRAP);
                break;
        }
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    @Override
    public boolean onDoubleTap(float x, float y) {
        mapzenMap.getMapController().setZoomEased(mapzenMap.getZoom() + 1.f, 500);
        LngLat tapped = mapzenMap.getMapController().coordinatesAtScreenPosition(x, y);
        LngLat current = mapzenMap.getPosition();
        LngLat next = new LngLat(
                .5 * (tapped.longitude + current.longitude),
                .5 * (tapped.latitude + current.latitude));
        mapzenMap.getMapController().setPositionEased(next, 500);
        return true;
    }
}
