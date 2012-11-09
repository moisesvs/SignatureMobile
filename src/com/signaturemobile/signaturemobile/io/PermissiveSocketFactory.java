package com.signaturemobile.signaturemobile.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * PermissiveSocketFactory allows socket connections for all kind of certificates
 *  
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class PermissiveSocketFactory implements SocketFactory, LayeredSocketFactory {

    private SSLContext currentContext = null;

    public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        
        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        int soTimeout = HttpConnectionParams.getSoTimeout(params);

        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

        if ((localAddress != null) || (localPort > 0)) {
            if (localPort < 0) {
                localPort = 0;
            }
            InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
            sslsock.bind(isa);
        }

        sslsock.connect(remoteAddress, connTimeout);
        sslsock.setSoTimeout(soTimeout);
        return sslsock;
        
    }

    public Socket createSocket() throws IOException {
        return getCurrentContext().getSocketFactory().createSocket();
    }

    public boolean isSecure(Socket arg0) throws IllegalArgumentException {
        return true;
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return getCurrentContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    private SSLContext getCurrentContext() throws IOException {
        if (this.currentContext == null) {
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new TrustManager[] { new NullTrustManager() }, null);
                this.currentContext = context;
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
        }
        return this.currentContext;
    }

    /**
     * NullTrustManager allows all kind of certificates
     */
    public class NullTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }

        public boolean isClientTrusted(X509Certificate[] chain) {
            return true;
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return true;
        }

    }
}

