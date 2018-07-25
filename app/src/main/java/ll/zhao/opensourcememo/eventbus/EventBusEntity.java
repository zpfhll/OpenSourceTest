package ll.zhao.opensourcememo.eventbus;

public class EventBusEntity {
    private String id;
    private String message;

    public static final String EVENT_ACTIVITY = "EVENT_ACTIVITY";
    public static final String EVENT_SUB_ACTIVITY1 = "EVENT_SUB_ACTIVITY1";

    public EventBusEntity(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        if(id == null){
            id = "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        if(message == null){
            message = "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
