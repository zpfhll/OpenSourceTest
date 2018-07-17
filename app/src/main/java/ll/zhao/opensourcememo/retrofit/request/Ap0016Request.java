package ll.zhao.opensourcememo.retrofit.request;

public class Ap0016Request extends BaseRequest {
    private String bankCode;

    public  Ap0016Request(String bankCode){
        this.bankCode = bankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        bankCode = bankCode;
    }
}
