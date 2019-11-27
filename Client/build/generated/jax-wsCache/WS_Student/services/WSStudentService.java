
package services;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2-hudson-752-
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "WS_StudentService", targetNamespace = "http://services/", wsdlLocation = "http://localhost:8084/WSSSSSSSwww/WS_Student?wsdl")
public class WSStudentService
    extends Service
{

    private final static URL WSSTUDENTSERVICE_WSDL_LOCATION;
    private final static WebServiceException WSSTUDENTSERVICE_EXCEPTION;
    private final static QName WSSTUDENTSERVICE_QNAME = new QName("http://services/", "WS_StudentService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8084/WSSSSSSSwww/WS_Student?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        WSSTUDENTSERVICE_WSDL_LOCATION = url;
        WSSTUDENTSERVICE_EXCEPTION = e;
    }

    public WSStudentService() {
        super(__getWsdlLocation(), WSSTUDENTSERVICE_QNAME);
    }

    public WSStudentService(WebServiceFeature... features) {
        super(__getWsdlLocation(), WSSTUDENTSERVICE_QNAME, features);
    }

    public WSStudentService(URL wsdlLocation) {
        super(wsdlLocation, WSSTUDENTSERVICE_QNAME);
    }

    public WSStudentService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, WSSTUDENTSERVICE_QNAME, features);
    }

    public WSStudentService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WSStudentService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns WSStudent
     */
    @WebEndpoint(name = "WS_StudentPort")
    public WSStudent getWSStudentPort() {
        return super.getPort(new QName("http://services/", "WS_StudentPort"), WSStudent.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WSStudent
     */
    @WebEndpoint(name = "WS_StudentPort")
    public WSStudent getWSStudentPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://services/", "WS_StudentPort"), WSStudent.class, features);
    }

    private static URL __getWsdlLocation() {
        if (WSSTUDENTSERVICE_EXCEPTION!= null) {
            throw WSSTUDENTSERVICE_EXCEPTION;
        }
        return WSSTUDENTSERVICE_WSDL_LOCATION;
    }

}