package de.hsos.bachelorarbeit.nh.endpoint.coverage.frameworks.jmeter;

import java.util.regex.Pattern;

class JMeterPatterns{
    static final String _startPattern = "(\\s)*<HTTPSamplerProxy(.*)";
    static final String _endPattern = "(\\s)*</HTTPSamplerProxy>(\\s)*";
    static final String _commentPattern = "(\\s)*<stringProp name=\"TestPlan.comments\">(.*)</stringProp>(\\s)*";
    static final String _httpMethod = "<stringProp name=\"HTTPSampler.method\">(.*)</stringProp>";

    static final Pattern startPattern = Pattern.compile(JMeterPatterns._startPattern);
    static final Pattern endPattern = Pattern.compile(JMeterPatterns._endPattern);
    static final Pattern commentPattern = Pattern.compile(JMeterPatterns._commentPattern);
    static final Pattern httpMethod = Pattern.compile(JMeterPatterns._httpMethod);
}
