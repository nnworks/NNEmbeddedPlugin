package nl.nnworks.nnembedded.plugin.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTime {
  private static DateTimeFormatter UTC = DateTimeFormatter.RFC_1123_DATE_TIME;

  public static String getCurrentUTCTime() {
    LocalDateTime ldt = LocalDateTime.now();
    ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID());
    ZonedDateTime zdt = ldt.atZone(zoneId);
    ZonedDateTime utcZoneDateTime = zdt.withZoneSameInstant(ZoneOffset.UTC);
    return utcZoneDateTime.format(UTC);
  }
}
