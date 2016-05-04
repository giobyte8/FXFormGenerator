package org.fxformgenerator.core.readonly;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TEA on 03/05/2016.
 */
public class DefaultValueFormatter implements AbstractValueFormatter {
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String format(boolean value) {
        return value
                ? "Si"
                : "No";
    }

    @Override
    public String format(Date value) {
        return dateFormatter.format(value);
    }
}
