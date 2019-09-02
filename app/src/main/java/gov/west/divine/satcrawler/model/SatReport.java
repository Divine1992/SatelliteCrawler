package gov.west.divine.satcrawler.model;

import java.util.Date;

public class SatReport {
    private String message;
    private Source source;
    private Date created;

    public SatReport() {
    }

    public SatReport(String message, Source source) {
        this.message = message;
        this.source = source;
        this.created = new Date();
    }

    public SatReport(String message, Source source, Date created) {
        this.message = message;
        this.source = source;
        this.created = created;
    }

    public static SatReport emptyReport(){
        SatReport satReport = new SatReport();
        satReport.setCreated(new Date());
        satReport.setMessage("You have not reports yet");
        return satReport;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
