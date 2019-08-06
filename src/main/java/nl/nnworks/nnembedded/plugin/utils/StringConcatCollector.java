package nl.nnworks.nnembedded.plugin.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * Completely for fun and learning how to make your own collector stuff. 
 * Could have been done with much less code :-)
 *
 */
public class StringConcatCollector implements Collector<String, StringBuilder, String> {

  public static StringConcatCollector collect(final String separator) {
    return new StringConcatCollector(separator);
  }
  
  private StringConcatCollector(final String separator) {
    this.separator = separator;
  }
  
  private String separator;
  
  @Override
  public Supplier<StringBuilder> supplier() {
    return new Supplier<StringBuilder>() {
      @Override
      public StringBuilder get() {
        return new StringBuilder();
      }
    };
  }

  @Override
  public BiConsumer<StringBuilder, String> accumulator() {
    return new BiConsumer<StringBuilder, String>() {
      @Override
      public void accept(StringBuilder accumulation, String part) {
        if (accumulation.length() > 0) {
          accumulation.append(separator).append(part);
        } else {
          accumulation.append(part);
        }
      }
    };
  }

  @Override
  public BinaryOperator<StringBuilder> combiner() {
    return new BinaryOperator<StringBuilder>() {
      @Override
      public StringBuilder apply(StringBuilder thisAccumulation, StringBuilder accumulationToAdd) {
        if (accumulationToAdd.length() > 0 && thisAccumulation.length() > 0) {
          return thisAccumulation.append(separator).append(accumulationToAdd);
        }
        return thisAccumulation.append(accumulationToAdd);
      }
    };
  }

  @Override
  public Function<StringBuilder, String> finisher() {
    return new Function<StringBuilder, String>() {
      @Override
      public String apply(StringBuilder accumulation) {
        return accumulation.toString();
      }
    };
  }

  @Override
  public Set<Characteristics> characteristics() {
    Set<Characteristics> characteristicsSet = new HashSet<Characteristics>();
    characteristicsSet.add(Characteristics.CONCURRENT);
    characteristicsSet.add(Characteristics.UNORDERED);
    return characteristicsSet;
  }
}
