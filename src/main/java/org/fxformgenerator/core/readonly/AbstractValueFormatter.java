package org.fxformgenerator.core.readonly;

import java.util.Date;

/**
 * Created by TEA on 03/05/2016.
 */
public interface AbstractValueFormatter {

    String format(boolean value);

    String format(Date value);

}
