package Controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Amarbold
 */
@ManagedBean
@RequestScoped
public class MapBean {

    private String centerCoords = "6.920833, 103.578611";
    private final MapModel mapModel;

    public MapBean() {
        mapModel = new DefaultMapModel();       
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public String getCenterCoords() {
        return centerCoords;
    }

    public String setCenterCoords(String centerCoords, String name) {
        this.centerCoords = centerCoords;
        String[] coords = centerCoords.split(", ");
        LatLng coord = new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
        mapModel.addOverlay(new Marker(coord, name, null, "http://cdn0.agoda.net/images/default/popup_map/icon_blue_H.gif"));
        
        return centerCoords;   
    }
}
