package dps.webapplication.adapter;

import javax.xml.bind.UnmarshalException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String,Date> {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date unmarshal(String v) throws UnmarshalException {
        try {
            return dateFormat.parse(v);
        } catch (ParseException e) {
            throw new UnmarshalException(e);
        }
    }

    @Override
    public String marshal(Date v) {
        return dateFormat.format(v);
    }


}
