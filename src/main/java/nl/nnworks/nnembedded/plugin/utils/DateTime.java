package nl.nnworks.nnembedded.plugin.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class DateTime {

  private static DateTimeFormatter RFC1123Formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
  private static ZoneId currentZoneId = ZoneId.of(TimeZone.getDefault().getID());
  
  
  public static String getCurrentUTCTimeInRFC1123() {
    LocalDateTime ldt = LocalDateTime.now();
    ZonedDateTime zdt = ldt.atZone(currentZoneId);
    ZonedDateTime utcZoneDateTime = zdt.withZoneSameInstant(ZoneOffset.UTC);
    return utcZoneDateTime.format(RFC1123Formatter);
  }
  
  public static ZonedDateTime getCurrentUTCTimeFromRFC1123(final String rfc1123DateTime) {
    return ZonedDateTime.parse(rfc1123DateTime, RFC1123Formatter);
  }
  
  public static ZonedDateTime convertLocalTimeStampToUTC(final long timeStamp) {
    LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), currentZoneId);
    ZonedDateTime zdt = ldt.atZone(currentZoneId);
    ZonedDateTime utcZoneDateTime = zdt.withZoneSameInstant(ZoneOffset.UTC);
    return utcZoneDateTime;
  }
}
