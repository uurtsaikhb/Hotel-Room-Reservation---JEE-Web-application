package Controller;

import Entity.Hotel;
import Controller.util.JsfUtil;
import Controller.util.PaginationHelper;
import Entity.Address;
import Entity.FeatureHotel;
import Entity.Location;
import Model.HotelFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import Entity.Picture;
import Model.FeatureHotelFacade;
import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

@Named("hotelController")
@SessionScoped
public class HotelController implements Serializable {

    private Hotel current;
    private Address address;
    private DataModel items = null;
    private List<Hotel> hotels;
    @EJB
    private Model.HotelFacade ejbFacade;    
    @EJB
    private Model.PictureFacade ejbPictureFacade;
    @EJB
    private Model.LocationFacade ejbLocationFacade;
    @Inject
    private RoomBookingController roomBookingController;
    @EJB 
    private Model.FeatureHotelFacade ejbFeature;
    
    private FeatureHotel [] features;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    //Amar added for pictures
    private List<File> files = new ArrayList<>();
    private final String path = "resources" + File.separator + "uploadhotel";
    private final ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    private String destination = servletContext.getRealPath(File.separator + path);
    
    //Amar added for location
    private double lat;
    private double lng;

    public HotelController() {
    }
//    @PostConstruct
//    public void intItems(){
//       if (items == null) {
//            items = getPagination().createPageDataModel();
//        }
//    }

    public List<Hotel> getHotels() {
        return hotels;
    }
  

    public RoomBookingController getRoomBookingController() {
        return roomBookingController;
    }

    public void setRoomBookingController(RoomBookingController roomBookingController) {
        this.roomBookingController = roomBookingController;
    }

    public FeatureHotelFacade getEjbFeature() {
        return ejbFeature;
    }
    

    public String findHotel(Hotel hotel){
        current=hotel;
        return "/hotel/review";
    }
    
    public String getListOfRooms(String id){
        
        return null;
    }
     public FeatureHotel[] getFeatures() {
        return features;
    }

    public void setFeatures(FeatureHotel[] features) {
        this.features = features;
    }
  
    public Hotel getSelected() {
        if (current == null) {
            current = new Hotel();
            selectedItemIndex = -1;
        }
        return current;
    }

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }
    private HotelFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Hotel) getItems().getRowData();
        address=current.getAddress();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/hotel/View";
    }

    public String prepareCreate() {
        current = new Hotel();
        address=new Address();
        selectedItemIndex = -1;
        return "/hotel/Create";
    }
    
     public void addFeature(){
        if(features.length>0){
        for(FeatureHotel f: features){
            current.getHotelFeatures().add(f);
            f.getHotels().add(current);
        }
        }
    }
    private void editFearure() {
        for(FeatureHotel f: current.getHotelFeatures()){
            getEjbFeature().edit(f);
        }
    }
    
@Interceptors(Logging.class)
         public String create() {
        current.setAddress(address);
       
        try {
            getFacade().create(current);
            addFeature();
            getFacade().edit(current);
            editFearure();
                    
            for (File file : files) {
                Picture picture = new Picture(path + File.separator + file.getName(), current);
                ejbPictureFacade.create(picture);
            }
            files.clear();
            
            Location location = new Location(lat,lng,current);
            ejbLocationFacade.create(location);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HotelCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Hotel) getItems().getRowData();
        address=current.getAddress();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HotelUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

//    public String delete(Hotel hotel){
//        current = (Hotel) getItems().getRowData();
//        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
//        performDestroy();
//        recreatePagination();
//        recreateModel();
//        return "List";
//    }
    public String destroy() {
        current = (Hotel) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("HotelDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }
   

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Hotel getHotel(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Hotel.class)
    public static class HotelControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HotelController controller = (HotelController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "hotelController");
            return controller.getHotel(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Hotel) {
                Hotel o = (Hotel) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Hotel.class.getName());
            }
        }

    }
    
    public void upload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {
            File file = new File(destination + File.separator + fileName);
            OutputStream out = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            files.add(file);
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public void onRowEdit(RowEditEvent event) {
        current=(Hotel) event.getObject();
        FacesMessage msg = new FacesMessage("Hotel Edited", ((Hotel) event.getObject()).getId()+"");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        update();
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Hotel) event.getObject()).getId()+"");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            update();
        }
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
