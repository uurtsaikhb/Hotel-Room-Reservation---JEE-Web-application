package Controller;

import Entity.Review;
import Controller.util.JsfUtil;
import Controller.util.PaginationHelper;
import Entity.Account;
import Entity.Hotel;
import Entity.RoomBooking;
import Model.ReviewFacade;
import Model.RoomBookingFacade;

import java.io.Serializable;
import java.util.Date;
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

@Named("reviewController")
@SessionScoped
public class ReviewController implements Serializable {

    private Review current;
    private DataModel items = null;
    private Long conf;
    private Hotel hotel;
    private int rank;
    private boolean disp;
    private Account account;
    private RoomBooking booking;
    @Inject
    private AccountController accountController;
    @Inject
    private RoomBookingController roomBookingController;
    @Inject
    private HotelController hotelController;
    @EJB
    private Model.ReviewFacade ejbFacade;
    @Inject
    private Model.RoomBookingFacade bookingEJBFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ReviewController() {
    }
    @PostConstruct
    public void init(){
        hotel=getHotelController().getSelected();
        account=getAccountController().getSelected();
        booking=getRoomBookingController().getSelected();
        
    }

    public AccountController getAccountController() {
        return accountController;
    }

    public RoomBookingController getRoomBookingController() {
        return roomBookingController;
    }

    public HotelController getHotelController() {
        return hotelController;
    }

    public Long getConf() {
        return conf;
    }

    public void setConf(Long conf) {
        this.conf = conf;
    }
    

    public Review getSelected() {
        if (current == null) {
            current = new Review();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ReviewFacade getFacade() {
        return ejbFacade;
    }

    public RoomBookingFacade getBookingEJBFacade() {
        return bookingEJBFacade;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        
    }

    public int getRank() {
        return rank;
    }
    public String putRank(int rank){
        this.rank=rank;
        getSelected().setRank(rank);
        return null;
    }

    public boolean isDisp() {
        return disp;
    }

    public void setDisp(boolean disp) {
        this.disp = disp;
    }

    public Account getAccount() {
        if(account==null){
            account=new Account();
        }
        return account;
    }

    public void setAccount(Account account) {
         this.account = account;
    }

    public RoomBooking getBooking() {
        if(booking==null){
            booking=new RoomBooking();
        }
        return booking;
    }

    public void setBooking(RoomBooking booking) {
      
        this.booking = booking;
    }
    
    
    public String findBooking(){
        setBooking(getBookingEJBFacade().findBooking(conf));
        if(booking!=null){
        hotel=booking.getRoom().getHotel();
        account=booking.getAccount();
        disp=true;
        }
        else{
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("confNumNotFound"));
        }
//       
       
        return "/review/reviewE";
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
        current = (Review) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Review();
        selectedItemIndex = -1;
        rank=0;
        disp=false;
        
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("reviewAdd"));
        return "/review/reviewE";
    }

    public String create() {
        current.setAccount(account);
        current.setHotel(hotel);
        current.setPostedDate(new Date());
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReviewCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Review) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReviewUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Review) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReviewDeleted"));
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

    public Review getReview(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Review.class)
    public static class ReviewControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReviewController controller = (ReviewController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reviewController");
            return controller.getReview(getKey(value));
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
            if (object instanceof Review) {
                Review o = (Review) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Review.class.getName());
            }
        }

    }

}
