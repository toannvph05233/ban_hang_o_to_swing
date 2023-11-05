import java.util.Date;

public class Invoice {
    private int id;
    private Car car;
    private int quantity;
    private Date createDate;
    private double totalPrice;
    private String nameCustomer;
    private String phoneCustomer;


    public Invoice(int id, Car car, int quantity, Date createDate, double totalPrice, String nameCustomer, String phoneCustomer) {
        this.id = id;
        this.car = car;
        this.quantity = quantity;
        this.createDate = createDate;
        this.totalPrice = totalPrice;
        this.nameCustomer = nameCustomer;
        this.phoneCustomer = phoneCustomer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public int getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
