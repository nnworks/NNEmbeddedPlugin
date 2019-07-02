package nl.nnworks.nnembedded.plugin.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTime {
  private static DateTimeFormatter UTC = DateTimeFormatter.RFC_1123_DATE_TIME;
  

  public static String getCurrentUTCTime() {
    ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
    LocalDateTime ldt = LocalDateTime.now();
    OffsetDateTime odt = ldt.atOffset(ZoneOffset.UTC);
    ZoneOffset zoneOffSet = zoneId.getRules().getOffset(ldt);
    return odt.format(UTC);
  }
}
