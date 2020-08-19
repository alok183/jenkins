import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import groovy.json.JsonSlurper;

// Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = [ new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
];

// Install the all-trusting trust manager
SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
HostnameVerifier allHostsValid = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};

// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
// Prepare a variable where we save parsed JSON as a HashMap, it's good for our use case, as we just need the 'name' of each tag.
def repo_response = [:]    
def url = new  URL("http://<host>/rest/api/latest/projects/<proejct_name>/repos")
repo_response = new JsonSlurper().parseText(url.getText('UTF-8'))
// println url.getText()
println repo_response

// Prepare a List to collect the tag names into
def repo_list = []
repo_list.add('<--Select-->')

// Iterate the HashMap of all Tags and grab only their "names" into our List
repo_response.values.each { tag_metadata ->
    repo_list.add(tag_metadata.slug) 
}

println repo_list
return repo_list.sort()
