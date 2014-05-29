package at.tuwien.sentimentanalyzer.converters;

import org.apache.camel.Converter;

/**
 * have to have it or remove the support for type converters completely.
 * it will complain if there are no converters at all
 * @author CLF
 *
 */
@Converter
public class DummyConverter {

}
