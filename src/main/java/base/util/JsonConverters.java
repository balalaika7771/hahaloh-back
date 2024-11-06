package base.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static lombok.AccessLevel.PRIVATE;


@NoArgsConstructor(access = PRIVATE)
public class JsonConverters {

  public static final class NoUTCInstant implements Converter<String, Instant> {

    private final String STR_DATE_FORMAT = "dd.MM.yyyy";

    @Override
    @SneakyThrows
    public Instant convert(String value) {
      var format = new SimpleDateFormat(STR_DATE_FORMAT);
      // Добавляются 5 часов, потому что фронт кидает дату без времени
      return format.parse(value).toInstant().plus(Duration.ofHours(5));
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
      return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
      return typeFactory.constructType(Instant.class);
    }
  }
}
