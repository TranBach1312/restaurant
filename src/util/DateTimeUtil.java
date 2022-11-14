package util;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeUtil {
    public String getString(Date date) {
        return date.toString();
    }

    public Date getDate(String string) {
        Date datetime = null;
            try {
                datetime = new Date((new SimpleDateFormat("yyyy/MM/dd").parse(string)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return datetime;
    }

}
