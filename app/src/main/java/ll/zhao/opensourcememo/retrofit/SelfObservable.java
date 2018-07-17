package ll.zhao.opensourcememo.retrofit;

import io.reactivex.Observable;

public class SelfObservable{

    private String tag = "";

    private Observable observable;

    public  SelfObservable(String tag,Observable observable){
        this.tag = tag;
        this.observable = observable;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Observable getObservable() {
        return observable;
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }
}
