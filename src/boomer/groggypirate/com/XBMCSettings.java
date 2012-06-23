package boomer.groggypirate.com;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 23/06/12
 * Time: 01:30
 * To change this template use File | Settings | File Templates.
 */
public class XBMCSettings {
    private static XBMCSettings ourInstance = new XBMCSettings();

    private String ipAddress;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    private String port;
    private String name = "BoomerSettings";


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public String getName() {
        return name;
    }



    public static XBMCSettings getInstance() {
        return ourInstance;
    }

    private XBMCSettings() {
    }
}
