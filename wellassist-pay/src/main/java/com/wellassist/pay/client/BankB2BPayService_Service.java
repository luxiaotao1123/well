
package com.wellassist.pay.client;

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
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "bankB2bPayService", targetNamespace = "http://impl.service.pay.wellassist.com/", wsdlLocation = "http://localhost:8089/services/bankB2bPayService?wsdl")
public class BankB2BPayService_Service
    extends Service
{

    private final static URL BANKB2BPAYSERVICE_WSDL_LOCATION;
    private final static WebServiceException BANKB2BPAYSERVICE_EXCEPTION;
    private final static QName BANKB2BPAYSERVICE_QNAME = new QName("http://impl.service.pay.wellassist.com/", "bankB2bPayService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8089/services/bankB2bPayService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        BANKB2BPAYSERVICE_WSDL_LOCATION = url;
        BANKB2BPAYSERVICE_EXCEPTION = e;
    }

    public BankB2BPayService_Service() {
        super(__getWsdlLocation(), BANKB2BPAYSERVICE_QNAME);
    }

    public BankB2BPayService_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), BANKB2BPAYSERVICE_QNAME, features);
    }

    public BankB2BPayService_Service(URL wsdlLocation) {
        super(wsdlLocation, BANKB2BPAYSERVICE_QNAME);
    }

    public BankB2BPayService_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, BANKB2BPAYSERVICE_QNAME, features);
    }

    public BankB2BPayService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BankB2BPayService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns BankB2BPayService
     */
    @WebEndpoint(name = "BankB2bPayServiceImplPort")
    public BankB2BPayService getBankB2BPayServiceImplPort() {
        return super.getPort(new QName("http://impl.service.pay.wellassist.com/", "BankB2bPayServiceImplPort"), BankB2BPayService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns BankB2BPayService
     */
    @WebEndpoint(name = "BankB2bPayServiceImplPort")
    public BankB2BPayService getBankB2BPayServiceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://impl.service.pay.wellassist.com/", "BankB2bPayServiceImplPort"), BankB2BPayService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (BANKB2BPAYSERVICE_EXCEPTION!= null) {
            throw BANKB2BPAYSERVICE_EXCEPTION;
        }
        return BANKB2BPAYSERVICE_WSDL_LOCATION;
    }

}