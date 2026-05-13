package schema;

import java.io.Serializable;

public class Network {

    // a class that holds the request sent from the client
    public static class Request implements Serializable {
        private String action; // example: "LOGIN", "ADD_USER"
        private Object payload; // data sent with the request

        public Request(String action, Object payload) {
            this.action = action;
            this.payload = payload;
        }

        public String getAction() {
            return action;
        }

        public Object getPayload() {
            return payload;
        }
    }

    // a class that holds the response from the server
    public static class Response implements Serializable {
        private String status; // "SUCCESS" or "ERROR"
        private String message;
        private Object payload; // data sent back

        public Response(String status, String message, Object payload) {
            this.status = status;
            this.message = message;
            this.payload = payload;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Object getPayload() {
            return payload;
        }
    }
}
