package com.iapp.angara.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.iapp.angara.R;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

/**
 * The class is designed to create a object label on the map
 * @author IgorApplications <igorapplications@gmail.com>
 * @version 1.0
 */
public class PlaceMark {

    /** Fixed image width in dp */
    private static final float WIDTH = 22.5f;
    /** Fixed image height in dp */
    private static final float HEIGHT = 61.5f;

    /** Activity the map on which the tags are located */
    private final Activity activity;
    /** Marker coordinates on the map */
    private final Point point;

    /** Marker name */
    private String text;
    /** Marker click listener */
    private OnClickListener clickListener;
    /** All markers listener
     * needed to save object from garbage collector */
    private MapObjectTapListener objectTapListener;

    /**
     * The constructor is designed to create
     * standard mark on the map
     * @param activity map context
     * @param point marker coordinate
     */
    public PlaceMark(Activity activity, Point point) {
        this.activity = activity;
        this.point = point;
    }

    /**
     * The constructor is designed to create marker with name
     * @see PlaceMark#PlaceMark(Activity, Point)
     * @param text marker name
     */
    public PlaceMark(Activity activity, Point point, String text) {
        this(activity, point);
        this.text = text;
    }

    /**
     * The method is designed to set the name
     * of the marker, which will be displayed for Toast
     * @see Toast
     * @param text marker name
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The method is designed for getting the name
     * of the marker, which will be displayed for Toast
     * @see Toast
     * @return current marker name
     */
    public String getText() {
        return text;
    }

   /**
    * The method to set a listener that fires
    *  a callback when the marker is clicked
    * @param clickListener mark click listener
    * */
    public void addOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * The method is designed to send all the data of this
     * instance and the map settings
     * @param mapView map data
     */

    private PlacemarkMapObject current;

    public void add(MapView mapView) {
        current = mapView.getMap().getMapObjects().addPlacemark(point, ImageProvider.fromBitmap(getUpImage()));

        objectTapListener = (obj, tapPoint) -> {
            if (current.equals(obj)) {
                if (text != null) Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
                if (clickListener != null) clickListener.onClick();
            }

            return false;
        };

        mapView.getMap().getMapObjects().addTapListener(objectTapListener);
    }

   private Bitmap getUpImage() {
        Bitmap decodedMarker = BitmapFactory.decodeResource(activity.getResources(), R.drawable.place_mark);
        return Bitmap.createScaledBitmap(decodedMarker, Math.round(parseDpToPx(WIDTH)),
                Math.round(parseDpToPx(HEIGHT)), true);
    }

    private float parseDpToPx(float dp) {
        return dp * ((float) activity.getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT);
    }
}





