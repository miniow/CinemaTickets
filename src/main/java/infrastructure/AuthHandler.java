/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure;

import infrastructure.repository.UserRepository;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import michal.com.application.AuthenticationService;
import org.hibernate.SessionFactory;

/**
 *
 * @author zymci
 */
public class AuthHandler implements SOAPHandler<SOAPMessageContext> {
    private final AuthenticationService authService;
    
    public AuthHandler() throws SQLException {
        this.authService = new AuthenticationService(new UserRepository(HibernateUtil.getSessionFactory()));
    }

     @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            return true;
        }

        QName operation = (QName) context.get(MessageContext.WSDL_OPERATION);
        String method = operation != null ? operation.getLocalPart() : "";
        
        Set<String> publicMethods = new HashSet<>();
        publicMethods.add("login");
        publicMethods.add("register");
        publicMethods.add("getAllMovies");
        publicMethods.add("getAllScreenings");

        if (publicMethods.contains(method)) {
            return true; // bez logowania do tych metod
        }

        try {
            String username = null;
            String password = null;

            SOAPMessage message = context.getMessage();
            SOAPHeader header = message.getSOAPHeader();
            if (header == null) {
                throw new SecurityException("Brak nagłówka SOAP");
            }

            // Iteracja przez elementy nagłówka
            Iterator<?> headerElements = header.getChildElements();
            while (headerElements.hasNext()) {
                Object element = headerElements.next();
                if (element instanceof SOAPElement) {
                    SOAPElement soapElement = (SOAPElement) element;
                    String localName = soapElement.getElementName().getLocalName();
                    if ("Username".equals(localName)) {
                        username = soapElement.getValue();
                    } else if ("Password".equals(localName)) {
                        password = soapElement.getValue();
                    }
                }
            }

            if (username == null || password == null) {
                throw new SecurityException("Brak loginu lub hasła w nagłówku");
            }

            if (!authService.validateCredentials(username, password)) {
                throw new SecurityException("Nieprawidłowy login lub hasło");
            }

            // Zapisz login w kontekście
            context.put("authenticatedUsername", username);
            context.setScope("authenticatedUsername", MessageContext.Scope.APPLICATION);

        } catch (SOAPException e) {
            throw new RuntimeException("Błąd przetwarzania wiadomości SOAP", e);
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }
}