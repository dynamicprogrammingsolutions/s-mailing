package dps.webapplication.adapter;

import javax.json.bind.adapter.JsonbAdapter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonbDateAdapter implements JsonbAdapter<Date,String> {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date adaptFromJson(String obj) {
        try {
            return dateFormat.parse(obj);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String adaptToJson(Date obj) {
        return dateFormat.format(obj);
    }

}
