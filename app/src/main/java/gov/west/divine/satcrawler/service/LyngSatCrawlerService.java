package gov.west.divine.satcrawler.service;

import gov.west.divine.satcrawler.dao.SatellitesDatabaseHelper;
import gov.west.divine.satcrawler.dao.repository.SatReportRepository;
import gov.west.divine.satcrawler.model.SatReport;
import gov.west.divine.satcrawler.model.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class LyngSatCrawlerService {
    private final SatReportRepository satReportRepository;

    public LyngSatCrawlerService(SatellitesDatabaseHelper dbHelper) {
        this.satReportRepository = new SatReportRepository(dbHelper);
    }

    public boolean hasNewReports(){
        return dailyReport() || launcheReport();
    }

    private boolean launcheReport(){
        AtomicBoolean hasReports = new AtomicBoolean(false);
        try {
            Document doc = Jsoup.connect("https://www.lyngsat.com/launches/index.html").get();
            Element launchedSatellites = doc.getElementsByAttributeValueStarting("width", "720").get(16);

            launchedSatellites.children().get(0).children().stream().skip(1).forEach(e -> {
                String strDate = e.children().get(0).text();
                Date date = parseDate(strDate);
                Date currDate = new Date();
                if (currDate.getYear() == date.getYear() && currDate.getMonth() == date.getMonth() && date.getDate() - currDate.getDate() == 1){
                    String message = "New Satellite "+e.children().get(2).text();
                    SatReport satReport = new SatReport(message, Source.LYNGSAT);

                    boolean reportExist = satReportRepository.isReportExistByMessage(satReport.getMessage());

                    if (!reportExist){
                        hasReports.set(true);
                        satReportRepository.insert(satReport);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hasReports.get();
    }

    private Date parseDate(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        if (strDate.length() == 4){
            dateFormat = new SimpleDateFormat("yyMM");
        }
        try {
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new InvalidParameterException("invalid date");
    }

    private boolean dailyReport(){
        boolean hasNewReports = false;
        try {
            Document doc = Jsoup.connect("https://www.lyngsat.com/headlines.html").get();
            Element lastWeekEl = doc.getElementsByAttributeValueStarting("width", "600").get(0).children().get(0);

            Elements values = lastWeekEl.children();

            for (Element v : values) {
                String messages = v.child(1).text();

                String[] messageArr = messages.split("\u0095");

                for (String message : messageArr) {
                    if (message.length() > 0){
                        SatReport satReport = new SatReport(message, Source.LYNGSAT);
                        boolean reportExist = satReportRepository.isReportExistByMessage(satReport.getMessage());

                        if (!reportExist){
                            hasNewReports = true;
                            satReportRepository.insert(satReport);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hasNewReports;
    }


}
