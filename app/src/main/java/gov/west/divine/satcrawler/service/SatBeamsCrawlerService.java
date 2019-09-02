package gov.west.divine.satcrawler.service;

import gov.west.divine.satcrawler.dao.SatellitesDatabaseHelper;
import gov.west.divine.satcrawler.dao.repository.SatReportRepository;
import gov.west.divine.satcrawler.dao.repository.SatBeamsRepository;
import gov.west.divine.satcrawler.model.SatBeamsEntity;
import gov.west.divine.satcrawler.model.SatReport;
import gov.west.divine.satcrawler.model.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SatBeamsCrawlerService {
    private final SatBeamsRepository satBeamsRepository;
    private final SatReportRepository satBeamsReportRepository;

    public SatBeamsCrawlerService(SatellitesDatabaseHelper dbHelper) {
        this.satBeamsRepository = new SatBeamsRepository(dbHelper);
        this.satBeamsReportRepository = new SatReportRepository(dbHelper);
    }

    public boolean hasNewReports(){
        List<SatReport> reports = new LinkedList<>();

        List<SatBeamsEntity> remoteSats = getRemoteSatBeamsData();
        List<SatBeamsEntity> innerSats = getInnerSatBeamsData();

        if (remoteSats.isEmpty()){
            return false;
        }

        if (innerSats.isEmpty()){
            satBeamsRepository.insert(remoteSats);
            return false;
        }

        for (SatBeamsEntity remoteSat : remoteSats) {

            if (innerSats.contains(remoteSat)){
                int index = innerSats.indexOf(remoteSat);
                SatBeamsEntity entity = innerSats.get(index);
                StringBuilder reportMessage = new StringBuilder();

                if (entity.getPosition() != null){
                    if (!entity.getPosition().equalsIgnoreCase(remoteSat.getPosition())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new position: "+remoteSat.getPosition());
                    }
                } else if (remoteSat.getPosition() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new position: "+remoteSat.getPosition());
                }

                if (entity.getStatus() != null){
                    if (!entity.getStatus().equalsIgnoreCase(remoteSat.getStatus())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new status: "+remoteSat.getStatus());
                    }
                } else if (remoteSat.getStatus() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new status: "+remoteSat.getStatus());
                }

                if (entity.getName() != null){
                    if (!entity.getName().equalsIgnoreCase(remoteSat.getName())){
                        reportMessage.append("\n"+remoteSat.getName()+" change name: "+remoteSat.getName());
                    }
                } else if (remoteSat.getName() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" change name: "+remoteSat.getName());
                }

                if (entity.getModel() != null){
                    if (!entity.getModel().equalsIgnoreCase(remoteSat.getModel())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new model: "+remoteSat.getModel());
                    }
                } else if (remoteSat.getModel() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new model: "+remoteSat.getModel());
                }

                if (entity.getManufacturer() != null){
                    if (!entity.getManufacturer().equalsIgnoreCase(remoteSat.getManufacturer())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new manufacturer: "+remoteSat.getManufacturer());
                    }
                } else if (remoteSat.getManufacturer() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new manufacturer: "+remoteSat.getManufacturer());
                }

                if (entity.getOperator() != null){
                    if (!entity.getOperator().equalsIgnoreCase(remoteSat.getOperator())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new operator: "+remoteSat.getOperator());
                    }
                } else if (remoteSat.getOperator() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new operator: "+remoteSat.getOperator());
                }

                if (entity.getSite() != null){
                    if (!entity.getSite().equalsIgnoreCase(remoteSat.getSite())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new site: "+remoteSat.getSite());
                    }
                } else if (remoteSat.getSite() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new site: "+remoteSat.getSite());
                }

                if (entity.getMass() != null){
                    if (!entity.getMass().equalsIgnoreCase(remoteSat.getMass())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new mass: "+remoteSat.getMass());
                    }
                } else if (remoteSat.getMass() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new mass: "+remoteSat.getMass());
                }

                if (entity.getLaunchDate() != null){
                    if (!entity.getLaunchDate().equalsIgnoreCase(remoteSat.getLaunchDate())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new launch date: "+remoteSat.getLaunchDate());
                    }
                } else if (remoteSat.getLaunchDate() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new launch date: "+remoteSat.getLaunchDate());
                }

                if (entity.getExpectedLifeTime() != null){
                    if (!entity.getExpectedLifeTime().equalsIgnoreCase(remoteSat.getExpectedLifeTime())){
                        reportMessage.append("\n"+remoteSat.getName()+" change expected life time: "+remoteSat.getExpectedLifeTime());
                    }
                } else if (remoteSat.getExpectedLifeTime() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" change expected life time: "+remoteSat.getExpectedLifeTime());
                }

                if (entity.getComments() != null) {
                    if (!entity.getComments().equalsIgnoreCase(remoteSat.getComments())){
                        reportMessage.append("\n"+remoteSat.getName()+" has new comments: "+remoteSat.getComments());
                    }
                } else if (remoteSat.getComments() != null){
                    reportMessage.append("\n"+remoteSat.getName()+" has new comments: "+remoteSat.getComments());
                }

                if (reportMessage.length() > 0){
                    reports.add(new SatReport(reportMessage.toString(), Source.SATBEAMS));
                }

            } else {
                reports.add(new SatReport("New satellite: "+remoteSat.getName(), Source.SATBEAMS));
            }
        }

        if (!reports.isEmpty()){
            this.satBeamsReportRepository.insert(reports);
            this.satBeamsRepository.refillTable(remoteSats);
            return true;
        }

        return false;
    }

    private List<SatBeamsEntity> getInnerSatBeamsData() {
        return satBeamsRepository.selectAll();
    }

    private List<SatBeamsEntity> getRemoteSatBeamsData(){
        List<SatBeamsEntity> sats = new LinkedList<>();

        try {
            Document doc = Jsoup.connect("https://www.satbeams.com/satellites/").get();
            Element satTable = doc.body().getElementById("sat_grid");

            ArrayList list = satTable.children().get(0).children();

            list.remove(0);

            for (Element element : (Elements) list) {
                sats.add(new SatBeamsEntity(element));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sats;
    }

}
