package gov.west.divine.satcrawler.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;

public class SatBeamsEntity {
    private int id;
    private String position;
    private String status;
    private String name;
    private String model;
    private String manufacturer;
    private String operator;
    private String site;
    private String mass;
    private String launchDate;
    private String expectedLifeTime;
    private String comments;

    public SatBeamsEntity() {
    }

    public SatBeamsEntity(int id, String position, String status, String name, String model, String manufacturer, String operator, String site, String mass, String launchDate, String expectedLifeTime, String comments) {
        this.id = id;
        this.position = position;
        this.status = status;
        this.name = name;
        this.model = model;
        this.manufacturer = manufacturer;
        this.operator = operator;
        this.site = site;
        this.mass = mass;
        this.launchDate = launchDate;
        this.expectedLifeTime = expectedLifeTime;
        this.comments = comments;
    }

    public SatBeamsEntity(Element el) {
        Elements els = el.children();
        this.id = Integer.parseInt(el.attr("id").substring(4));
        if (els.get(1).children().size() > 0){
            this.position = els.get(1).child(0).text();
        }
        this.status = els.get(2).text();
        if (els.get(3).children().size() > 0){
            this.name = els.get(3).child(0).text();
        }
        if (els.get(7).children().size() > 0){
            this.operator = els.get(7).child(0).text();
        }
        if (els.get(8).children().size() > 0 && els.get(8).child(0).children().size() > 0){
            this.site = els.get(8).child(0).child(0).attr("href");
        }
        this.mass = els.get(9).text();
        if (els.get(10).children().size() > 0){
            this.launchDate = els.get(10).child(0).text();
        }
        if (els.get(10).children().size() > 0){
            this.expectedLifeTime = els.get(10).child(0).attr("title");
        }
        this.comments = els.get(13).text();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SatBeamsEntity that = (SatBeamsEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getExpectedLifeTime() {
        return expectedLifeTime;
    }

    public void setExpectedLifeTime(String expectedLifeTime) {
        this.expectedLifeTime = expectedLifeTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "SatBeamsEntity{" +
                "id='" + id + '\'' +
                ", position='" + position + '\'' +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", operator='" + operator + '\'' +
                ", site='" + site + '\'' +
                ", mass='" + mass + '\'' +
                ", launchDate='" + launchDate + '\'' +
                ", expectedLifeTime='" + expectedLifeTime + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
