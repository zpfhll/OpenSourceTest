package ll.zhao.opensourcememo.retrofit.response;

import java.io.Serializable;

/**
 * 送金アプリログイン
 */
public class Ap0002 extends BaseResponse{
    private String token_type;
    private String access_token;
    private long expires_in;
    private long consented_on;
    private String scope;
    private String metadata;

    public Ap0002(){
    }

    public Ap0002(String token_type, String access_token, long expires_in, long consented_on, String scope, String metadata) {
        this.token_type = token_type;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.consented_on = consented_on;
        this.scope = scope;
        this.metadata = metadata;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token.replaceAll("a:" , "");
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public long getConsented_on() {
        return consented_on;
    }

    public void setConsented_on(long consented_on) {
        this.consented_on = consented_on;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getMetadata() {
        metadata = metadata.replace("a:","");
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "Ap0002ResponseInfo{" +
                "token_type='" + token_type + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", consented_on=" + consented_on +
                ", scope='" + scope + '\'' +
                ", metadata='" + metadata + '\'' +
                '}';
    }
}
