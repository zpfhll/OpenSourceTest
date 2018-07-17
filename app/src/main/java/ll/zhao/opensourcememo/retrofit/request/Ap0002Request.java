package ll.zhao.opensourcememo.retrofit.request;

public class Ap0002Request extends BaseRequest {
    private String deviceID;

    public Ap0002Request(String deviceID){
     this.deviceID = deviceID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
