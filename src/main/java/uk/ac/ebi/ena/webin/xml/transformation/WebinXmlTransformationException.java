package uk.ac.ebi.ena.webin.xml.transformation;

public class WebinXmlTransformationException extends RuntimeException {
    public WebinXmlTransformationException() {
        super();
    }

    public WebinXmlTransformationException(String message) {
        super(message);
    }

    public WebinXmlTransformationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebinXmlTransformationException(Throwable cause) {
        super(cause);
    }

    protected WebinXmlTransformationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
