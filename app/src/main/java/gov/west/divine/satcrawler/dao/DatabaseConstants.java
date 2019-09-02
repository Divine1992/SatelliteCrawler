package gov.west.divine.satcrawler.dao;

public enum DatabaseConstants {

    DATABASE_NAME("Satellites"), SATBEAMS_TABLE("SatBeams"), SAT_REPORT_TABLE("SatReport"), DATABASE_VERSION("6");

    DatabaseConstants(String value) {
        this.value = value;
    }

    private String value;

    public static String getValue(DatabaseConstants constants){
        DatabaseConstants[] values = DatabaseConstants.values();

        for (DatabaseConstants databaseConstants : values) {
            if (databaseConstants.equals(constants)){
                return databaseConstants.value;
            }
        }

        return null;
    }
}
