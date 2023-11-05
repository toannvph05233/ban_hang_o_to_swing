import java.util.Date;

public class Car {
    private int id;
    private String nation;
    private String name;
    private String company;
    private double price;
    private Date createDate;

    public Car(int id, String nation, String name, String company, double price, Date createDate) {
        this.id = id;
        this.nation = nation;
        this.name = name;
        this.company = company;
        this.price = price;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

