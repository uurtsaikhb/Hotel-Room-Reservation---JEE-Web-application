package Controller;

import Entity.RoomBooking;
import Controller.util.JsfUtil;
import Controller.util.PaginationHelper;
import Entity.Account;
import Entity.Hotel;
import Entity.Room;
import Model.AccountFacade;
import Model.HotelFacade;
import Model.RoomBookingFacade;
import Model.RoomFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

@Named("roomBookingController")
@SessionScoped
public class RoomBookingController implements Serializable {

    private RoomBooking current;
    private Date dateFrom;
    private Date dateTo;
    private List<Hotel> hotels;
    private Account account;
    private List<Room> rooms;
    private DataModel items = null;
    private String place;
    @EJB 
    private Model.HotelFacade hotelEjbFacade;
    @EJB
    private Model.RoomBookingFacade ejbFacade;
    @EJB
    private BookingController bookingController;
    @EJB
    private RoomFacade roomFacade;
   
    @Inject
    private LoginController loginController;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private String numberofroom;

    public RoomBookingController() {
    }
    
    @PostConstruct
    public void initAccount(){
        account = (Account)loginController.getAccount();
    }
    

    public Account getAccount() {
        if(account==null){
            account = new Account();
        }
        return account;
    }
    

    public void setAccount(Account account) {
        this.account = account;
    }

    
    
    public RoomFacade getRoomFacade() {
        return roomFacade;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        getSelected().setDateFrom(dateFrom);
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        getSelected().setDateTo(dateTo);
    }
    
      public List<Hotel> getHotels() {
        return hotels;
    }

    public List<Room> getRooms() {
        return rooms;
    }
  
    public HotelFacade getHotelEjbFacade() {
        return hotelEjbFacade;
    }

    public void setHotelEjbFacade(HotelFacade hotelEjbFacade) {
        this.hotelEjbFacade = hotelEjbFacade;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    
    public BookingController getBookingController() {
        return bookingController;
    }

    public void setBookingController(BookingController bookingController) {
        this.bookingController = bookingController;
    }
    
    public RoomBooking getSelected() {
        if (current == null) {
            current = new RoomBooking();
            selectedItemIndex = -1;
        }
        return current;
    }

    private RoomBookingFacade getFacade() {
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
        current = (RoomBooking) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new RoomBooking();
        selectedItemIndex = -1;
        return "Create";
    }
    public String findHotels(){
        if(getBookingController().validateDate(dateFrom, dateTo)){
        hotels=getHotelEjbFacade().findHotelByLocation(place);
        return "/roomBooking/hotelView?faces-redirect=true";
        }
        else
        {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("DateError"));
           return null; 
        }
    }
     public String findHotelById(Long id){
        rooms = getFacade().findAvailabileRooms(id, getDateFrom(), getDateTo());
      // rooms=getHotelEjbFacade().find(id).getRooms();
       
     return  "/roomBooking/roomView?faces-redirect=true";
  }
    public String addingRoom(Room room) {
        current.setRoom(room);
        account=loginController.getAccount();
        if(account!=null){
            return "/roomBooking/payment";
        }
        else{
            return "/roomBooking/Form";
        }
        
    }
    public String createAccount(){
       getBookingController().addAccount(account);
        return "/roomBooking/payment";
    }

    public String create() {
       
        if(loginController.getAccount()!=null)
             account = loginController.getAccount();
       
        current.setAccount(account);
        
        try {
            
            getBookingController().createBooking(current);
            
            return "/roomBooking/Confirmation";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (RoomBooking) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RoomBookingUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (RoomBooking) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RoomBookingDeleted"));
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

    public RoomBooking getRoomBooking(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public String getNumberofroom() {
        return numberofroom;
    }

    public void setNumberofroom(String numberofroom) {
        this.numberofroom = numberofroom;
    }

    @FacesConverter(forClass = RoomBooking.class)
    public static class RoomBookingControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RoomBookingController controller = (RoomBookingController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "roomBookingController");
            return controller.getRoomBooking(getKey(value));
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
            if (object instanceof RoomBooking) {
                RoomBooking o = (RoomBooking) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + RoomBooking.class.getName());
            }
        }

    }

}
